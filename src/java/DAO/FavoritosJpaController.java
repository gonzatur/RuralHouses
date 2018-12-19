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
import DTO.Favoritos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author gonza
 */
public class FavoritosJpaController implements Serializable {

    public FavoritosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Favoritos favoritos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario nif = favoritos.getNif();
            if (nif != null) {
                nif = em.getReference(nif.getClass(), nif.getNif());
                favoritos.setNif(nif);
            }
            Alojamiento registroTurismo = favoritos.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo = em.getReference(registroTurismo.getClass(), registroTurismo.getRegistroTurismo());
                favoritos.setRegistroTurismo(registroTurismo);
            }
            em.persist(favoritos);
            if (nif != null) {
                nif.getFavoritosList().add(favoritos);
                nif = em.merge(nif);
            }
            if (registroTurismo != null) {
                registroTurismo.getFavoritosList().add(favoritos);
                registroTurismo = em.merge(registroTurismo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Favoritos favoritos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Favoritos persistentFavoritos = em.find(Favoritos.class, favoritos.getCodigoFav());
            Usuario nifOld = persistentFavoritos.getNif();
            Usuario nifNew = favoritos.getNif();
            Alojamiento registroTurismoOld = persistentFavoritos.getRegistroTurismo();
            Alojamiento registroTurismoNew = favoritos.getRegistroTurismo();
            if (nifNew != null) {
                nifNew = em.getReference(nifNew.getClass(), nifNew.getNif());
                favoritos.setNif(nifNew);
            }
            if (registroTurismoNew != null) {
                registroTurismoNew = em.getReference(registroTurismoNew.getClass(), registroTurismoNew.getRegistroTurismo());
                favoritos.setRegistroTurismo(registroTurismoNew);
            }
            favoritos = em.merge(favoritos);
            if (nifOld != null && !nifOld.equals(nifNew)) {
                nifOld.getFavoritosList().remove(favoritos);
                nifOld = em.merge(nifOld);
            }
            if (nifNew != null && !nifNew.equals(nifOld)) {
                nifNew.getFavoritosList().add(favoritos);
                nifNew = em.merge(nifNew);
            }
            if (registroTurismoOld != null && !registroTurismoOld.equals(registroTurismoNew)) {
                registroTurismoOld.getFavoritosList().remove(favoritos);
                registroTurismoOld = em.merge(registroTurismoOld);
            }
            if (registroTurismoNew != null && !registroTurismoNew.equals(registroTurismoOld)) {
                registroTurismoNew.getFavoritosList().add(favoritos);
                registroTurismoNew = em.merge(registroTurismoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = favoritos.getCodigoFav();
                if (findFavoritos(id) == null) {
                    throw new NonexistentEntityException("The favoritos with id " + id + " no longer exists.");
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
            Favoritos favoritos;
            try {
                favoritos = em.getReference(Favoritos.class, id);
                favoritos.getCodigoFav();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The favoritos with id " + id + " no longer exists.", enfe);
            }
            Usuario nif = favoritos.getNif();
            if (nif != null) {
                nif.getFavoritosList().remove(favoritos);
                nif = em.merge(nif);
            }
            Alojamiento registroTurismo = favoritos.getRegistroTurismo();
            if (registroTurismo != null) {
                registroTurismo.getFavoritosList().remove(favoritos);
                registroTurismo = em.merge(registroTurismo);
            }
            em.remove(favoritos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Favoritos> findFavoritosEntities() {
        return findFavoritosEntities(true, -1, -1);
    }

    public List<Favoritos> findFavoritosEntities(int maxResults, int firstResult) {
        return findFavoritosEntities(false, maxResults, firstResult);
    }

    private List<Favoritos> findFavoritosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Favoritos.class));
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

    public Favoritos findFavoritos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Favoritos.class, id);
        } finally {
            em.close();
        }
    }

    public int getFavoritosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Favoritos> rt = cq.from(Favoritos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Favoritos> findByNif(Usuario usu) {
        EntityManager em = getEntityManager();
        TypedQuery q = em.createNamedQuery("Favoritos.findByNif", Favoritos.class);
        q.setParameter("nif", usu);
        List<Favoritos> listadoFavoritos = q.getResultList();
        return listadoFavoritos;
    }

    public Favoritos findByNifAloj(Usuario nif, Alojamiento registroTurismo) {
        Favoritos fav = null;
        try {
            EntityManager em = getEntityManager();
            TypedQuery q = em.createNamedQuery("Favoritos.findByNifAndRegistroTurismo", Favoritos.class);
            q.setParameter("nif", nif);
            q.setParameter("registroTurismo", registroTurismo);
            fav = (Favoritos) q.getResultList().get(0);

        } catch (Exception e) {
            System.out.println(e);
        }
        return fav;
    }

}
