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
import DTO.Usuario;
import DTO.Puebloszona;
import DTO.Servicio;
import java.util.ArrayList;
import java.util.List;
import DTO.Imagen;
import DTO.Condicionesalojamiento;
import DTO.Ocupacion;
import DTO.Actividad;
import DTO.Alojamiento;
import DTO.Opinion;
import DTO.Favoritos;
import DTO.Reserva;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author gonza
 */
public class AlojamientoJpaController implements Serializable {

    public AlojamientoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alojamiento alojamiento) throws PreexistingEntityException, Exception {
        if (alojamiento.getServicioList() == null) {
            alojamiento.setServicioList(new ArrayList<Servicio>());
        }
        if (alojamiento.getImagenList() == null) {
            alojamiento.setImagenList(new ArrayList<Imagen>());
        }
        if (alojamiento.getCondicionesalojamientoList() == null) {
            alojamiento.setCondicionesalojamientoList(new ArrayList<Condicionesalojamiento>());
        }
        if (alojamiento.getOcupacionList() == null) {
            alojamiento.setOcupacionList(new ArrayList<Ocupacion>());
        }
        if (alojamiento.getActividadList() == null) {
            alojamiento.setActividadList(new ArrayList<Actividad>());
        }
        if (alojamiento.getOpinionList() == null) {
            alojamiento.setOpinionList(new ArrayList<Opinion>());
        }
        if (alojamiento.getFavoritosList() == null) {
            alojamiento.setFavoritosList(new ArrayList<Favoritos>());
        }
        if (alojamiento.getReservaList() == null) {
            alojamiento.setReservaList(new ArrayList<Reserva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario nif = alojamiento.getNif();
            if (nif != null) {
                nif = em.getReference(nif.getClass(), nif.getNif());
                alojamiento.setNif(nif);
            }
            Puebloszona puebloszona = alojamiento.getPuebloszona();
            if (puebloszona != null) {
                puebloszona = em.getReference(puebloszona.getClass(), puebloszona.getCodigoPueblosZona());
                alojamiento.setPuebloszona(puebloszona);
            }
            List<Servicio> attachedServicioList = new ArrayList<Servicio>();
            for (Servicio servicioListServicioToAttach : alojamiento.getServicioList()) {
                servicioListServicioToAttach = em.getReference(servicioListServicioToAttach.getClass(), servicioListServicioToAttach.getCodS());
                attachedServicioList.add(servicioListServicioToAttach);
            }
            alojamiento.setServicioList(attachedServicioList);
            List<Imagen> attachedImagenList = new ArrayList<Imagen>();
            for (Imagen imagenListImagenToAttach : alojamiento.getImagenList()) {
                imagenListImagenToAttach = em.getReference(imagenListImagenToAttach.getClass(), imagenListImagenToAttach.getCodImagen());
                attachedImagenList.add(imagenListImagenToAttach);
            }
            alojamiento.setImagenList(attachedImagenList);
            List<Condicionesalojamiento> attachedCondicionesalojamientoList = new ArrayList<Condicionesalojamiento>();
            for (Condicionesalojamiento condicionesalojamientoListCondicionesalojamientoToAttach : alojamiento.getCondicionesalojamientoList()) {
                condicionesalojamientoListCondicionesalojamientoToAttach = em.getReference(condicionesalojamientoListCondicionesalojamientoToAttach.getClass(), condicionesalojamientoListCondicionesalojamientoToAttach.getCodigoCond());
                attachedCondicionesalojamientoList.add(condicionesalojamientoListCondicionesalojamientoToAttach);
            }
            alojamiento.setCondicionesalojamientoList(attachedCondicionesalojamientoList);
            List<Ocupacion> attachedOcupacionList = new ArrayList<Ocupacion>();
            for (Ocupacion ocupacionListOcupacionToAttach : alojamiento.getOcupacionList()) {
                ocupacionListOcupacionToAttach = em.getReference(ocupacionListOcupacionToAttach.getClass(), ocupacionListOcupacionToAttach.getOcupacionPK());
                attachedOcupacionList.add(ocupacionListOcupacionToAttach);
            }
            alojamiento.setOcupacionList(attachedOcupacionList);
            List<Actividad> attachedActividadList = new ArrayList<Actividad>();
            for (Actividad actividadListActividadToAttach : alojamiento.getActividadList()) {
                actividadListActividadToAttach = em.getReference(actividadListActividadToAttach.getClass(), actividadListActividadToAttach.getCodA());
                attachedActividadList.add(actividadListActividadToAttach);
            }
            alojamiento.setActividadList(attachedActividadList);
            List<Opinion> attachedOpinionList = new ArrayList<Opinion>();
            for (Opinion opinionListOpinionToAttach : alojamiento.getOpinionList()) {
                opinionListOpinionToAttach = em.getReference(opinionListOpinionToAttach.getClass(), opinionListOpinionToAttach.getCodigoOpinion());
                attachedOpinionList.add(opinionListOpinionToAttach);
            }
            alojamiento.setOpinionList(attachedOpinionList);
            List<Favoritos> attachedFavoritosList = new ArrayList<Favoritos>();
            for (Favoritos favoritosListFavoritosToAttach : alojamiento.getFavoritosList()) {
                favoritosListFavoritosToAttach = em.getReference(favoritosListFavoritosToAttach.getClass(), favoritosListFavoritosToAttach.getCodigoFav());
                attachedFavoritosList.add(favoritosListFavoritosToAttach);
            }
            alojamiento.setFavoritosList(attachedFavoritosList);
            List<Reserva> attachedReservaList = new ArrayList<Reserva>();
            for (Reserva reservaListReservaToAttach : alojamiento.getReservaList()) {
                reservaListReservaToAttach = em.getReference(reservaListReservaToAttach.getClass(), reservaListReservaToAttach.getCodigoReserva());
                attachedReservaList.add(reservaListReservaToAttach);
            }
            alojamiento.setReservaList(attachedReservaList);
            em.persist(alojamiento);
            if (nif != null) {
                nif.getAlojamientoList().add(alojamiento);
                nif = em.merge(nif);
            }
            if (puebloszona != null) {
                Alojamiento oldRegistroTurismoOfPuebloszona = puebloszona.getRegistroTurismo();
                if (oldRegistroTurismoOfPuebloszona != null) {
                    oldRegistroTurismoOfPuebloszona.setPuebloszona(null);
                    oldRegistroTurismoOfPuebloszona = em.merge(oldRegistroTurismoOfPuebloszona);
                }
                puebloszona.setRegistroTurismo(alojamiento);
                puebloszona = em.merge(puebloszona);
            }
            for (Servicio servicioListServicio : alojamiento.getServicioList()) {
                Alojamiento oldRegistroTurismoOfServicioListServicio = servicioListServicio.getRegistroTurismo();
                servicioListServicio.setRegistroTurismo(alojamiento);
                servicioListServicio = em.merge(servicioListServicio);
                if (oldRegistroTurismoOfServicioListServicio != null) {
                    oldRegistroTurismoOfServicioListServicio.getServicioList().remove(servicioListServicio);
                    oldRegistroTurismoOfServicioListServicio = em.merge(oldRegistroTurismoOfServicioListServicio);
                }
            }
            for (Imagen imagenListImagen : alojamiento.getImagenList()) {
                Alojamiento oldRegistroTurismoOfImagenListImagen = imagenListImagen.getRegistroTurismo();
                imagenListImagen.setRegistroTurismo(alojamiento);
                imagenListImagen = em.merge(imagenListImagen);
                if (oldRegistroTurismoOfImagenListImagen != null) {
                    oldRegistroTurismoOfImagenListImagen.getImagenList().remove(imagenListImagen);
                    oldRegistroTurismoOfImagenListImagen = em.merge(oldRegistroTurismoOfImagenListImagen);
                }
            }
            for (Condicionesalojamiento condicionesalojamientoListCondicionesalojamiento : alojamiento.getCondicionesalojamientoList()) {
                Alojamiento oldRegistroTurismoOfCondicionesalojamientoListCondicionesalojamiento = condicionesalojamientoListCondicionesalojamiento.getRegistroTurismo();
                condicionesalojamientoListCondicionesalojamiento.setRegistroTurismo(alojamiento);
                condicionesalojamientoListCondicionesalojamiento = em.merge(condicionesalojamientoListCondicionesalojamiento);
                if (oldRegistroTurismoOfCondicionesalojamientoListCondicionesalojamiento != null) {
                    oldRegistroTurismoOfCondicionesalojamientoListCondicionesalojamiento.getCondicionesalojamientoList().remove(condicionesalojamientoListCondicionesalojamiento);
                    oldRegistroTurismoOfCondicionesalojamientoListCondicionesalojamiento = em.merge(oldRegistroTurismoOfCondicionesalojamientoListCondicionesalojamiento);
                }
            }
            for (Ocupacion ocupacionListOcupacion : alojamiento.getOcupacionList()) {
                Alojamiento oldAlojamientoOfOcupacionListOcupacion = ocupacionListOcupacion.getAlojamiento();
                ocupacionListOcupacion.setAlojamiento(alojamiento);
                ocupacionListOcupacion = em.merge(ocupacionListOcupacion);
                if (oldAlojamientoOfOcupacionListOcupacion != null) {
                    oldAlojamientoOfOcupacionListOcupacion.getOcupacionList().remove(ocupacionListOcupacion);
                    oldAlojamientoOfOcupacionListOcupacion = em.merge(oldAlojamientoOfOcupacionListOcupacion);
                }
            }
            for (Actividad actividadListActividad : alojamiento.getActividadList()) {
                Alojamiento oldRegistroTurismoOfActividadListActividad = actividadListActividad.getRegistroTurismo();
                actividadListActividad.setRegistroTurismo(alojamiento);
                actividadListActividad = em.merge(actividadListActividad);
                if (oldRegistroTurismoOfActividadListActividad != null) {
                    oldRegistroTurismoOfActividadListActividad.getActividadList().remove(actividadListActividad);
                    oldRegistroTurismoOfActividadListActividad = em.merge(oldRegistroTurismoOfActividadListActividad);
                }
            }
            for (Opinion opinionListOpinion : alojamiento.getOpinionList()) {
                Alojamiento oldRegistroTurismoOfOpinionListOpinion = opinionListOpinion.getRegistroTurismo();
                opinionListOpinion.setRegistroTurismo(alojamiento);
                opinionListOpinion = em.merge(opinionListOpinion);
                if (oldRegistroTurismoOfOpinionListOpinion != null) {
                    oldRegistroTurismoOfOpinionListOpinion.getOpinionList().remove(opinionListOpinion);
                    oldRegistroTurismoOfOpinionListOpinion = em.merge(oldRegistroTurismoOfOpinionListOpinion);
                }
            }
            for (Favoritos favoritosListFavoritos : alojamiento.getFavoritosList()) {
                Alojamiento oldRegistroTurismoOfFavoritosListFavoritos = favoritosListFavoritos.getRegistroTurismo();
                favoritosListFavoritos.setRegistroTurismo(alojamiento);
                favoritosListFavoritos = em.merge(favoritosListFavoritos);
                if (oldRegistroTurismoOfFavoritosListFavoritos != null) {
                    oldRegistroTurismoOfFavoritosListFavoritos.getFavoritosList().remove(favoritosListFavoritos);
                    oldRegistroTurismoOfFavoritosListFavoritos = em.merge(oldRegistroTurismoOfFavoritosListFavoritos);
                }
            }
            for (Reserva reservaListReserva : alojamiento.getReservaList()) {
                Alojamiento oldRegistroTurismoOfReservaListReserva = reservaListReserva.getRegistroTurismo();
                reservaListReserva.setRegistroTurismo(alojamiento);
                reservaListReserva = em.merge(reservaListReserva);
                if (oldRegistroTurismoOfReservaListReserva != null) {
                    oldRegistroTurismoOfReservaListReserva.getReservaList().remove(reservaListReserva);
                    oldRegistroTurismoOfReservaListReserva = em.merge(oldRegistroTurismoOfReservaListReserva);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlojamiento(alojamiento.getRegistroTurismo()) != null) {
                throw new PreexistingEntityException("Alojamiento " + alojamiento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alojamiento alojamiento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alojamiento persistentAlojamiento = em.find(Alojamiento.class, alojamiento.getRegistroTurismo());
            Usuario nifOld = persistentAlojamiento.getNif();
            Usuario nifNew = alojamiento.getNif();
            Puebloszona puebloszonaOld = persistentAlojamiento.getPuebloszona();
            Puebloszona puebloszonaNew = alojamiento.getPuebloszona();
            List<Servicio> servicioListOld = persistentAlojamiento.getServicioList();
            List<Servicio> servicioListNew = alojamiento.getServicioList();
            List<Imagen> imagenListOld = persistentAlojamiento.getImagenList();
            List<Imagen> imagenListNew = alojamiento.getImagenList();
            List<Condicionesalojamiento> condicionesalojamientoListOld = persistentAlojamiento.getCondicionesalojamientoList();
            List<Condicionesalojamiento> condicionesalojamientoListNew = alojamiento.getCondicionesalojamientoList();
            List<Ocupacion> ocupacionListOld = persistentAlojamiento.getOcupacionList();
            List<Ocupacion> ocupacionListNew = alojamiento.getOcupacionList();
            List<Actividad> actividadListOld = persistentAlojamiento.getActividadList();
            List<Actividad> actividadListNew = alojamiento.getActividadList();
            List<Opinion> opinionListOld = persistentAlojamiento.getOpinionList();
            List<Opinion> opinionListNew = alojamiento.getOpinionList();
            List<Favoritos> favoritosListOld = persistentAlojamiento.getFavoritosList();
            List<Favoritos> favoritosListNew = alojamiento.getFavoritosList();
            List<Reserva> reservaListOld = persistentAlojamiento.getReservaList();
            List<Reserva> reservaListNew = alojamiento.getReservaList();
            List<String> illegalOrphanMessages = null;
            if (puebloszonaOld != null && !puebloszonaOld.equals(puebloszonaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Puebloszona " + puebloszonaOld + " since its registroTurismo field is not nullable.");
            }
            for (Servicio servicioListOldServicio : servicioListOld) {
                if (!servicioListNew.contains(servicioListOldServicio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Servicio " + servicioListOldServicio + " since its registroTurismo field is not nullable.");
                }
            }
            for (Imagen imagenListOldImagen : imagenListOld) {
                if (!imagenListNew.contains(imagenListOldImagen)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Imagen " + imagenListOldImagen + " since its registroTurismo field is not nullable.");
                }
            }
            for (Condicionesalojamiento condicionesalojamientoListOldCondicionesalojamiento : condicionesalojamientoListOld) {
                if (!condicionesalojamientoListNew.contains(condicionesalojamientoListOldCondicionesalojamiento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Condicionesalojamiento " + condicionesalojamientoListOldCondicionesalojamiento + " since its registroTurismo field is not nullable.");
                }
            }
            for (Ocupacion ocupacionListOldOcupacion : ocupacionListOld) {
                if (!ocupacionListNew.contains(ocupacionListOldOcupacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ocupacion " + ocupacionListOldOcupacion + " since its alojamiento field is not nullable.");
                }
            }
            for (Actividad actividadListOldActividad : actividadListOld) {
                if (!actividadListNew.contains(actividadListOldActividad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Actividad " + actividadListOldActividad + " since its registroTurismo field is not nullable.");
                }
            }
            for (Opinion opinionListOldOpinion : opinionListOld) {
                if (!opinionListNew.contains(opinionListOldOpinion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Opinion " + opinionListOldOpinion + " since its registroTurismo field is not nullable.");
                }
            }
            for (Favoritos favoritosListOldFavoritos : favoritosListOld) {
                if (!favoritosListNew.contains(favoritosListOldFavoritos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Favoritos " + favoritosListOldFavoritos + " since its registroTurismo field is not nullable.");
                }
            }
            for (Reserva reservaListOldReserva : reservaListOld) {
                if (!reservaListNew.contains(reservaListOldReserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reserva " + reservaListOldReserva + " since its registroTurismo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (nifNew != null) {
                nifNew = em.getReference(nifNew.getClass(), nifNew.getNif());
                alojamiento.setNif(nifNew);
            }
            if (puebloszonaNew != null) {
                puebloszonaNew = em.getReference(puebloszonaNew.getClass(), puebloszonaNew.getCodigoPueblosZona());
                alojamiento.setPuebloszona(puebloszonaNew);
            }
            List<Servicio> attachedServicioListNew = new ArrayList<Servicio>();
            for (Servicio servicioListNewServicioToAttach : servicioListNew) {
                servicioListNewServicioToAttach = em.getReference(servicioListNewServicioToAttach.getClass(), servicioListNewServicioToAttach.getCodS());
                attachedServicioListNew.add(servicioListNewServicioToAttach);
            }
            servicioListNew = attachedServicioListNew;
            alojamiento.setServicioList(servicioListNew);
            List<Imagen> attachedImagenListNew = new ArrayList<Imagen>();
            for (Imagen imagenListNewImagenToAttach : imagenListNew) {
                imagenListNewImagenToAttach = em.getReference(imagenListNewImagenToAttach.getClass(), imagenListNewImagenToAttach.getCodImagen());
                attachedImagenListNew.add(imagenListNewImagenToAttach);
            }
            imagenListNew = attachedImagenListNew;
            alojamiento.setImagenList(imagenListNew);
            List<Condicionesalojamiento> attachedCondicionesalojamientoListNew = new ArrayList<Condicionesalojamiento>();
            for (Condicionesalojamiento condicionesalojamientoListNewCondicionesalojamientoToAttach : condicionesalojamientoListNew) {
                condicionesalojamientoListNewCondicionesalojamientoToAttach = em.getReference(condicionesalojamientoListNewCondicionesalojamientoToAttach.getClass(), condicionesalojamientoListNewCondicionesalojamientoToAttach.getCodigoCond());
                attachedCondicionesalojamientoListNew.add(condicionesalojamientoListNewCondicionesalojamientoToAttach);
            }
            condicionesalojamientoListNew = attachedCondicionesalojamientoListNew;
            alojamiento.setCondicionesalojamientoList(condicionesalojamientoListNew);
            List<Ocupacion> attachedOcupacionListNew = new ArrayList<Ocupacion>();
            for (Ocupacion ocupacionListNewOcupacionToAttach : ocupacionListNew) {
                ocupacionListNewOcupacionToAttach = em.getReference(ocupacionListNewOcupacionToAttach.getClass(), ocupacionListNewOcupacionToAttach.getOcupacionPK());
                attachedOcupacionListNew.add(ocupacionListNewOcupacionToAttach);
            }
            ocupacionListNew = attachedOcupacionListNew;
            alojamiento.setOcupacionList(ocupacionListNew);
            List<Actividad> attachedActividadListNew = new ArrayList<Actividad>();
            for (Actividad actividadListNewActividadToAttach : actividadListNew) {
                actividadListNewActividadToAttach = em.getReference(actividadListNewActividadToAttach.getClass(), actividadListNewActividadToAttach.getCodA());
                attachedActividadListNew.add(actividadListNewActividadToAttach);
            }
            actividadListNew = attachedActividadListNew;
            alojamiento.setActividadList(actividadListNew);
            List<Opinion> attachedOpinionListNew = new ArrayList<Opinion>();
            for (Opinion opinionListNewOpinionToAttach : opinionListNew) {
                opinionListNewOpinionToAttach = em.getReference(opinionListNewOpinionToAttach.getClass(), opinionListNewOpinionToAttach.getCodigoOpinion());
                attachedOpinionListNew.add(opinionListNewOpinionToAttach);
            }
            opinionListNew = attachedOpinionListNew;
            alojamiento.setOpinionList(opinionListNew);
            List<Favoritos> attachedFavoritosListNew = new ArrayList<Favoritos>();
            for (Favoritos favoritosListNewFavoritosToAttach : favoritosListNew) {
                favoritosListNewFavoritosToAttach = em.getReference(favoritosListNewFavoritosToAttach.getClass(), favoritosListNewFavoritosToAttach.getCodigoFav());
                attachedFavoritosListNew.add(favoritosListNewFavoritosToAttach);
            }
            favoritosListNew = attachedFavoritosListNew;
            alojamiento.setFavoritosList(favoritosListNew);
            List<Reserva> attachedReservaListNew = new ArrayList<Reserva>();
            for (Reserva reservaListNewReservaToAttach : reservaListNew) {
                reservaListNewReservaToAttach = em.getReference(reservaListNewReservaToAttach.getClass(), reservaListNewReservaToAttach.getCodigoReserva());
                attachedReservaListNew.add(reservaListNewReservaToAttach);
            }
            reservaListNew = attachedReservaListNew;
            alojamiento.setReservaList(reservaListNew);
            alojamiento = em.merge(alojamiento);
            if (nifOld != null && !nifOld.equals(nifNew)) {
                nifOld.getAlojamientoList().remove(alojamiento);
                nifOld = em.merge(nifOld);
            }
            if (nifNew != null && !nifNew.equals(nifOld)) {
                nifNew.getAlojamientoList().add(alojamiento);
                nifNew = em.merge(nifNew);
            }
            if (puebloszonaNew != null && !puebloszonaNew.equals(puebloszonaOld)) {
                Alojamiento oldRegistroTurismoOfPuebloszona = puebloszonaNew.getRegistroTurismo();
                if (oldRegistroTurismoOfPuebloszona != null) {
                    oldRegistroTurismoOfPuebloszona.setPuebloszona(null);
                    oldRegistroTurismoOfPuebloszona = em.merge(oldRegistroTurismoOfPuebloszona);
                }
                puebloszonaNew.setRegistroTurismo(alojamiento);
                puebloszonaNew = em.merge(puebloszonaNew);
            }
            for (Servicio servicioListNewServicio : servicioListNew) {
                if (!servicioListOld.contains(servicioListNewServicio)) {
                    Alojamiento oldRegistroTurismoOfServicioListNewServicio = servicioListNewServicio.getRegistroTurismo();
                    servicioListNewServicio.setRegistroTurismo(alojamiento);
                    servicioListNewServicio = em.merge(servicioListNewServicio);
                    if (oldRegistroTurismoOfServicioListNewServicio != null && !oldRegistroTurismoOfServicioListNewServicio.equals(alojamiento)) {
                        oldRegistroTurismoOfServicioListNewServicio.getServicioList().remove(servicioListNewServicio);
                        oldRegistroTurismoOfServicioListNewServicio = em.merge(oldRegistroTurismoOfServicioListNewServicio);
                    }
                }
            }
            for (Imagen imagenListNewImagen : imagenListNew) {
                if (!imagenListOld.contains(imagenListNewImagen)) {
                    Alojamiento oldRegistroTurismoOfImagenListNewImagen = imagenListNewImagen.getRegistroTurismo();
                    imagenListNewImagen.setRegistroTurismo(alojamiento);
                    imagenListNewImagen = em.merge(imagenListNewImagen);
                    if (oldRegistroTurismoOfImagenListNewImagen != null && !oldRegistroTurismoOfImagenListNewImagen.equals(alojamiento)) {
                        oldRegistroTurismoOfImagenListNewImagen.getImagenList().remove(imagenListNewImagen);
                        oldRegistroTurismoOfImagenListNewImagen = em.merge(oldRegistroTurismoOfImagenListNewImagen);
                    }
                }
            }
            for (Condicionesalojamiento condicionesalojamientoListNewCondicionesalojamiento : condicionesalojamientoListNew) {
                if (!condicionesalojamientoListOld.contains(condicionesalojamientoListNewCondicionesalojamiento)) {
                    Alojamiento oldRegistroTurismoOfCondicionesalojamientoListNewCondicionesalojamiento = condicionesalojamientoListNewCondicionesalojamiento.getRegistroTurismo();
                    condicionesalojamientoListNewCondicionesalojamiento.setRegistroTurismo(alojamiento);
                    condicionesalojamientoListNewCondicionesalojamiento = em.merge(condicionesalojamientoListNewCondicionesalojamiento);
                    if (oldRegistroTurismoOfCondicionesalojamientoListNewCondicionesalojamiento != null && !oldRegistroTurismoOfCondicionesalojamientoListNewCondicionesalojamiento.equals(alojamiento)) {
                        oldRegistroTurismoOfCondicionesalojamientoListNewCondicionesalojamiento.getCondicionesalojamientoList().remove(condicionesalojamientoListNewCondicionesalojamiento);
                        oldRegistroTurismoOfCondicionesalojamientoListNewCondicionesalojamiento = em.merge(oldRegistroTurismoOfCondicionesalojamientoListNewCondicionesalojamiento);
                    }
                }
            }
            for (Ocupacion ocupacionListNewOcupacion : ocupacionListNew) {
                if (!ocupacionListOld.contains(ocupacionListNewOcupacion)) {
                    Alojamiento oldAlojamientoOfOcupacionListNewOcupacion = ocupacionListNewOcupacion.getAlojamiento();
                    ocupacionListNewOcupacion.setAlojamiento(alojamiento);
                    ocupacionListNewOcupacion = em.merge(ocupacionListNewOcupacion);
                    if (oldAlojamientoOfOcupacionListNewOcupacion != null && !oldAlojamientoOfOcupacionListNewOcupacion.equals(alojamiento)) {
                        oldAlojamientoOfOcupacionListNewOcupacion.getOcupacionList().remove(ocupacionListNewOcupacion);
                        oldAlojamientoOfOcupacionListNewOcupacion = em.merge(oldAlojamientoOfOcupacionListNewOcupacion);
                    }
                }
            }
            for (Actividad actividadListNewActividad : actividadListNew) {
                if (!actividadListOld.contains(actividadListNewActividad)) {
                    Alojamiento oldRegistroTurismoOfActividadListNewActividad = actividadListNewActividad.getRegistroTurismo();
                    actividadListNewActividad.setRegistroTurismo(alojamiento);
                    actividadListNewActividad = em.merge(actividadListNewActividad);
                    if (oldRegistroTurismoOfActividadListNewActividad != null && !oldRegistroTurismoOfActividadListNewActividad.equals(alojamiento)) {
                        oldRegistroTurismoOfActividadListNewActividad.getActividadList().remove(actividadListNewActividad);
                        oldRegistroTurismoOfActividadListNewActividad = em.merge(oldRegistroTurismoOfActividadListNewActividad);
                    }
                }
            }
            for (Opinion opinionListNewOpinion : opinionListNew) {
                if (!opinionListOld.contains(opinionListNewOpinion)) {
                    Alojamiento oldRegistroTurismoOfOpinionListNewOpinion = opinionListNewOpinion.getRegistroTurismo();
                    opinionListNewOpinion.setRegistroTurismo(alojamiento);
                    opinionListNewOpinion = em.merge(opinionListNewOpinion);
                    if (oldRegistroTurismoOfOpinionListNewOpinion != null && !oldRegistroTurismoOfOpinionListNewOpinion.equals(alojamiento)) {
                        oldRegistroTurismoOfOpinionListNewOpinion.getOpinionList().remove(opinionListNewOpinion);
                        oldRegistroTurismoOfOpinionListNewOpinion = em.merge(oldRegistroTurismoOfOpinionListNewOpinion);
                    }
                }
            }
            for (Favoritos favoritosListNewFavoritos : favoritosListNew) {
                if (!favoritosListOld.contains(favoritosListNewFavoritos)) {
                    Alojamiento oldRegistroTurismoOfFavoritosListNewFavoritos = favoritosListNewFavoritos.getRegistroTurismo();
                    favoritosListNewFavoritos.setRegistroTurismo(alojamiento);
                    favoritosListNewFavoritos = em.merge(favoritosListNewFavoritos);
                    if (oldRegistroTurismoOfFavoritosListNewFavoritos != null && !oldRegistroTurismoOfFavoritosListNewFavoritos.equals(alojamiento)) {
                        oldRegistroTurismoOfFavoritosListNewFavoritos.getFavoritosList().remove(favoritosListNewFavoritos);
                        oldRegistroTurismoOfFavoritosListNewFavoritos = em.merge(oldRegistroTurismoOfFavoritosListNewFavoritos);
                    }
                }
            }
            for (Reserva reservaListNewReserva : reservaListNew) {
                if (!reservaListOld.contains(reservaListNewReserva)) {
                    Alojamiento oldRegistroTurismoOfReservaListNewReserva = reservaListNewReserva.getRegistroTurismo();
                    reservaListNewReserva.setRegistroTurismo(alojamiento);
                    reservaListNewReserva = em.merge(reservaListNewReserva);
                    if (oldRegistroTurismoOfReservaListNewReserva != null && !oldRegistroTurismoOfReservaListNewReserva.equals(alojamiento)) {
                        oldRegistroTurismoOfReservaListNewReserva.getReservaList().remove(reservaListNewReserva);
                        oldRegistroTurismoOfReservaListNewReserva = em.merge(oldRegistroTurismoOfReservaListNewReserva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = alojamiento.getRegistroTurismo();
                if (findAlojamiento(id) == null) {
                    throw new NonexistentEntityException("The alojamiento with id " + id + " no longer exists.");
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
            Alojamiento alojamiento;
            try {
                alojamiento = em.getReference(Alojamiento.class, id);
                alojamiento.getRegistroTurismo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alojamiento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Puebloszona puebloszonaOrphanCheck = alojamiento.getPuebloszona();
            if (puebloszonaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alojamiento (" + alojamiento + ") cannot be destroyed since the Puebloszona " + puebloszonaOrphanCheck + " in its puebloszona field has a non-nullable registroTurismo field.");
            }
            List<Servicio> servicioListOrphanCheck = alojamiento.getServicioList();
            for (Servicio servicioListOrphanCheckServicio : servicioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alojamiento (" + alojamiento + ") cannot be destroyed since the Servicio " + servicioListOrphanCheckServicio + " in its servicioList field has a non-nullable registroTurismo field.");
            }
            List<Imagen> imagenListOrphanCheck = alojamiento.getImagenList();
            for (Imagen imagenListOrphanCheckImagen : imagenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alojamiento (" + alojamiento + ") cannot be destroyed since the Imagen " + imagenListOrphanCheckImagen + " in its imagenList field has a non-nullable registroTurismo field.");
            }
            List<Condicionesalojamiento> condicionesalojamientoListOrphanCheck = alojamiento.getCondicionesalojamientoList();
            for (Condicionesalojamiento condicionesalojamientoListOrphanCheckCondicionesalojamiento : condicionesalojamientoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alojamiento (" + alojamiento + ") cannot be destroyed since the Condicionesalojamiento " + condicionesalojamientoListOrphanCheckCondicionesalojamiento + " in its condicionesalojamientoList field has a non-nullable registroTurismo field.");
            }
            List<Ocupacion> ocupacionListOrphanCheck = alojamiento.getOcupacionList();
            for (Ocupacion ocupacionListOrphanCheckOcupacion : ocupacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alojamiento (" + alojamiento + ") cannot be destroyed since the Ocupacion " + ocupacionListOrphanCheckOcupacion + " in its ocupacionList field has a non-nullable alojamiento field.");
            }
            List<Actividad> actividadListOrphanCheck = alojamiento.getActividadList();
            for (Actividad actividadListOrphanCheckActividad : actividadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alojamiento (" + alojamiento + ") cannot be destroyed since the Actividad " + actividadListOrphanCheckActividad + " in its actividadList field has a non-nullable registroTurismo field.");
            }
            List<Opinion> opinionListOrphanCheck = alojamiento.getOpinionList();
            for (Opinion opinionListOrphanCheckOpinion : opinionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alojamiento (" + alojamiento + ") cannot be destroyed since the Opinion " + opinionListOrphanCheckOpinion + " in its opinionList field has a non-nullable registroTurismo field.");
            }
            List<Favoritos> favoritosListOrphanCheck = alojamiento.getFavoritosList();
            for (Favoritos favoritosListOrphanCheckFavoritos : favoritosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alojamiento (" + alojamiento + ") cannot be destroyed since the Favoritos " + favoritosListOrphanCheckFavoritos + " in its favoritosList field has a non-nullable registroTurismo field.");
            }
            List<Reserva> reservaListOrphanCheck = alojamiento.getReservaList();
            for (Reserva reservaListOrphanCheckReserva : reservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alojamiento (" + alojamiento + ") cannot be destroyed since the Reserva " + reservaListOrphanCheckReserva + " in its reservaList field has a non-nullable registroTurismo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario nif = alojamiento.getNif();
            if (nif != null) {
                nif.getAlojamientoList().remove(alojamiento);
                nif = em.merge(nif);
            }
            em.remove(alojamiento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alojamiento> findAlojamientoEntities() {
        return findAlojamientoEntities(true, -1, -1);
    }

    public List<Alojamiento> findAlojamientoEntities(int maxResults, int firstResult) {
        return findAlojamientoEntities(false, maxResults, firstResult);
    }

    private List<Alojamiento> findAlojamientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alojamiento.class));
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

    public Alojamiento findAlojamiento(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alojamiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlojamientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alojamiento> rt = cq.from(Alojamiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Alojamiento> findByNif(Usuario usu) {
        EntityManager em = getEntityManager();
            TypedQuery q = em.createNamedQuery("Alojamiento.findByNif", Alojamiento.class);
            q.setParameter("nif", usu);
            List <Alojamiento> listadoAlojamientos = q.getResultList();
            return listadoAlojamientos;
    }
    
    public List<Alojamiento> findByPrecio() {
        EntityManager em = getEntityManager();
            TypedQuery q = em.createNamedQuery("Alojamiento.findByPrecioNoche", Alojamiento.class);
            List <Alojamiento> listadoAlojamientos = q.getResultList();
            return listadoAlojamientos;
    }
    
    public List<Alojamiento> findByPlazasMax() {
        EntityManager em = getEntityManager();
            TypedQuery q = em.createNamedQuery("Alojamiento.findByPlazasMax", Alojamiento.class);
            List <Alojamiento> listadoAlojamientos = q.getResultList();
            return listadoAlojamientos;
    }
    
    public List<Alojamiento> findResultadosBuscador(int codigoPoblacion) {
        EntityManager em = getEntityManager();
            TypedQuery q = em.createNamedQuery("Alojamiento.findByCodigoPoblacion", Alojamiento.class);
            q.setParameter("codigoPoblacion", codigoPoblacion);
            List <Alojamiento> listadoAlojamientos = q.getResultList();
            return listadoAlojamientos;
    }
    
}
