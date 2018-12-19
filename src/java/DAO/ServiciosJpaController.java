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
import DTO.Servicio;
import DTO.Servicios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author gonza
 */
public class ServiciosJpaController implements Serializable {

    public ServiciosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Servicios servicios) {
        if (servicios.getServicioList() == null) {
            servicios.setServicioList(new ArrayList<Servicio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Servicio> attachedServicioList = new ArrayList<Servicio>();
            for (Servicio servicioListServicioToAttach : servicios.getServicioList()) {
                servicioListServicioToAttach = em.getReference(servicioListServicioToAttach.getClass(), servicioListServicioToAttach.getCodS());
                attachedServicioList.add(servicioListServicioToAttach);
            }
            servicios.setServicioList(attachedServicioList);
            em.persist(servicios);
            for (Servicio servicioListServicio : servicios.getServicioList()) {
                Servicios oldCodigoServicioOfServicioListServicio = servicioListServicio.getCodigoServicio();
                servicioListServicio.setCodigoServicio(servicios);
                servicioListServicio = em.merge(servicioListServicio);
                if (oldCodigoServicioOfServicioListServicio != null) {
                    oldCodigoServicioOfServicioListServicio.getServicioList().remove(servicioListServicio);
                    oldCodigoServicioOfServicioListServicio = em.merge(oldCodigoServicioOfServicioListServicio);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Servicios servicios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Servicios persistentServicios = em.find(Servicios.class, servicios.getCodigoServicio());
            List<Servicio> servicioListOld = persistentServicios.getServicioList();
            List<Servicio> servicioListNew = servicios.getServicioList();
            List<String> illegalOrphanMessages = null;
            for (Servicio servicioListOldServicio : servicioListOld) {
                if (!servicioListNew.contains(servicioListOldServicio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Servicio " + servicioListOldServicio + " since its codigoServicio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Servicio> attachedServicioListNew = new ArrayList<Servicio>();
            for (Servicio servicioListNewServicioToAttach : servicioListNew) {
                servicioListNewServicioToAttach = em.getReference(servicioListNewServicioToAttach.getClass(), servicioListNewServicioToAttach.getCodS());
                attachedServicioListNew.add(servicioListNewServicioToAttach);
            }
            servicioListNew = attachedServicioListNew;
            servicios.setServicioList(servicioListNew);
            servicios = em.merge(servicios);
            for (Servicio servicioListNewServicio : servicioListNew) {
                if (!servicioListOld.contains(servicioListNewServicio)) {
                    Servicios oldCodigoServicioOfServicioListNewServicio = servicioListNewServicio.getCodigoServicio();
                    servicioListNewServicio.setCodigoServicio(servicios);
                    servicioListNewServicio = em.merge(servicioListNewServicio);
                    if (oldCodigoServicioOfServicioListNewServicio != null && !oldCodigoServicioOfServicioListNewServicio.equals(servicios)) {
                        oldCodigoServicioOfServicioListNewServicio.getServicioList().remove(servicioListNewServicio);
                        oldCodigoServicioOfServicioListNewServicio = em.merge(oldCodigoServicioOfServicioListNewServicio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = servicios.getCodigoServicio();
                if (findServicios(id) == null) {
                    throw new NonexistentEntityException("The servicios with id " + id + " no longer exists.");
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
            Servicios servicios;
            try {
                servicios = em.getReference(Servicios.class, id);
                servicios.getCodigoServicio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The servicios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Servicio> servicioListOrphanCheck = servicios.getServicioList();
            for (Servicio servicioListOrphanCheckServicio : servicioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Servicios (" + servicios + ") cannot be destroyed since the Servicio " + servicioListOrphanCheckServicio + " in its servicioList field has a non-nullable codigoServicio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(servicios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Servicios> findServiciosEntities() {
        return findServiciosEntities(true, -1, -1);
    }

    public List<Servicios> findServiciosEntities(int maxResults, int firstResult) {
        return findServiciosEntities(false, maxResults, firstResult);
    }

    private List<Servicios> findServiciosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Servicios.class));
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

    public Servicios findServicios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Servicios.class, id);
        } finally {
            em.close();
        }
    }

    public int getServiciosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Servicios> rt = cq.from(Servicios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
