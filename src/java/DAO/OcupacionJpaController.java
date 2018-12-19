/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Alojamiento;
import DTO.Ocupacion;
import DTO.OcupacionPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author gonza
 */
public class OcupacionJpaController implements Serializable {

    public OcupacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ocupacion ocupacion) throws PreexistingEntityException, Exception {
        if (ocupacion.getOcupacionPK() == null) {
            ocupacion.setOcupacionPK(new OcupacionPK());
        }
        ocupacion.getOcupacionPK().setRegistroTurismo(ocupacion.getAlojamiento().getRegistroTurismo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alojamiento alojamiento = ocupacion.getAlojamiento();
            if (alojamiento != null) {
                alojamiento = em.getReference(alojamiento.getClass(), alojamiento.getRegistroTurismo());
                ocupacion.setAlojamiento(alojamiento);
            }
            em.persist(ocupacion);
            if (alojamiento != null) {
                alojamiento.getOcupacionList().add(ocupacion);
                alojamiento = em.merge(alojamiento);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOcupacion(ocupacion.getOcupacionPK()) != null) {
                throw new PreexistingEntityException("Ocupacion " + ocupacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ocupacion ocupacion) throws NonexistentEntityException, Exception {
        ocupacion.getOcupacionPK().setRegistroTurismo(ocupacion.getAlojamiento().getRegistroTurismo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ocupacion persistentOcupacion = em.find(Ocupacion.class, ocupacion.getOcupacionPK());
            Alojamiento alojamientoOld = persistentOcupacion.getAlojamiento();
            Alojamiento alojamientoNew = ocupacion.getAlojamiento();
            if (alojamientoNew != null) {
                alojamientoNew = em.getReference(alojamientoNew.getClass(), alojamientoNew.getRegistroTurismo());
                ocupacion.setAlojamiento(alojamientoNew);
            }
            ocupacion = em.merge(ocupacion);
            if (alojamientoOld != null && !alojamientoOld.equals(alojamientoNew)) {
                alojamientoOld.getOcupacionList().remove(ocupacion);
                alojamientoOld = em.merge(alojamientoOld);
            }
            if (alojamientoNew != null && !alojamientoNew.equals(alojamientoOld)) {
                alojamientoNew.getOcupacionList().add(ocupacion);
                alojamientoNew = em.merge(alojamientoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                OcupacionPK id = ocupacion.getOcupacionPK();
                if (findOcupacion(id) == null) {
                    throw new NonexistentEntityException("The ocupacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(OcupacionPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ocupacion ocupacion;
            try {
                ocupacion = em.getReference(Ocupacion.class, id);
                ocupacion.getOcupacionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ocupacion with id " + id + " no longer exists.", enfe);
            }
            Alojamiento alojamiento = ocupacion.getAlojamiento();
            if (alojamiento != null) {
                alojamiento.getOcupacionList().remove(ocupacion);
                alojamiento = em.merge(alojamiento);
            }
            em.remove(ocupacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ocupacion> findOcupacionEntities() {
        return findOcupacionEntities(true, -1, -1);
    }

    public List<Ocupacion> findOcupacionEntities(int maxResults, int firstResult) {
        return findOcupacionEntities(false, maxResults, firstResult);
    }

    private List<Ocupacion> findOcupacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ocupacion.class));
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

    public Ocupacion findOcupacion(OcupacionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ocupacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getOcupacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ocupacion> rt = cq.from(Ocupacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List findByOcupacion(String aloj) {
        EntityManager em = getEntityManager();
            TypedQuery q = em.createNamedQuery("Ocupacion.findByRegistroTurismo", Alojamiento.class);
            q.setParameter("registroTurismo", aloj);
            List <Alojamiento> listadoAlojamientos = q.getResultList();
            return listadoAlojamientos;
    }
    
}
