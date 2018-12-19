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
import DTO.Condicionesalojamiento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gonza
 */
public class CondicionesalojamientoJpaController implements Serializable {

    public CondicionesalojamientoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Condicionesalojamiento condicionesalojamiento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alojamiento registroTurismo = condicionesalojamiento.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo = em.getReference(registroTurismo.getClass(), registroTurismo.getRegistroTurismo());
                condicionesalojamiento.setRegistroTurismo(registroTurismo);
            }
            em.persist(condicionesalojamiento);
            if (registroTurismo != null) {
                registroTurismo.getCondicionesalojamientoList().add(condicionesalojamiento);
                registroTurismo = em.merge(registroTurismo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Condicionesalojamiento condicionesalojamiento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Condicionesalojamiento persistentCondicionesalojamiento = em.find(Condicionesalojamiento.class, condicionesalojamiento.getCodigoCond());
            Alojamiento registroTurismoOld = persistentCondicionesalojamiento.getRegistroTurismo();
            Alojamiento registroTurismoNew = condicionesalojamiento.getRegistroTurismo();
            if (registroTurismoNew != null) {
                registroTurismoNew = em.getReference(registroTurismoNew.getClass(), registroTurismoNew.getRegistroTurismo());
                condicionesalojamiento.setRegistroTurismo(registroTurismoNew);
            }
            condicionesalojamiento = em.merge(condicionesalojamiento);
            if (registroTurismoOld != null && !registroTurismoOld.equals(registroTurismoNew)) {
                registroTurismoOld.getCondicionesalojamientoList().remove(condicionesalojamiento);
                registroTurismoOld = em.merge(registroTurismoOld);
            }
            if (registroTurismoNew != null && !registroTurismoNew.equals(registroTurismoOld)) {
                registroTurismoNew.getCondicionesalojamientoList().add(condicionesalojamiento);
                registroTurismoNew = em.merge(registroTurismoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = condicionesalojamiento.getCodigoCond();
                if (findCondicionesalojamiento(id) == null) {
                    throw new NonexistentEntityException("The condicionesalojamiento with id " + id + " no longer exists.");
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
            Condicionesalojamiento condicionesalojamiento;
            try {
                condicionesalojamiento = em.getReference(Condicionesalojamiento.class, id);
                condicionesalojamiento.getCodigoCond();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The condicionesalojamiento with id " + id + " no longer exists.", enfe);
            }
            Alojamiento registroTurismo = condicionesalojamiento.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo.getCondicionesalojamientoList().remove(condicionesalojamiento);
                registroTurismo = em.merge(registroTurismo);
            }
            em.remove(condicionesalojamiento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Condicionesalojamiento> findCondicionesalojamientoEntities() {
        return findCondicionesalojamientoEntities(true, -1, -1);
    }

    public List<Condicionesalojamiento> findCondicionesalojamientoEntities(int maxResults, int firstResult) {
        return findCondicionesalojamientoEntities(false, maxResults, firstResult);
    }

    private List<Condicionesalojamiento> findCondicionesalojamientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Condicionesalojamiento.class));
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

    public Condicionesalojamiento findCondicionesalojamiento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Condicionesalojamiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getCondicionesalojamientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Condicionesalojamiento> rt = cq.from(Condicionesalojamiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
