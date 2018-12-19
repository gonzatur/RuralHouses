/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Opinion;
import java.util.ArrayList;
import java.util.List;
import DTO.Cuentabancaria;
import DTO.Favoritos;
import DTO.Alojamiento;
import DTO.Reserva;
import DTO.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author gonza
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getOpinionList() == null) {
            usuario.setOpinionList(new ArrayList<Opinion>());
        }
        if (usuario.getCuentabancariaList() == null) {
            usuario.setCuentabancariaList(new ArrayList<Cuentabancaria>());
        }
        if (usuario.getFavoritosList() == null) {
            usuario.setFavoritosList(new ArrayList<Favoritos>());
        }
        if (usuario.getAlojamientoList() == null) {
            usuario.setAlojamientoList(new ArrayList<Alojamiento>());
        }
        if (usuario.getReservaList() == null) {
            usuario.setReservaList(new ArrayList<Reserva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Opinion> attachedOpinionList = new ArrayList<Opinion>();
            for (Opinion opinionListOpinionToAttach : usuario.getOpinionList()) {
                opinionListOpinionToAttach = em.getReference(opinionListOpinionToAttach.getClass(), opinionListOpinionToAttach.getCodigoOpinion());
                attachedOpinionList.add(opinionListOpinionToAttach);
            }
            usuario.setOpinionList(attachedOpinionList);
            List<Cuentabancaria> attachedCuentabancariaList = new ArrayList<Cuentabancaria>();
            for (Cuentabancaria cuentabancariaListCuentabancariaToAttach : usuario.getCuentabancariaList()) {
                cuentabancariaListCuentabancariaToAttach = em.getReference(cuentabancariaListCuentabancariaToAttach.getClass(), cuentabancariaListCuentabancariaToAttach.getTarjeta());
                attachedCuentabancariaList.add(cuentabancariaListCuentabancariaToAttach);
            }
            usuario.setCuentabancariaList(attachedCuentabancariaList);
            List<Favoritos> attachedFavoritosList = new ArrayList<Favoritos>();
            for (Favoritos favoritosListFavoritosToAttach : usuario.getFavoritosList()) {
                favoritosListFavoritosToAttach = em.getReference(favoritosListFavoritosToAttach.getClass(), favoritosListFavoritosToAttach.getCodigoFav());
                attachedFavoritosList.add(favoritosListFavoritosToAttach);
            }
            usuario.setFavoritosList(attachedFavoritosList);
            List<Alojamiento> attachedAlojamientoList = new ArrayList<Alojamiento>();
            for (Alojamiento alojamientoListAlojamientoToAttach : usuario.getAlojamientoList()) {
                alojamientoListAlojamientoToAttach = em.getReference(alojamientoListAlojamientoToAttach.getClass(), alojamientoListAlojamientoToAttach.getRegistroTurismo());
                attachedAlojamientoList.add(alojamientoListAlojamientoToAttach);
            }
            usuario.setAlojamientoList(attachedAlojamientoList);
            List<Reserva> attachedReservaList = new ArrayList<Reserva>();
            for (Reserva reservaListReservaToAttach : usuario.getReservaList()) {
                reservaListReservaToAttach = em.getReference(reservaListReservaToAttach.getClass(), reservaListReservaToAttach.getCodigoReserva());
                attachedReservaList.add(reservaListReservaToAttach);
            }
            usuario.setReservaList(attachedReservaList);
            em.persist(usuario);
            for (Opinion opinionListOpinion : usuario.getOpinionList()) {
                Usuario oldNifOfOpinionListOpinion = opinionListOpinion.getNif();
                opinionListOpinion.setNif(usuario);
                opinionListOpinion = em.merge(opinionListOpinion);
                if (oldNifOfOpinionListOpinion != null) {
                    oldNifOfOpinionListOpinion.getOpinionList().remove(opinionListOpinion);
                    oldNifOfOpinionListOpinion = em.merge(oldNifOfOpinionListOpinion);
                }
            }
            for (Cuentabancaria cuentabancariaListCuentabancaria : usuario.getCuentabancariaList()) {
                Usuario oldNifOfCuentabancariaListCuentabancaria = cuentabancariaListCuentabancaria.getNif();
                cuentabancariaListCuentabancaria.setNif(usuario);
                cuentabancariaListCuentabancaria = em.merge(cuentabancariaListCuentabancaria);
                if (oldNifOfCuentabancariaListCuentabancaria != null) {
                    oldNifOfCuentabancariaListCuentabancaria.getCuentabancariaList().remove(cuentabancariaListCuentabancaria);
                    oldNifOfCuentabancariaListCuentabancaria = em.merge(oldNifOfCuentabancariaListCuentabancaria);
                }
            }
            for (Favoritos favoritosListFavoritos : usuario.getFavoritosList()) {
                Usuario oldNifOfFavoritosListFavoritos = favoritosListFavoritos.getNif();
                favoritosListFavoritos.setNif(usuario);
                favoritosListFavoritos = em.merge(favoritosListFavoritos);
                if (oldNifOfFavoritosListFavoritos != null) {
                    oldNifOfFavoritosListFavoritos.getFavoritosList().remove(favoritosListFavoritos);
                    oldNifOfFavoritosListFavoritos = em.merge(oldNifOfFavoritosListFavoritos);
                }
            }
            for (Alojamiento alojamientoListAlojamiento : usuario.getAlojamientoList()) {
                Usuario oldNifOfAlojamientoListAlojamiento = alojamientoListAlojamiento.getNif();
                alojamientoListAlojamiento.setNif(usuario);
                alojamientoListAlojamiento = em.merge(alojamientoListAlojamiento);
                if (oldNifOfAlojamientoListAlojamiento != null) {
                    oldNifOfAlojamientoListAlojamiento.getAlojamientoList().remove(alojamientoListAlojamiento);
                    oldNifOfAlojamientoListAlojamiento = em.merge(oldNifOfAlojamientoListAlojamiento);
                }
            }
            for (Reserva reservaListReserva : usuario.getReservaList()) {
                Usuario oldNifOfReservaListReserva = reservaListReserva.getNif();
                reservaListReserva.setNif(usuario);
                reservaListReserva = em.merge(reservaListReserva);
                if (oldNifOfReservaListReserva != null) {
                    oldNifOfReservaListReserva.getReservaList().remove(reservaListReserva);
                    oldNifOfReservaListReserva = em.merge(oldNifOfReservaListReserva);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getNif()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getNif());
            List<Opinion> opinionListOld = persistentUsuario.getOpinionList();
            List<Opinion> opinionListNew = usuario.getOpinionList();
            List<Cuentabancaria> cuentabancariaListOld = persistentUsuario.getCuentabancariaList();
            List<Cuentabancaria> cuentabancariaListNew = usuario.getCuentabancariaList();
            List<Favoritos> favoritosListOld = persistentUsuario.getFavoritosList();
            List<Favoritos> favoritosListNew = usuario.getFavoritosList();
            List<Alojamiento> alojamientoListOld = persistentUsuario.getAlojamientoList();
            List<Alojamiento> alojamientoListNew = usuario.getAlojamientoList();
            List<Reserva> reservaListOld = persistentUsuario.getReservaList();
            List<Reserva> reservaListNew = usuario.getReservaList();
            List<String> illegalOrphanMessages = null;
            for (Opinion opinionListOldOpinion : opinionListOld) {
                if (!opinionListNew.contains(opinionListOldOpinion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Opinion " + opinionListOldOpinion + " since its nif field is not nullable.");
                }
            }
            for (Cuentabancaria cuentabancariaListOldCuentabancaria : cuentabancariaListOld) {
                if (!cuentabancariaListNew.contains(cuentabancariaListOldCuentabancaria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cuentabancaria " + cuentabancariaListOldCuentabancaria + " since its nif field is not nullable.");
                }
            }
            for (Favoritos favoritosListOldFavoritos : favoritosListOld) {
                if (!favoritosListNew.contains(favoritosListOldFavoritos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Favoritos " + favoritosListOldFavoritos + " since its nif field is not nullable.");
                }
            }
            for (Alojamiento alojamientoListOldAlojamiento : alojamientoListOld) {
                if (!alojamientoListNew.contains(alojamientoListOldAlojamiento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alojamiento " + alojamientoListOldAlojamiento + " since its nif field is not nullable.");
                }
            }
            for (Reserva reservaListOldReserva : reservaListOld) {
                if (!reservaListNew.contains(reservaListOldReserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reserva " + reservaListOldReserva + " since its nif field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Opinion> attachedOpinionListNew = new ArrayList<Opinion>();
            for (Opinion opinionListNewOpinionToAttach : opinionListNew) {
                opinionListNewOpinionToAttach = em.getReference(opinionListNewOpinionToAttach.getClass(), opinionListNewOpinionToAttach.getCodigoOpinion());
                attachedOpinionListNew.add(opinionListNewOpinionToAttach);
            }
            opinionListNew = attachedOpinionListNew;
            usuario.setOpinionList(opinionListNew);
            List<Cuentabancaria> attachedCuentabancariaListNew = new ArrayList<Cuentabancaria>();
            for (Cuentabancaria cuentabancariaListNewCuentabancariaToAttach : cuentabancariaListNew) {
                cuentabancariaListNewCuentabancariaToAttach = em.getReference(cuentabancariaListNewCuentabancariaToAttach.getClass(), cuentabancariaListNewCuentabancariaToAttach.getTarjeta());
                attachedCuentabancariaListNew.add(cuentabancariaListNewCuentabancariaToAttach);
            }
            cuentabancariaListNew = attachedCuentabancariaListNew;
            usuario.setCuentabancariaList(cuentabancariaListNew);
            List<Favoritos> attachedFavoritosListNew = new ArrayList<Favoritos>();
            for (Favoritos favoritosListNewFavoritosToAttach : favoritosListNew) {
                favoritosListNewFavoritosToAttach = em.getReference(favoritosListNewFavoritosToAttach.getClass(), favoritosListNewFavoritosToAttach.getCodigoFav());
                attachedFavoritosListNew.add(favoritosListNewFavoritosToAttach);
            }
            favoritosListNew = attachedFavoritosListNew;
            usuario.setFavoritosList(favoritosListNew);
            List<Alojamiento> attachedAlojamientoListNew = new ArrayList<Alojamiento>();
            for (Alojamiento alojamientoListNewAlojamientoToAttach : alojamientoListNew) {
                alojamientoListNewAlojamientoToAttach = em.getReference(alojamientoListNewAlojamientoToAttach.getClass(), alojamientoListNewAlojamientoToAttach.getRegistroTurismo());
                attachedAlojamientoListNew.add(alojamientoListNewAlojamientoToAttach);
            }
            alojamientoListNew = attachedAlojamientoListNew;
            usuario.setAlojamientoList(alojamientoListNew);
            List<Reserva> attachedReservaListNew = new ArrayList<Reserva>();
            for (Reserva reservaListNewReservaToAttach : reservaListNew) {
                reservaListNewReservaToAttach = em.getReference(reservaListNewReservaToAttach.getClass(), reservaListNewReservaToAttach.getCodigoReserva());
                attachedReservaListNew.add(reservaListNewReservaToAttach);
            }
            reservaListNew = attachedReservaListNew;
            usuario.setReservaList(reservaListNew);
            usuario = em.merge(usuario);
            for (Opinion opinionListNewOpinion : opinionListNew) {
                if (!opinionListOld.contains(opinionListNewOpinion)) {
                    Usuario oldNifOfOpinionListNewOpinion = opinionListNewOpinion.getNif();
                    opinionListNewOpinion.setNif(usuario);
                    opinionListNewOpinion = em.merge(opinionListNewOpinion);
                    if (oldNifOfOpinionListNewOpinion != null && !oldNifOfOpinionListNewOpinion.equals(usuario)) {
                        oldNifOfOpinionListNewOpinion.getOpinionList().remove(opinionListNewOpinion);
                        oldNifOfOpinionListNewOpinion = em.merge(oldNifOfOpinionListNewOpinion);
                    }
                }
            }
            for (Cuentabancaria cuentabancariaListNewCuentabancaria : cuentabancariaListNew) {
                if (!cuentabancariaListOld.contains(cuentabancariaListNewCuentabancaria)) {
                    Usuario oldNifOfCuentabancariaListNewCuentabancaria = cuentabancariaListNewCuentabancaria.getNif();
                    cuentabancariaListNewCuentabancaria.setNif(usuario);
                    cuentabancariaListNewCuentabancaria = em.merge(cuentabancariaListNewCuentabancaria);
                    if (oldNifOfCuentabancariaListNewCuentabancaria != null && !oldNifOfCuentabancariaListNewCuentabancaria.equals(usuario)) {
                        oldNifOfCuentabancariaListNewCuentabancaria.getCuentabancariaList().remove(cuentabancariaListNewCuentabancaria);
                        oldNifOfCuentabancariaListNewCuentabancaria = em.merge(oldNifOfCuentabancariaListNewCuentabancaria);
                    }
                }
            }
            for (Favoritos favoritosListNewFavoritos : favoritosListNew) {
                if (!favoritosListOld.contains(favoritosListNewFavoritos)) {
                    Usuario oldNifOfFavoritosListNewFavoritos = favoritosListNewFavoritos.getNif();
                    favoritosListNewFavoritos.setNif(usuario);
                    favoritosListNewFavoritos = em.merge(favoritosListNewFavoritos);
                    if (oldNifOfFavoritosListNewFavoritos != null && !oldNifOfFavoritosListNewFavoritos.equals(usuario)) {
                        oldNifOfFavoritosListNewFavoritos.getFavoritosList().remove(favoritosListNewFavoritos);
                        oldNifOfFavoritosListNewFavoritos = em.merge(oldNifOfFavoritosListNewFavoritos);
                    }
                }
            }
            for (Alojamiento alojamientoListNewAlojamiento : alojamientoListNew) {
                if (!alojamientoListOld.contains(alojamientoListNewAlojamiento)) {
                    Usuario oldNifOfAlojamientoListNewAlojamiento = alojamientoListNewAlojamiento.getNif();
                    alojamientoListNewAlojamiento.setNif(usuario);
                    alojamientoListNewAlojamiento = em.merge(alojamientoListNewAlojamiento);
                    if (oldNifOfAlojamientoListNewAlojamiento != null && !oldNifOfAlojamientoListNewAlojamiento.equals(usuario)) {
                        oldNifOfAlojamientoListNewAlojamiento.getAlojamientoList().remove(alojamientoListNewAlojamiento);
                        oldNifOfAlojamientoListNewAlojamiento = em.merge(oldNifOfAlojamientoListNewAlojamiento);
                    }
                }
            }
            for (Reserva reservaListNewReserva : reservaListNew) {
                if (!reservaListOld.contains(reservaListNewReserva)) {
                    Usuario oldNifOfReservaListNewReserva = reservaListNewReserva.getNif();
                    reservaListNewReserva.setNif(usuario);
                    reservaListNewReserva = em.merge(reservaListNewReserva);
                    if (oldNifOfReservaListNewReserva != null && !oldNifOfReservaListNewReserva.equals(usuario)) {
                        oldNifOfReservaListNewReserva.getReservaList().remove(reservaListNewReserva);
                        oldNifOfReservaListNewReserva = em.merge(oldNifOfReservaListNewReserva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getNif();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getNif();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Opinion> opinionListOrphanCheck = usuario.getOpinionList();
            for (Opinion opinionListOrphanCheckOpinion : opinionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Opinion " + opinionListOrphanCheckOpinion + " in its opinionList field has a non-nullable nif field.");
            }
            List<Cuentabancaria> cuentabancariaListOrphanCheck = usuario.getCuentabancariaList();
            for (Cuentabancaria cuentabancariaListOrphanCheckCuentabancaria : cuentabancariaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Cuentabancaria " + cuentabancariaListOrphanCheckCuentabancaria + " in its cuentabancariaList field has a non-nullable nif field.");
            }
            List<Favoritos> favoritosListOrphanCheck = usuario.getFavoritosList();
            for (Favoritos favoritosListOrphanCheckFavoritos : favoritosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Favoritos " + favoritosListOrphanCheckFavoritos + " in its favoritosList field has a non-nullable nif field.");
            }
            List<Alojamiento> alojamientoListOrphanCheck = usuario.getAlojamientoList();
            for (Alojamiento alojamientoListOrphanCheckAlojamiento : alojamientoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Alojamiento " + alojamientoListOrphanCheckAlojamiento + " in its alojamientoList field has a non-nullable nif field.");
            }
            List<Reserva> reservaListOrphanCheck = usuario.getReservaList();
            for (Reserva reservaListOrphanCheckReserva : reservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Reserva " + reservaListOrphanCheckReserva + " in its reservaList field has a non-nullable nif field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Usuario findByEmail(String email) {
        Usuario usu = null;
        try {
            EntityManager em = getEntityManager();
            TypedQuery q = em.createNamedQuery("Usuario.findByEmail", Usuario.class);
            q.setParameter("email", email);
            usu = (Usuario) q.getResultList().get(0);
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return usu;
    }
    
}
