/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import DTO.Poblacion;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author gonza
 */
public class PoblacionJpaController implements Serializable {

    public PoblacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Poblacion poblacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(poblacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Poblacion poblacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            poblacion = em.merge(poblacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = poblacion.getCodigoPoblacion();
                if (findPoblacion(id) == null) {
                    throw new NonexistentEntityException("The poblacion with id " + id + " no longer exists.");
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
            Poblacion poblacion;
            try {
                poblacion = em.getReference(Poblacion.class, id);
                poblacion.getCodigoPoblacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The poblacion with id " + id + " no longer exists.", enfe);
            }
            em.remove(poblacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Poblacion> findPoblacionEntities() {
        return findPoblacionEntities(true, -1, -1);
    }

    public List<Poblacion> findPoblacionEntities(int maxResults, int firstResult) {
        return findPoblacionEntities(false, maxResults, firstResult);
    }

    private List<Poblacion> findPoblacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Poblacion.class));
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

    public Poblacion findPoblacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Poblacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getPoblacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Poblacion> rt = cq.from(Poblacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Poblacion> findByNumVisitas() {
        EntityManager em = getEntityManager();
            TypedQuery q = em.createNamedQuery("Poblacion.findByBusquedas", Poblacion.class);
            List <Poblacion> listadoPoblacionesVis = q.getResultList();
            return listadoPoblacionesVis;
    }
    
}
