/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.PreexistingEntityException;
import DTO.Cuentabancaria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gonza
 */
public class CuentabancariaJpaController implements Serializable {

    public CuentabancariaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cuentabancaria cuentabancaria) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario nif = cuentabancaria.getNif();
            if (nif != null) {
                nif = em.getReference(nif.getClass(), nif.getNif());
                cuentabancaria.setNif(nif);
            }
            em.persist(cuentabancaria);
            if (nif != null) {
                nif.getCuentabancariaList().add(cuentabancaria);
                nif = em.merge(nif);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCuentabancaria(cuentabancaria.getTarjeta()) != null) {
                throw new PreexistingEntityException("Cuentabancaria " + cuentabancaria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cuentabancaria cuentabancaria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuentabancaria persistentCuentabancaria = em.find(Cuentabancaria.class, cuentabancaria.getTarjeta());
            Usuario nifOld = persistentCuentabancaria.getNif();
            Usuario nifNew = cuentabancaria.getNif();
            if (nifNew != null) {
                nifNew = em.getReference(nifNew.getClass(), nifNew.getNif());
                cuentabancaria.setNif(nifNew);
            }
            cuentabancaria = em.merge(cuentabancaria);
            if (nifOld != null && !nifOld.equals(nifNew)) {
                nifOld.getCuentabancariaList().remove(cuentabancaria);
                nifOld = em.merge(nifOld);
            }
            if (nifNew != null && !nifNew.equals(nifOld)) {
                nifNew.getCuentabancariaList().add(cuentabancaria);
                nifNew = em.merge(nifNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = cuentabancaria.getTarjeta();
                if (findCuentabancaria(id) == null) {
                    throw new NonexistentEntityException("The cuentabancaria with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuentabancaria cuentabancaria;
            try {
                cuentabancaria = em.getReference(Cuentabancaria.class, id);
                cuentabancaria.getTarjeta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuentabancaria with id " + id + " no longer exists.", enfe);
            }
            Usuario nif = cuentabancaria.getNif();
            if (nif != null) {
                nif.getCuentabancariaList().remove(cuentabancaria);
                nif = em.merge(nif);
            }
            em.remove(cuentabancaria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cuentabancaria> findCuentabancariaEntities() {
        return findCuentabancariaEntities(true, -1, -1);
    }

    public List<Cuentabancaria> findCuentabancariaEntities(int maxResults, int firstResult) {
        return findCuentabancariaEntities(false, maxResults, firstResult);
    }

    private List<Cuentabancaria> findCuentabancariaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cuentabancaria.class));
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

    public Cuentabancaria findCuentabancaria(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cuentabancaria.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuentabancariaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cuentabancaria> rt = cq.from(Cuentabancaria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
