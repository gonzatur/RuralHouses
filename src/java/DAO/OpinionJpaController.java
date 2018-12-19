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
import DTO.Usuario;
import DTO.Alojamiento;
import DTO.Opinion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author gonza
 */
public class OpinionJpaController implements Serializable {

    public OpinionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Opinion opinion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario nif = opinion.getNif();
            if (nif != null) {
                nif = em.getReference(nif.getClass(), nif.getNif());
                opinion.setNif(nif);
            }
            Alojamiento registroTurismo = opinion.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo = em.getReference(registroTurismo.getClass(), registroTurismo.getRegistroTurismo());
                opinion.setRegistroTurismo(registroTurismo);
            }
            em.persist(opinion);
            if (nif != null) {
                nif.getOpinionList().add(opinion);
                nif = em.merge(nif);
            }
            if (registroTurismo != null) {
                registroTurismo.getOpinionList().add(opinion);
                registroTurismo = em.merge(registroTurismo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Opinion opinion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Opinion persistentOpinion = em.find(Opinion.class, opinion.getCodigoOpinion());
            Usuario nifOld = persistentOpinion.getNif();
            Usuario nifNew = opinion.getNif();
            Alojamiento registroTurismoOld = persistentOpinion.getRegistroTurismo();
            Alojamiento registroTurismoNew = opinion.getRegistroTurismo();
            if (nifNew != null) {
                nifNew = em.getReference(nifNew.getClass(), nifNew.getNif());
                opinion.setNif(nifNew);
            }
            if (registroTurismoNew != null) {
                registroTurismoNew = em.getReference(registroTurismoNew.getClass(), registroTurismoNew.getRegistroTurismo());
                opinion.setRegistroTurismo(registroTurismoNew);
            }
            opinion = em.merge(opinion);
            if (nifOld != null && !nifOld.equals(nifNew)) {
                nifOld.getOpinionList().remove(opinion);
                nifOld = em.merge(nifOld);
            }
            if (nifNew != null && !nifNew.equals(nifOld)) {
                nifNew.getOpinionList().add(opinion);
                nifNew = em.merge(nifNew);
            }
            if (registroTurismoOld != null && !registroTurismoOld.equals(registroTurismoNew)) {
                registroTurismoOld.getOpinionList().remove(opinion);
                registroTurismoOld = em.merge(registroTurismoOld);
            }
            if (registroTurismoNew != null && !registroTurismoNew.equals(registroTurismoOld)) {
                registroTurismoNew.getOpinionList().add(opinion);
                registroTurismoNew = em.merge(registroTurismoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = opinion.getCodigoOpinion();
                if (findOpinion(id) == null) {
                    throw new NonexistentEntityException("The opinion with id " + id + " no longer exists.");
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
            Opinion opinion;
            try {
                opinion = em.getReference(Opinion.class, id);
                opinion.getCodigoOpinion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The opinion with id " + id + " no longer exists.", enfe);
            }
            Usuario nif = opinion.getNif();
            if (nif != null) {
                nif.getOpinionList().remove(opinion);
                nif = em.merge(nif);
            }
            Alojamiento registroTurismo = opinion.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo.getOpinionList().remove(opinion);
                registroTurismo = em.merge(registroTurismo);
            }
            em.remove(opinion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Opinion> findOpinionEntities() {
        return findOpinionEntities(true, -1, -1);
    }

    public List<Opinion> findOpinionEntities(int maxResults, int firstResult) {
        return findOpinionEntities(false, maxResults, firstResult);
    }

    private List<Opinion> findOpinionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Opinion.class));
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

    public Opinion findOpinion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Opinion.class, id);
        } finally {
            em.close();
        }
    }

    public int getOpinionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Opinion> rt = cq.from(Opinion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Opinion> findByDen(int codDenuncia) {
        EntityManager em = getEntityManager();
        TypedQuery q = em.createNamedQuery("Opinion.findByDenuncia", Opinion.class);
        q.setParameter("denuncia", codDenuncia);
        List<Opinion> listadoOpiniones = q.getResultList();
        return listadoOpiniones;
    }

    public List<Opinion> findByAlojamiento(Alojamiento aloj) {
        EntityManager em = getEntityManager();
        TypedQuery q = em.createNamedQuery("Opinion.findByCodAlojamiento", Opinion.class);
        q.setParameter("registroTurismo", aloj);
        List<Opinion> listadoOpiniones = q.getResultList();
        return listadoOpiniones;
    }

    public List<Opinion> findByNif(Usuario usu) {
        EntityManager em = getEntityManager();
        TypedQuery q = em.createNamedQuery("Opinion.findByNif", Opinion.class);
        q.setParameter("nif", usu);
        List<Opinion> listadoOpiniones = q.getResultList();
        return listadoOpiniones;
    }

}
