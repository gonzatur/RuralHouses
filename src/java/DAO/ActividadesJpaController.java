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
import DTO.Actividad;
import DTO.Actividades;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gonza
 */
public class ActividadesJpaController implements Serializable {

    public ActividadesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Actividades actividades) {
        if (actividades.getActividadList() == null) {
            actividades.setActividadList(new ArrayList<Actividad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Actividad> attachedActividadList = new ArrayList<Actividad>();
            for (Actividad actividadListActividadToAttach : actividades.getActividadList()) {
                actividadListActividadToAttach = em.getReference(actividadListActividadToAttach.getClass(), actividadListActividadToAttach.getCodA());
                attachedActividadList.add(actividadListActividadToAttach);
            }
            actividades.setActividadList(attachedActividadList);
            em.persist(actividades);
            for (Actividad actividadListActividad : actividades.getActividadList()) {
                Actividades oldCodigoActividadOfActividadListActividad = actividadListActividad.getCodigoActividad();
                actividadListActividad.setCodigoActividad(actividades);
                actividadListActividad = em.merge(actividadListActividad);
                if (oldCodigoActividadOfActividadListActividad != null) {
                    oldCodigoActividadOfActividadListActividad.getActividadList().remove(actividadListActividad);
                    oldCodigoActividadOfActividadListActividad = em.merge(oldCodigoActividadOfActividadListActividad);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Actividades actividades) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividades persistentActividades = em.find(Actividades.class, actividades.getCodigoActividad());
            List<Actividad> actividadListOld = persistentActividades.getActividadList();
            List<Actividad> actividadListNew = actividades.getActividadList();
            List<String> illegalOrphanMessages = null;
            for (Actividad actividadListOldActividad : actividadListOld) {
                if (!actividadListNew.contains(actividadListOldActividad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Actividad " + actividadListOldActividad + " since its codigoActividad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Actividad> attachedActividadListNew = new ArrayList<Actividad>();
            for (Actividad actividadListNewActividadToAttach : actividadListNew) {
                actividadListNewActividadToAttach = em.getReference(actividadListNewActividadToAttach.getClass(), actividadListNewActividadToAttach.getCodA());
                attachedActividadListNew.add(actividadListNewActividadToAttach);
            }
            actividadListNew = attachedActividadListNew;
            actividades.setActividadList(actividadListNew);
            actividades = em.merge(actividades);
            for (Actividad actividadListNewActividad : actividadListNew) {
                if (!actividadListOld.contains(actividadListNewActividad)) {
                    Actividades oldCodigoActividadOfActividadListNewActividad = actividadListNewActividad.getCodigoActividad();
                    actividadListNewActividad.setCodigoActividad(actividades);
                    actividadListNewActividad = em.merge(actividadListNewActividad);
                    if (oldCodigoActividadOfActividadListNewActividad != null && !oldCodigoActividadOfActividadListNewActividad.equals(actividades)) {
                        oldCodigoActividadOfActividadListNewActividad.getActividadList().remove(actividadListNewActividad);
                        oldCodigoActividadOfActividadListNewActividad = em.merge(oldCodigoActividadOfActividadListNewActividad);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = actividades.getCodigoActividad();
                if (findActividades(id) == null) {
                    throw new NonexistentEntityException("The actividades with id " + id + " no longer exists.");
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
            Actividades actividades;
            try {
                actividades = em.getReference(Actividades.class, id);
                actividades.getCodigoActividad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The actividades with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Actividad> actividadListOrphanCheck = actividades.getActividadList();
            for (Actividad actividadListOrphanCheckActividad : actividadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Actividades (" + actividades + ") cannot be destroyed since the Actividad " + actividadListOrphanCheckActividad + " in its actividadList field has a non-nullable codigoActividad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(actividades);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Actividades> findActividadesEntities() {
        return findActividadesEntities(true, -1, -1);
    }

    public List<Actividades> findActividadesEntities(int maxResults, int firstResult) {
        return findActividadesEntities(false, maxResults, firstResult);
    }

    private List<Actividades> findActividadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Actividades.class));
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

    public Actividades findActividades(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Actividades.class, id);
        } finally {
            em.close();
        }
    }

    public int getActividadesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Actividades> rt = cq.from(Actividades.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
