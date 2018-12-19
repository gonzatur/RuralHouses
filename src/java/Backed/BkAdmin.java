/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backed;

import DAO.ActividadesJpaController;
import DAO.OpinionJpaController;
import DAO.ServiciosJpaController;
import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import DTO.Actividades;
import DTO.Opinion;
import DTO.Servicios;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author gonza
 */
public class BkAdmin {

    private EntityManagerFactory emf;
    private ActividadesJpaController controlActividades;
    private ServiciosJpaController controlServicios;
    private OpinionJpaController controlOpiniones;
    private ArrayList listadoActividades = null;
    private ArrayList listadoServicios = null;
    private ArrayList listadoOpinionesDen = null;
    private String actividad;
    private String servicio;
    private String actividadCrear;
    private String servicioCrear;
    private Opinion opinionEscogida;
    private int codigoOpinion;
    private boolean renderOpinion = false;

    public BkAdmin() {
        emf = Persistence.createEntityManagerFactory("RuralHousesPU");
        controlActividades = new ActividadesJpaController(emf);
        controlServicios = new ServiciosJpaController(emf);
        controlOpiniones = new OpinionJpaController(emf);
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public OpinionJpaController getControlOpiniones() {
        return controlOpiniones;
    }

    public void setControlOpiniones(OpinionJpaController controlOpiniones) {
        this.controlOpiniones = controlOpiniones;
    }

    public ActividadesJpaController getControlActividades() {
        return controlActividades;
    }

    public void setControlActividades(ActividadesJpaController controlActividades) {
        this.controlActividades = controlActividades;
    }

    public ServiciosJpaController getControlServicios() {
        return controlServicios;
    }

    public void setControlServicios(ServiciosJpaController controlServicios) {
        this.controlServicios = controlServicios;
    }

    public ArrayList getListadoActividades() {
        return listadoActividades;
    }

    public void setListadoActividades(ArrayList listadoActividades) {
        this.listadoActividades = listadoActividades;
    }

    public ArrayList getListadoServicios() {
        return listadoServicios;
    }

    public void setListadoServicios(ArrayList listadoServicios) {
        this.listadoServicios = listadoServicios;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getActividadCrear() {
        return actividadCrear;
    }

    public void setActividadCrear(String actividadCrear) {
        this.actividadCrear = actividadCrear;
    }

    public String getServicioCrear() {
        return servicioCrear;
    }

    public void setServicioCrear(String servicioCrear) {
        this.servicioCrear = servicioCrear;
    }

    public ArrayList getListadoOpinionesDen() {
        listadoOpinionesDen();
        return listadoOpinionesDen;
    }

    public void setListadoOpinionesDen(ArrayList listadoOpinionesDen) {
        this.listadoOpinionesDen = listadoOpinionesDen;
    }

    public Opinion getOpinionEscogida() {
        return opinionEscogida;
    }

    public void setOpinionEscogida(Opinion opinionEscogida) {
        this.opinionEscogida = opinionEscogida;
    }

    public int getCodigoOpinion() {
        return codigoOpinion;
    }

    public void setCodigoOpinion(int codigoOpinion) {
        this.codigoOpinion = codigoOpinion;
    }

    public boolean isRenderOpinion() {
        return renderOpinion;
    }

    public void setRenderOpinion(boolean renderOpinion) {
        this.renderOpinion = renderOpinion;
    }

    //METODOS PARA OBTENER LISTADO DE ACTIVIDADES Y SERVICIOS
    public ArrayList getListaActividades() {
        listadoActividades = null;
        if (listadoActividades == null) {
            listadoActividades = new ArrayList();
            List<Actividades> listaAct = controlActividades.findActividadesEntities();
            for (Actividades act : listaAct) {
                listadoActividades.add(new SelectItem(act.getCodigoActividad(), act.getNombreActividad()));
            }
        }
        return listadoActividades;
    }

    public ArrayList getListaServicios() {
        listadoServicios = null;
        if (listadoServicios == null) {
            listadoServicios = new ArrayList();
            List<Servicios> listaServ = controlServicios.findServiciosEntities();
            for (Servicios serv : listaServ) {
                listadoServicios.add(new SelectItem(serv.getCodigoServicio(), serv.getNombreServicio()));
            }
        }
        return listadoServicios;
    }

    //METODOS PARA BORRAR ACTIVIDADES Y SERVICIOS
    public void borraActividad() {
        try {
            int codigoBorraAct = Integer.parseInt(actividad);
            controlActividades.destroy(codigoBorraAct);
            getListaActividades();
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            Logger.getLogger(BkAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void borraServicio() {
        try {
            int codigoBorraServ = Integer.parseInt(servicio);
            controlServicios.destroy(codigoBorraServ);
            getListaServicios();
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            Logger.getLogger(BkAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //METODO PARA CREAR ACTIVIDADES O SERVICIOS DEPENDIENDO LA ENTRADA
    public void creaActServ() {
        if (actividadCrear != null) {
            Actividades actCrear = new Actividades(null, actividadCrear);
            controlActividades.create(actCrear);
            getListaActividades();
        }

        if (servicioCrear != null) {
            Servicios servCrear = new Servicios(null, servicioCrear);
            controlServicios.create(servCrear);
            getListaServicios();
        }
    }

    //METODO PARA OBTENER TODAS LAS OPINIONES QUE HAN SIDO DENUNCIADAS
    public ArrayList listadoOpinionesDen() {
        listadoOpinionesDen = null;
        if (listadoOpinionesDen == null) {
            listadoOpinionesDen = new ArrayList();
            List<Opinion> listaOpin = controlOpiniones.findByDen(1);
            for (Opinion opi : listaOpin) {
                listadoOpinionesDen.add(opi);
            }
        }
        return listadoOpinionesDen;
    }

    public Opinion mostrarOpinion(int codigo) {
        opinionEscogida = controlOpiniones.findOpinion(codigo);

        return opinionEscogida;
    }

    public void borrarOpinion() {

        try {
            int codigoOpiBorrar = opinionEscogida.getCodigoOpinion();
            controlOpiniones.destroy(codigoOpiBorrar);
            listadoOpinionesDen();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(BkAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
