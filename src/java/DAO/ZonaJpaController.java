/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Puebloszona;
import DTO.Zona;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gonza
 */
public class ZonaJpaController implements Serializable {

    public ZonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Zona zona) {
        if (zona.getPuebloszonaList() == null) {
            zona.setPuebloszonaList(new ArrayList<Puebloszona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Puebloszona> attachedPuebloszonaList = new ArrayList<Puebloszona>();
            for (Puebloszona puebloszonaListPuebloszonaToAttach : zona.getPuebloszonaList()) {
                puebloszonaListPuebloszonaToAttach = em.getReference(puebloszonaListPuebloszonaToAttach.getClass(), puebloszonaListPuebloszonaToAttach.getCodigoPueblosZona());
                attachedPuebloszonaList.add(puebloszonaListPuebloszonaToAttach);
            }
            zona.setPuebloszonaList(attachedPuebloszonaList);
            em.persist(zona);
            for (Puebloszona puebloszonaListPuebloszona : zona.getPuebloszonaList()) {
                Zona oldCodigoZonaOfPuebloszonaListPuebloszona = puebloszonaListPuebloszona.getCodigoZona();
                puebloszonaListPuebloszona.setCodigoZona(zona);
                puebloszonaListPuebloszona = em.merge(puebloszonaListPuebloszona);
                if (oldCodigoZonaOfPuebloszonaListPuebloszona != null) {
                    oldCodigoZonaOfPuebloszonaListPuebloszona.getPuebloszonaList().remove(puebloszonaListPuebloszona);
                    oldCodigoZonaOfPuebloszonaListPuebloszona = em.merge(oldCodigoZonaOfPuebloszonaListPuebloszona);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Zona zona) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Zona persistentZona = em.find(Zona.class, zona.getCodigoZona());
            List<Puebloszona> puebloszonaListOld = persistentZona.getPuebloszonaList();
            List<Puebloszona> puebloszonaListNew = zona.getPuebloszonaList();
            List<String> illegalOrphanMessages = null;
            for (Puebloszona puebloszonaListOldPuebloszona : puebloszonaListOld) {
                if (!puebloszonaListNew.contains(puebloszonaListOldPuebloszona)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Puebloszona " + puebloszonaListOldPuebloszona + " since its codigoZona field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Puebloszona> attachedPuebloszonaListNew = new ArrayList<Puebloszona>();
            for (Puebloszona puebloszonaListNewPuebloszonaToAttach : puebloszonaListNew) {
                puebloszonaListNewPuebloszonaToAttach = em.getReference(puebloszonaListNewPuebloszonaToAttach.getClass(), puebloszonaListNewPuebloszonaToAttach.getCodigoPueblosZona());
                attachedPuebloszonaListNew.add(puebloszonaListNewPuebloszonaToAttach);
            }
            puebloszonaListNew = attachedPuebloszonaListNew;
            zona.setPuebloszonaList(puebloszonaListNew);
            zona = em.merge(zona);
            for (Puebloszona puebloszonaListNewPuebloszona : puebloszonaListNew) {
                if (!puebloszonaListOld.contains(puebloszonaListNewPuebloszona)) {
                    Zona oldCodigoZonaOfPuebloszonaListNewPuebloszona = puebloszonaListNewPuebloszona.getCodigoZona();
                    puebloszonaListNewPuebloszona.setCodigoZona(zona);
                    puebloszonaListNewPuebloszona = em.merge(puebloszonaListNewPuebloszona);
                    if (oldCodigoZonaOfPuebloszonaListNewPuebloszona != null && !oldCodigoZonaOfPuebloszonaListNewPuebloszona.equals(zona)) {
                        oldCodigoZonaOfPuebloszonaListNewPuebloszona.getPuebloszonaList().remove(puebloszonaListNewPuebloszona);
                        oldCodigoZonaOfPuebloszonaListNewPuebloszona = em.merge(oldCodigoZonaOfPuebloszonaListNewPuebloszona);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = zona.getCodigoZona();
                if (findZona(id) == null) {
                    throw new NonexistentEntityException("The zona with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Zona zona;
            try {
                zona = em.getReference(Zona.class, id);
                zona.getCodigoZona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The zona with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Puebloszona> puebloszonaListOrphanCheck = zona.getPuebloszonaList();
            for (Puebloszona puebloszonaListOrphanCheckPuebloszona : puebloszonaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Zona (" + zona + ") cannot be destroyed since the Puebloszona " + puebloszonaListOrphanCheckPuebloszona + " in its puebloszonaList field has a non-nullable codigoZona field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(zona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Zona> findZonaEntities() {
        return findZonaEntities(true, -1, -1);
    }

    public List<Zona> findZonaEntities(int maxResults, int firstResult) {
        return findZonaEntities(false, maxResults, firstResult);
    }

    private List<Zona> findZonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Zona.class));
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

    public Zona findZona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Zona.class, id);
        } finally {
            em.close();
        }
    }

    public int getZonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Zona> rt = cq.from(Zona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
