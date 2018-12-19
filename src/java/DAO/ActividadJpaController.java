/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import DTO.Actividad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Actividades;
import DTO.Alojamiento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author gonza
 */
public class ActividadJpaController implements Serializable {

    public ActividadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Actividad actividad) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividades codigoActividad = actividad.getCodigoActividad();
            if (codigoActividad != null) {
                codigoActividad = em.getReference(codigoActividad.getClass(), codigoActividad.getCodigoActividad());
                actividad.setCodigoActividad(codigoActividad);
            }
            Alojamiento registroTurismo = actividad.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo = em.getReference(registroTurismo.getClass(), registroTurismo.getRegistroTurismo());
                actividad.setRegistroTurismo(registroTurismo);
            }
            em.persist(actividad);
            if (codigoActividad != null) {
                codigoActividad.getActividadList().add(actividad);
                codigoActividad = em.merge(codigoActividad);
            }
            if (registroTurismo != null) {
                registroTurismo.getActividadList().add(actividad);
                registroTurismo = em.merge(registroTurismo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Actividad actividad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad persistentActividad = em.find(Actividad.class, actividad.getCodA());
            Actividades codigoActividadOld = persistentActividad.getCodigoActividad();
            Actividades codigoActividadNew = actividad.getCodigoActividad();
            Alojamiento registroTurismoOld = persistentActividad.getRegistroTurismo();
            Alojamiento registroTurismoNew = actividad.getRegistroTurismo();
            if (codigoActividadNew != null) {
                codigoActividadNew = em.getReference(codigoActividadNew.getClass(), codigoActividadNew.getCodigoActividad());
                actividad.setCodigoActividad(codigoActividadNew);
            }
            if (registroTurismoNew != null) {
                registroTurismoNew = em.getReference(registroTurismoNew.getClass(), registroTurismoNew.getRegistroTurismo());
                actividad.setRegistroTurismo(registroTurismoNew);
            }
            actividad = em.merge(actividad);
            if (codigoActividadOld != null && !codigoActividadOld.equals(codigoActividadNew)) {
                codigoActividadOld.getActividadList().remove(actividad);
                codigoActividadOld = em.merge(codigoActividadOld);
            }
            if (codigoActividadNew != null && !codigoActividadNew.equals(codigoActividadOld)) {
                codigoActividadNew.getActividadList().add(actividad);
                codigoActividadNew = em.merge(codigoActividadNew);
            }
            if (registroTurismoOld != null && !registroTurismoOld.equals(registroTurismoNew)) {
                registroTurismoOld.getActividadList().remove(actividad);
                registroTurismoOld = em.merge(registroTurismoOld);
            }
            if (registroTurismoNew != null && !registroTurismoNew.equals(registroTurismoOld)) {
                registroTurismoNew.getActividadList().add(actividad);
                registroTurismoNew = em.merge(registroTurismoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = actividad.getCodA();
                if (findActividad(id) == null) {
                    throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad actividad;
            try {
                actividad = em.getReference(Actividad.class, id);
                actividad.getCodA();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.", enfe);
            }
            Actividades codigoActividad = actividad.getCodigoActividad();
            if (codigoActividad != null) {
                codigoActividad.getActividadList().remove(actividad);
                codigoActividad = em.merge(codigoActividad);
            }
            Alojamiento registroTurismo = actividad.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo.getActividadList().remove(actividad);
                registroTurismo = em.merge(registroTurismo);
            }
            em.remove(actividad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Actividad> findActividadEntities() {
        return findActividadEntities(true, -1, -1);
    }

    public List<Actividad> findActividadEntities(int maxResults, int firstResult) {
        return findActividadEntities(false, maxResults, firstResult);
    }

    private List<Actividad> findActividadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Actividad.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Actividad findActividad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Actividad.class, id);
        } finally {
            em.close();
        }
    }

    public int getActividadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Actividad> rt = cq.from(Actividad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Actividad> findByAlojamiento(Alojamiento aloja) {
        EntityManager em = getEntityManager();
            TypedQuery q = em.createNamedQuery("Actividad.findByCodAlojamiento", Actividad.class);
            q.setParameter("registroTurismo", aloja);
            List <Actividad> listadoActividades = q.getResultList();
            return listadoActividades;
    }
    
}
