/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Alojamiento;
import DTO.Servicio;
import DTO.Servicios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author gonza
 */
public class ServicioJpaController implements Serializable {

    public ServicioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Servicio servicio) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alojamiento registroTurismo = servicio.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo = em.getReference(registroTurismo.getClass(), registroTurismo.getRegistroTurismo());
                servicio.setRegistroTurismo(registroTurismo);
            }
            Servicios codigoServicio = servicio.getCodigoServicio();
            if (codigoServicio != null) {
                codigoServicio = em.getReference(codigoServicio.getClass(), codigoServicio.getCodigoServicio());
                servicio.setCodigoServicio(codigoServicio);
            }
            em.persist(servicio);
            if (registroTurismo != null) {
                registroTurismo.getServicioList().add(servicio);
                registroTurismo = em.merge(registroTurismo);
            }
            if (codigoServicio != null) {
                codigoServicio.getServicioList().add(servicio);
                codigoServicio = em.merge(codigoServicio);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Servicio servicio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Servicio persistentServicio = em.find(Servicio.class, servicio.getCodS());
            Alojamiento registroTurismoOld = persistentServicio.getRegistroTurismo();
            Alojamiento registroTurismoNew = servicio.getRegistroTurismo();
            Servicios codigoServicioOld = persistentServicio.getCodigoServicio();
            Servicios codigoServicioNew = servicio.getCodigoServicio();
            if (registroTurismoNew != null) {
                registroTurismoNew = em.getReference(registroTurismoNew.getClass(), registroTurismoNew.getRegistroTurismo());
                servicio.setRegistroTurismo(registroTurismoNew);
            }
            if (codigoServicioNew != null) {
                codigoServicioNew = em.getReference(codigoServicioNew.getClass(), codigoServicioNew.getCodigoServicio());
                servicio.setCodigoServicio(codigoServicioNew);
            }
            servicio = em.merge(servicio);
            if (registroTurismoOld != null && !registroTurismoOld.equals(registroTurismoNew)) {
                registroTurismoOld.getServicioList().remove(servicio);
                registroTurismoOld = em.merge(registroTurismoOld);
            }
            if (registroTurismoNew != null && !registroTurismoNew.equals(registroTurismoOld)) {
                registroTurismoNew.getServicioList().add(servicio);
                registroTurismoNew = em.merge(registroTurismoNew);
            }
            if (codigoServicioOld != null && !codigoServicioOld.equals(codigoServicioNew)) {
                codigoServicioOld.getServicioList().remove(servicio);
                codigoServicioOld = em.merge(codigoServicioOld);
            }
            if (codigoServicioNew != null && !codigoServicioNew.equals(codigoServicioOld)) {
                codigoServicioNew.getServicioList().add(servicio);
                codigoServicioNew = em.merge(codigoServicioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = servicio.getCodS();
                if (findServicio(id) == null) {
                    throw new NonexistentEntityException("The servicio with id " + id + " no longer exists.");
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
            Servicio servicio;
            try {
                servicio = em.getReference(Servicio.class, id);
                servicio.getCodS();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The servicio with id " + id + " no longer exists.", enfe);
            }
            Alojamiento registroTurismo = servicio.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo.getServicioList().remove(servicio);
                registroTurismo = em.merge(registroTurismo);
            }
            Servicios codigoServicio = servicio.getCodigoServicio();
            if (codigoServicio != null) {
                codigoServicio.getServicioList().remove(servicio);
                codigoServicio = em.merge(codigoServicio);
            }
            em.remove(servicio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Servicio> findServicioEntities() {
        return findServicioEntities(true, -1, -1);
    }

    public List<Servicio> findServicioEntities(int maxResults, int firstResult) {
        return findServicioEntities(false, maxResults, firstResult);
    }

    private List<Servicio> findServicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Servicio.class));
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

    public Servicio findServicio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Servicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getServicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Servicio> rt = cq.from(Servicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Servicio> findByAlojamiento(Alojamiento aloja) {
        EntityManager em = getEntityManager();
            TypedQuery q = em.createNamedQuery("Servicio.findByCodAlojamiento", Servicio.class);
            q.setParameter("registroTurismo", aloja);
            List <Servicio> listadoActividades = q.getResultList();
            return listadoActividades;
    }
    
}
