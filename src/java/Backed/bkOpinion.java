/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backed;

import DAO.AlojamientoJpaController;
import DAO.OpinionJpaController;
import DTO.Alojamiento;
import DTO.Opinion;
import DTO.Usuario;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gonza
 */
public class bkOpinion {

    private EntityManagerFactory emf;
    private Alojamiento alojamiento;
    private AlojamientoJpaController controlAlojamientos;
    private OpinionJpaController controlOpinion;
    private Usuario propietario;
    private Usuario viajero;
    private ArrayList listadoAlojamientos;
    private ArrayList listadoOpinionesAlojamiento = null;
    private ArrayList listadoOpinionesUsuario = null;
    private HtmlInputHidden opinionDenunciada;

    public bkOpinion() {
        emf = Persistence.createEntityManagerFactory("RuralHousesPU");
        controlAlojamientos = new AlojamientoJpaController(emf);
        controlOpinion = new OpinionJpaController(emf);
    }

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public AlojamientoJpaController getControlAlojamientos() {
        return controlAlojamientos;
    }

    public void setControlAlojamientos(AlojamientoJpaController controlAlojamientos) {
        this.controlAlojamientos = controlAlojamientos;
    }

    public OpinionJpaController getControlOpinion() {
        return controlOpinion;
    }

    public void setControlOpinion(OpinionJpaController controlOpinion) {
        this.controlOpinion = controlOpinion;
    }

    public Usuario getPropietario() {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        BkManageBeanSesion BkManageBeanSesion = new BkManageBeanSesion();

        HttpSession session = (HttpSession) ctx.getSession(false);
        BkManageBeanSesion = (BkManageBeanSesion) session.getAttribute("BkManageBeanSesion");
        propietario = (Usuario) BkManageBeanSesion.getUsuarioLog();
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    public Usuario getViajero() {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        BkManageBeanSesion BkManageBeanSesion = new BkManageBeanSesion();

        HttpSession session = (HttpSession) ctx.getSession(false);
        BkManageBeanSesion = (BkManageBeanSesion) session.getAttribute("BkManageBeanSesion");
        viajero = (Usuario) BkManageBeanSesion.getUsuarioLog();
        return viajero;
    }

    public void setViajero(Usuario viajero) {
        this.viajero = viajero;
    }

    public ArrayList getListadoAlojamientos() {
        listaAlojamientosPropietario();
        return listadoAlojamientos;
    }

    public void setListadoAlojamientos(ArrayList listadoAlojamientos) {
        this.listadoAlojamientos = listadoAlojamientos;
    }

    public ArrayList getListadoOpinionesAlojamiento() {
        return listadoOpinionesAlojamiento;
    }

    public void setListadoOpinionesAlojamiento(ArrayList listadoOpinionesAlojamiento) {
        this.listadoOpinionesAlojamiento = listadoOpinionesAlojamiento;
    }

    public HtmlInputHidden getOpinionDenunciada() {
        return opinionDenunciada;
    }

    public void setOpinionDenunciada(HtmlInputHidden opinionDenunciada) {
        this.opinionDenunciada = opinionDenunciada;
    }

    public ArrayList getListadoOpinionesUsuario() {
        listadoOpinionesDelUsu();
        return listadoOpinionesUsuario;
    }

    public void setListadoOpinionesUsuario(ArrayList listadoOpinionesUsuario) {
        this.listadoOpinionesUsuario = listadoOpinionesUsuario;
    }

    //METODOS----------------------------------------------------------------------------------------
    public ArrayList listaAlojamientosPropietario() {
        if (listadoAlojamientos == null) {
            listadoAlojamientos = new ArrayList();
            List<Alojamiento> listaAloj = controlAlojamientos.findByNif(propietario);
            for (Alojamiento aloj : listaAloj) {
                listadoAlojamientos.add(aloj);
            }
            if (listadoAlojamientos.size() > 0) {
                inicializarAlojamiento();
            }
        }
        return listadoAlojamientos;
    }

    public void inicializarAlojamiento() {
        alojamiento = propietario.getAlojamientoList().get(0);

        restaurarOpiniones();
    }

    public void cambiarAlojamiento() {
        String codAlojamiento = alojamiento.getRegistroTurismo();
        alojamiento = controlAlojamientos.findAlojamiento(codAlojamiento);

        restaurarOpiniones();

    }

    public void restaurarOpiniones() {
        List<Opinion> listaOpinAloj = controlOpinion.findByAlojamiento(alojamiento);
        listadoOpinionesAlojamiento = new ArrayList();
        for (Opinion op : listaOpinAloj) {
            listadoOpinionesAlojamiento.add(op);
        }
    }

    public void denunciarOpinion() {
        try {
            int codigoOpinion = Integer.parseInt(opinionDenunciada.getValue().toString());
            Opinion op = controlOpinion.findOpinion(codigoOpinion);
            op.setDenuncia(1);
            controlOpinion.edit(op);
            listadoOpinionesAlojamiento = null;

            restaurarOpiniones();
        } catch (Exception ex) {
            Logger.getLogger(bkOpinion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listadoOpinionesDelUsu() {
        if (listadoOpinionesUsuario == null) {
            listadoOpinionesUsuario = new ArrayList();
            List<Opinion> listadoOpi = controlOpinion.findByNif(viajero);
            for (Opinion opi : listadoOpi) {
                listadoOpinionesUsuario.add(opi);
            }
        }
    }

    public String puntuacionMediaAlojStr(String registroTurismo) {
        Alojamiento aloj = controlAlojamientos.findAlojamiento(registroTurismo);
        List<Opinion> listadoOpiniones = aloj.getOpinionList();
        float sumatorio = 0;
        String retorno = "";
        for (Opinion op : listadoOpiniones) {
            sumatorio += op.getPuntuacionMedia();
        }

        if (sumatorio > 0) {

            sumatorio = sumatorio / listadoOpiniones.size();
            DecimalFormat df = new DecimalFormat("#.#");
            System.out.println("kilobytes (DecimalFormat) : " + df.format(sumatorio));
            retorno = df.format(sumatorio);
        } else {

        }
        return retorno;
    }

    public int puntuacionLimpieza(String registroTurismo) {
        Alojamiento aloj = controlAlojamientos.findAlojamiento(registroTurismo);
        List<Opinion> listadoOpiniones = aloj.getOpinionList();
        double sumatorio = 0;
        for (Opinion op : listadoOpiniones) {
            sumatorio += op.getLimpieza();
        }
        int notaDevolver = 0;

        sumatorio = sumatorio / listadoOpiniones.size();
        Double calculo = Math.ceil(sumatorio);
        notaDevolver = calculo.intValue()*10;

        return notaDevolver;
    }
    
    public int puntuacionConfort(String registroTurismo) {
        Alojamiento aloj = controlAlojamientos.findAlojamiento(registroTurismo);
        List<Opinion> listadoOpiniones = aloj.getOpinionList();
        double sumatorio = 0;
        for (Opinion op : listadoOpiniones) {
            sumatorio += op.getConfort();
        }
        int notaDevolver = 0;

        sumatorio = sumatorio / listadoOpiniones.size();
        Double calculo = Math.ceil(sumatorio);
        notaDevolver = calculo.intValue()*10;

        return notaDevolver;
    }
    
    public int puntuacionUbicacion(String registroTurismo) {
        Alojamiento aloj = controlAlojamientos.findAlojamiento(registroTurismo);
        List<Opinion> listadoOpiniones = aloj.getOpinionList();
        double sumatorio = 0;
        for (Opinion op : listadoOpiniones) {
            sumatorio += op.getUbicacion();
        }
        int notaDevolver = 0;

        sumatorio = sumatorio / listadoOpiniones.size();
        Double calculo = Math.ceil(sumatorio);
        notaDevolver = calculo.intValue()*10;

        return notaDevolver;
    }
    
    public int puntuacionCalPre(String registroTurismo) {
        Alojamiento aloj = controlAlojamientos.findAlojamiento(registroTurismo);
        List<Opinion> listadoOpiniones = aloj.getOpinionList();
        double sumatorio = 0;
        for (Opinion op : listadoOpiniones) {
            sumatorio += op.getRelacionCP();
        }
        int notaDevolver = 0;

        sumatorio = sumatorio / listadoOpiniones.size();
        Double calculo = Math.ceil(sumatorio);
        notaDevolver = calculo.intValue()*10;

        return notaDevolver;
    }
    
    public int puntuacionTrato(String registroTurismo) {
        Alojamiento aloj = controlAlojamientos.findAlojamiento(registroTurismo);
        List<Opinion> listadoOpiniones = aloj.getOpinionList();
        double sumatorio = 0;
        for (Opinion op : listadoOpiniones) {
            sumatorio += op.getTratoRecibido();
        }
        int notaDevolver = 0;

        sumatorio = sumatorio / listadoOpiniones.size();
        Double calculo = Math.ceil(sumatorio);
        notaDevolver = calculo.intValue()*10;

        return notaDevolver;
    }

}
