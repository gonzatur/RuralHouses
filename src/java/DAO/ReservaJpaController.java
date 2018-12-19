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
import DTO.Usuario;
import DTO.Alojamiento;
import DTO.Reserva;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author gonza
 */
public class ReservaJpaController implements Serializable {

    public ReservaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reserva reserva) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario nif = reserva.getNif();
            if (nif != null) {
                nif = em.getReference(nif.getClass(), nif.getNif());
                reserva.setNif(nif);
            }
            Alojamiento registroTurismo = reserva.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo = em.getReference(registroTurismo.getClass(), registroTurismo.getRegistroTurismo());
                reserva.setRegistroTurismo(registroTurismo);
            }
            em.persist(reserva);
            if (nif != null) {
                nif.getReservaList().add(reserva);
                nif = em.merge(nif);
            }
            if (registroTurismo != null) {
                registroTurismo.getReservaList().add(reserva);
                registroTurismo = em.merge(registroTurismo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReserva(reserva.getCodigoReserva()) != null) {
                throw new PreexistingEntityException("Reserva " + reserva + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reserva reserva) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reserva persistentReserva = em.find(Reserva.class, reserva.getCodigoReserva());
            Usuario nifOld = persistentReserva.getNif();
            Usuario nifNew = reserva.getNif();
            Alojamiento registroTurismoOld = persistentReserva.getRegistroTurismo();
            Alojamiento registroTurismoNew = reserva.getRegistroTurismo();
            if (nifNew != null) {
                nifNew = em.getReference(nifNew.getClass(), nifNew.getNif());
                reserva.setNif(nifNew);
            }
            if (registroTurismoNew != null) {
                registroTurismoNew = em.getReference(registroTurismoNew.getClass(), registroTurismoNew.getRegistroTurismo());
                reserva.setRegistroTurismo(registroTurismoNew);
            }
            reserva = em.merge(reserva);
            if (nifOld != null && !nifOld.equals(nifNew)) {
                nifOld.getReservaList().remove(reserva);
                nifOld = em.merge(nifOld);
            }
            if (nifNew != null && !nifNew.equals(nifOld)) {
                nifNew.getReservaList().add(reserva);
                nifNew = em.merge(nifNew);
            }
            if (registroTurismoOld != null && !registroTurismoOld.equals(registroTurismoNew)) {
                registroTurismoOld.getReservaList().remove(reserva);
                registroTurismoOld = em.merge(registroTurismoOld);
            }
            if (registroTurismoNew != null && !registroTurismoNew.equals(registroTurismoOld)) {
                registroTurismoNew.getReservaList().add(reserva);
                registroTurismoNew = em.merge(registroTurismoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = reserva.getCodigoReserva();
                if (findReserva(id) == null) {
                    throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.");
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
            Reserva reserva;
            try {
                reserva = em.getReference(Reserva.class, id);
                reserva.getCodigoReserva();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.", enfe);
            }
            Usuario nif = reserva.getNif();
            if (nif != null) {
                nif.getReservaList().remove(reserva);
                nif = em.merge(nif);
            }
            Alojamiento registroTurismo = reserva.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo.getReservaList().remove(reserva);
                registroTurismo = em.merge(registroTurismo);
            }
            em.remove(reserva);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reserva> findReservaEntities() {
        return findReservaEntities(true, -1, -1);
    }

    public List<Reserva> findReservaEntities(int maxResults, int firstResult) {
        return findReservaEntities(false, maxResults, firstResult);
    }

    private List<Reserva> findReservaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reserva.class));
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

    public Reserva findReserva(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reserva.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reserva> rt = cq.from(Reserva.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Reserva> findByNif(Usuario usu) {
        EntityManager em = getEntityManager();
            TypedQuery q = em.createNamedQuery("Reserva.findByNif", Reserva.class);
            q.setParameter("nif", usu);
            List <Reserva> listadoReservas = q.getResultList();
            return listadoReservas;
    }
    
}
