/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backed;

import DAO.ActividadJpaController;
import DAO.ActividadesJpaController;
import DAO.AlojamientoJpaController;
import DAO.CondicionesalojamientoJpaController;
import DAO.ImagenJpaController;
import DAO.PoblacionJpaController;
import DAO.ProvinciaJpaController;
import DAO.ServicioJpaController;
import DAO.ServiciosJpaController;
import DAO.UsuarioJpaController;
import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import DTO.Actividad;
import DTO.Actividades;
import DTO.Alojamiento;
import DTO.Condicionesalojamiento;
import DTO.Imagen;
import DTO.Opinion;
import DTO.Poblacion;
import DTO.Servicio;
import DTO.Servicios;
import DTO.Usuario;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author gonza
 */
public class bkOwner implements Serializable {

    private EntityManagerFactory emf;
    private AlojamientoJpaController controlAlojamientos;
    private PoblacionJpaController controlPoblaciones;
    private ProvinciaJpaController controlProvincias;
    private UsuarioJpaController controlUsuarios;
    private CondicionesalojamientoJpaController controlCondicionesAloj;
    private ActividadesJpaController controlActividades;
    private ActividadJpaController controlActividad;
    private ServiciosJpaController controlServicios;
    private ServicioJpaController controlServicio;
    private ImagenJpaController controlImagen;
    private Usuario propietario;
    private ArrayList listadoAlojamientos;
    private Alojamiento alojamiento;
    private List<Condicionesalojamiento> condiciones;
    private Condicionesalojamiento condicionesAlojamiento;
    private Poblacion poblacion;
    private String provincia;
    private int posicionTab;
    private ArrayList listadoHorarioEntrada = null;
    private ArrayList listadoHorarioSalida = null;
    private ArrayList listadoActividades = null;
    private ArrayList listadoServicios = null;
    private ArrayList listadoFotosAlojamiento = null;
    private String anticipo;
    private String cancelacion;
    private String actividadesElegidas[];
    private String[] serviciosElegidos;
    private HtmlInputHidden fotoEliminar;

    //NUEVO ALOJAMIENTO
    private String nombreAlojamiento;
    private String registroTurismo;
    private int city;

    //COMPROBAR ALOJAMIENTO VACIO
    private boolean vacio;

    public bkOwner() {
        emf = Persistence.createEntityManagerFactory("RuralHousesPU");
        controlAlojamientos = new AlojamientoJpaController(emf);
        controlPoblaciones = new PoblacionJpaController(emf);
        controlProvincias = new ProvinciaJpaController(emf);
        controlUsuarios = new UsuarioJpaController(emf);
        controlCondicionesAloj = new CondicionesalojamientoJpaController(emf);
        controlActividades = new ActividadesJpaController(emf);
        controlActividad = new ActividadJpaController(emf);
        controlServicio = new ServicioJpaController(emf);
        controlServicios = new ServiciosJpaController(emf);
        controlImagen = new ImagenJpaController(emf);
        condiciones = new ArrayList();

    }

    public AlojamientoJpaController getControlAlojamientos() {
        return controlAlojamientos;
    }

    public void setControlAlojamientos(AlojamientoJpaController controlAlojamientos) {
        this.controlAlojamientos = controlAlojamientos;
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

    public List<Condicionesalojamiento> getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(List<Condicionesalojamiento> condiciones) {
        this.condiciones = condiciones;
    }

    public Condicionesalojamiento getCondicionesAlojamiento() {
        return condicionesAlojamiento;
    }

    public void setCondicionesAlojamiento(Condicionesalojamiento condicionesAlojamiento) {
        this.condicionesAlojamiento = condicionesAlojamiento;
    }

    public ArrayList getListadoAlojamientos() {
        listaAlojamientosPropietario();
        return listadoAlojamientos;
    }

    public CondicionesalojamientoJpaController getControlCondicionesAloj() {
        return controlCondicionesAloj;
    }

    public void setControlCondicionesAloj(CondicionesalojamientoJpaController controlCondicionesAloj) {
        this.controlCondicionesAloj = controlCondicionesAloj;
    }

    public void setListadoAlojamientos(ArrayList listadoAlojamientos) {
        this.listadoAlojamientos = listadoAlojamientos;
    }

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public int getPosicionTab() {
        return posicionTab;
    }

    public void setPosicionTab(int posicionTab) {
        this.posicionTab = posicionTab;
    }

    public Poblacion getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(Poblacion poblacion) {
        this.poblacion = poblacion;
    }

    public PoblacionJpaController getControlPoblaciones() {
        return controlPoblaciones;
    }

    public void setControlPoblaciones(PoblacionJpaController controlPoblaciones) {
        this.controlPoblaciones = controlPoblaciones;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public ProvinciaJpaController getControlProvincias() {
        return controlProvincias;
    }

    public ServicioJpaController getControlServicio() {
        return controlServicio;
    }

    public void setControlServicio(ServicioJpaController controlServicio) {
        this.controlServicio = controlServicio;
    }

    public void setControlProvincias(ProvinciaJpaController controlProvincias) {
        this.controlProvincias = controlProvincias;
    }

    public ArrayList getListadoHorarioEntrada() {
        return listadoHorarioEntrada;
    }

    public void setListadoHorarioEntrada(ArrayList listadoHorarioEntrada) {
        this.listadoHorarioEntrada = listadoHorarioEntrada;
    }

    public ArrayList getListadoHorarioSalida() {
        return listadoHorarioSalida;
    }

    public void setListadoHorarioSalida(ArrayList listadoHorarioSalida) {
        this.listadoHorarioSalida = listadoHorarioSalida;
    }

    public String getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(String anticipo) {
        this.anticipo = anticipo;
    }

    public String getCancelacion() {
        return cancelacion;
    }

    public void setCancelacion(String cancelacion) {
        this.cancelacion = cancelacion;
    }

    public UsuarioJpaController getControlUsuarios() {
        return controlUsuarios;
    }

    public void setControlUsuarios(UsuarioJpaController controlUsuarios) {
        this.controlUsuarios = controlUsuarios;
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

    public ArrayList getListadoFotosAlojamiento() {
        return listadoFotosAlojamiento;
    }

    public void setListadoFotosAlojamiento(ArrayList listadoFotosAlojamiento) {
        this.listadoFotosAlojamiento = listadoFotosAlojamiento;
    }

    public String[] getActividadesElegidas() {
        return actividadesElegidas;
    }

    public void setActividadesElegidas(String[] actividadesElegidas) {
        this.actividadesElegidas = actividadesElegidas;
    }

    public String[] getServiciosElegidos() {
        return serviciosElegidos;
    }

    public void setServiciosElegidos(String[] serviciosElegidos) {
        this.serviciosElegidos = serviciosElegidos;
    }

    public ActividadJpaController getControlActividad() {
        return controlActividad;
    }

    public void setControlActividad(ActividadJpaController controlActividad) {
        this.controlActividad = controlActividad;
    }

    public ImagenJpaController getControlImagen() {
        return controlImagen;
    }

    public void setControlImagen(ImagenJpaController controlImagen) {
        this.controlImagen = controlImagen;
    }

    public HtmlInputHidden getFotoEliminar() {
        return fotoEliminar;
    }

    public void setFotoEliminar(HtmlInputHidden fotoEliminar) {
        this.fotoEliminar = fotoEliminar;
    }

    public boolean isVacio() {
        return vacio;
    }

    public void setVacio(boolean vacio) {
        this.vacio = vacio;
    }

    //GETTER AND SETTER NEW HOUSE
    public String getNombreAlojamiento() {
        return nombreAlojamiento;
    }

    public void setNombreAlojamiento(String nombreAlojamiento) {
        this.nombreAlojamiento = nombreAlojamiento;
    }

    public String getRegistroTurismo() {
        return registroTurismo;
    }

    public void setRegistroTurismo(String registroTurismo) {
        this.registroTurismo = registroTurismo;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    //METODOS-----------------------------------------------------------------------------------------------------
    public ArrayList getHorarioEntrada() {
        if (listadoHorarioEntrada == null) {
            listadoHorarioEntrada = new ArrayList();
            listadoHorarioEntrada.add(new SelectItem("08:00", "08:00"));
            listadoHorarioEntrada.add(new SelectItem("09:00", "09:00"));
            listadoHorarioEntrada.add(new SelectItem("10:00", "10:00"));
            listadoHorarioEntrada.add(new SelectItem("11:00", "11:00"));
            listadoHorarioEntrada.add(new SelectItem("12:00", "12:00"));
            listadoHorarioEntrada.add(new SelectItem("13:00", "13:00"));
            listadoHorarioEntrada.add(new SelectItem("14:00", "14:00"));
            listadoHorarioEntrada.add(new SelectItem("15:00", "15:00"));
            listadoHorarioEntrada.add(new SelectItem("16:00", "16:00"));
            listadoHorarioEntrada.add(new SelectItem("17:00", "17:00"));
            listadoHorarioEntrada.add(new SelectItem("18:00", "18:00"));
            listadoHorarioEntrada.add(new SelectItem("19:00", "19:00"));
            listadoHorarioEntrada.add(new SelectItem("20:00", "20:00"));
            listadoHorarioEntrada.add(new SelectItem("21:00", "21:00"));
            listadoHorarioEntrada.add(new SelectItem("22:00", "22:00"));
            listadoHorarioEntrada.add(new SelectItem("23:00", "23:00"));
            listadoHorarioEntrada.add(new SelectItem("00:00", "00:00"));
            listadoHorarioEntrada.add(new SelectItem("01:00", "01:00"));
            listadoHorarioEntrada.add(new SelectItem("02:00", "02:00"));
            listadoHorarioEntrada.add(new SelectItem("03:00", "03:00"));
            listadoHorarioEntrada.add(new SelectItem("04:00", "04:00"));
            listadoHorarioEntrada.add(new SelectItem("05:00", "05:00"));
            listadoHorarioEntrada.add(new SelectItem("06:00", "06:00"));
            listadoHorarioEntrada.add(new SelectItem("07:00", "07:00"));
        }
        return listadoHorarioEntrada;
    }

    public ArrayList getHorarioSalida() {
        if (listadoHorarioSalida == null) {
            listadoHorarioSalida = new ArrayList();
            listadoHorarioSalida.add(new SelectItem("08:00", "08:00"));
            listadoHorarioSalida.add(new SelectItem("09:00", "09:00"));
            listadoHorarioSalida.add(new SelectItem("10:00", "10:00"));
            listadoHorarioSalida.add(new SelectItem("11:00", "11:00"));
            listadoHorarioSalida.add(new SelectItem("12:00", "12:00"));
            listadoHorarioSalida.add(new SelectItem("13:00", "13:00"));
            listadoHorarioSalida.add(new SelectItem("14:00", "14:00"));
            listadoHorarioSalida.add(new SelectItem("15:00", "15:00"));
            listadoHorarioSalida.add(new SelectItem("16:00", "16:00"));
            listadoHorarioSalida.add(new SelectItem("17:00", "17:00"));
            listadoHorarioSalida.add(new SelectItem("18:00", "18:00"));
            listadoHorarioSalida.add(new SelectItem("19:00", "19:00"));
            listadoHorarioSalida.add(new SelectItem("20:00", "20:00"));
            listadoHorarioSalida.add(new SelectItem("21:00", "21:00"));
            listadoHorarioSalida.add(new SelectItem("22:00", "22:00"));
            listadoHorarioSalida.add(new SelectItem("23:00", "23:00"));
            listadoHorarioSalida.add(new SelectItem("00:00", "00:00"));
            listadoHorarioSalida.add(new SelectItem("01:00", "01:00"));
            listadoHorarioSalida.add(new SelectItem("02:00", "02:00"));
            listadoHorarioSalida.add(new SelectItem("03:00", "03:00"));
            listadoHorarioSalida.add(new SelectItem("04:00", "04:00"));
            listadoHorarioSalida.add(new SelectItem("05:00", "05:00"));
            listadoHorarioSalida.add(new SelectItem("06:00", "06:00"));
            listadoHorarioSalida.add(new SelectItem("07:00", "07:00"));
        }
        return listadoHorarioSalida;
    }

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

    public ArrayList getListaActividades() {
        if (listadoActividades == null) {
            listadoActividades = new ArrayList();
            List<Actividades> listaAct = controlActividades.findActividadesEntities();
            for (Actividades act : listaAct) {
                listadoActividades.add(new SelectItem(act.getCodigoActividad(), act.getNombreActividad()));
            }

            restaurarServiciosActividades();
        }
        return listadoActividades;
    }

    public void restaurarServiciosActividades() {
        //RESTAURAR ACTIVIDADES
        List<Actividad> listaActAloj = controlActividad.findByAlojamiento(alojamiento);
        actividadesElegidas = new String[listaActAloj.size()];
        int i = 0;
        for (Actividad act : listaActAloj) {
            actividadesElegidas[i] = act.getCodigoActividad().getCodigoActividad().toString();
            i++;
        }

        //RESTAURAR SERVICIOS
        List<Servicio> listaServAloj = controlServicio.findByAlojamiento(alojamiento);
        serviciosElegidos = new String[listaServAloj.size()];
        int j = 0;
        for (Servicio serv : listaServAloj) {
            serviciosElegidos[j] = serv.getCodigoServicio().getCodigoServicio().toString();
            j++;
        }
    }

    public ArrayList getListaServicios() {
        if (listadoServicios == null) {
            listadoServicios = new ArrayList();
            List<Servicios> listaServ = controlServicios.findServiciosEntities();
            for (Servicios serv : listaServ) {
                listadoServicios.add(new SelectItem(serv.getCodigoServicio(), serv.getNombreServicio()));
            }
        }
        return listadoServicios;
    }

    public void bajaAlojamiento() {
        try {
            Alojamiento aloja = controlAlojamientos.findAlojamiento(alojamiento.getRegistroTurismo());
            controlAlojamientos.destroy(aloja.getRegistroTurismo());
            listadoAlojamientos = null;
            listaAlojamientosPropietario();
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void inicializarAlojamiento() {

        alojamiento = propietario.getAlojamientoList().get(0);
        condicionesAlojamiento = alojamiento.getCondicionesalojamientoList().get(0);
        poblacion = controlPoblaciones.findPoblacion(alojamiento.getCodigoPoblacion());
        provincia = controlProvincias.findProvincia(poblacion.getCodigoProvincia()).getNombreProvincia();

        cambiarAnticipoCancel();
        restaurarImagenes();
    }

    public void cambiarAlojamiento() {
        String codAlojamiento = alojamiento.getRegistroTurismo();

        alojamiento = controlAlojamientos.findAlojamiento(codAlojamiento);
        condicionesAlojamiento = alojamiento.getCondicionesalojamientoList().get(0);
        poblacion = controlPoblaciones.findPoblacion(alojamiento.getCodigoPoblacion());
        provincia = controlProvincias.findProvincia(poblacion.getCodigoProvincia()).getNombreProvincia();

        cambiarAnticipoCancel();
        restaurarServiciosActividades();
        restaurarImagenes();

    }

    public void restaurarImagenes() {
        List<Imagen> listaImgAloj = controlImagen.findByAlojamiento(alojamiento);
        listadoFotosAlojamiento = new ArrayList();
        for (Imagen img : listaImgAloj) {
            listadoFotosAlojamiento.add(img);
        }
    }

    public void cambiarAnticipoCancel() {
        int comprobarAnticipo = alojamiento.getCondicionesalojamientoList().get(0).getD();
        int comprobarCancelacion = alojamiento.getCondicionesalojamientoList().get(0).getF();

        if (comprobarAnticipo > 0) {
            anticipo = "2";
        } else {
            anticipo = "1";
        }

        if (comprobarCancelacion > 0) {
            cancelacion = "2";
        } else {
            cancelacion = "1";
        }
    }

    public void modificarResumen(Alojamiento aloj) {
        try {
            alojamiento = aloj;
            controlAlojamientos.edit(alojamiento);

            alojamientoCompleto();
            listadoAlojamientos = null;
            listaAlojamientosPropietario();
            controlUsuarios.edit(propietario);

            
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificarComentario() {
        try {
            controlAlojamientos.edit(alojamiento);
            controlUsuarios.edit(propietario);

            alojamientoCompleto();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificarPrecios() {
        try {
            controlAlojamientos.edit(alojamiento);
            controlCondicionesAloj.edit(condicionesAlojamiento);
            alojamientoCompleto();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificarCondiciones() {
        try {
            controlCondicionesAloj.edit(condicionesAlojamiento);
            alojamientoCompleto();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificarActServ() {

        List<Actividad> listaAct = controlActividad.findByAlojamiento(alojamiento);
        List<Servicio> listaServ = controlServicio.findByAlojamiento(alojamiento);

        //MODIFICACION DE ACTIVIDADES
        for (Actividad act : listaAct) {
            try {
                controlActividad.destroy(act.getCodA());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (int i = 0; i < actividadesElegidas.length; i++) {
            try {
                Actividades acts = controlActividades.findActividades(Integer.parseInt(actividadesElegidas[i]));
                Actividad act = new Actividad(null, acts, alojamiento);
                controlActividad.create(act);
            } catch (Exception ex) {
                Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //MODIFICACION DE SERVICIOS
        for (Servicio serv : listaServ) {
            try {
                controlServicio.destroy(serv.getCodS());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (int i = 0; i < serviciosElegidos.length; i++) {
            try {
                Servicios servs = controlServicios.findServicios(Integer.parseInt(serviciosElegidos[i]));
                Servicio serv = new Servicio(null, servs, alojamiento);
                controlServicio.create(serv);
            } catch (Exception ex) {
                Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        alojamientoCompleto();

    }

    public void alojamientoCompleto() {

        if (alojamiento.getCompleto() == 1) {// SI EL ALOJAMIENTO NO ESTA COMPLETO.....
            if (alojamiento.getNumeroHab() > 0
                    & alojamiento.getNumeroWc() > 0
                    && alojamiento.getMetrosCuadrados() > 0
                    && alojamiento.getPrecioNoche() > 0
                    && alojamiento.getComentario().length() > 0
                    && alojamiento.getActividadList().size() > 0
                    && alojamiento.getServicioList().size() > 0) {
                try {
                    //ESTABLECER EL ALOJAMIENTO COMO ALOJAMIENTO COMPLETO
                    alojamiento.setCompleto(0);
                    controlAlojamientos.edit(alojamiento);
                    listadoAlojamientos = null;
                    listaAlojamientosPropietario();
                    controlUsuarios.edit(propietario);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void alojamientoCompletoRecharge(Alojamiento aloj){

        alojamiento = aloj;
        
        if (alojamiento.getCompleto() == 1) {// SI EL ALOJAMIENTO NO ESTA COMPLETO.....
            if (alojamiento.getNumeroHab() > 0
                    & alojamiento.getNumeroWc() > 0
                    && alojamiento.getMetrosCuadrados() > 0
                    && alojamiento.getPrecioNoche() > 0
                    && alojamiento.getComentario().length() > 0
                    && alojamiento.getActividadList().size() > 0
                    && alojamiento.getServicioList().size() > 0) {
                try {
                    //ESTABLECER EL ALOJAMIENTO COMO ALOJAMIENTO COMPLETO
                    alojamiento.setCompleto(0);
                    controlAlojamientos.edit(alojamiento);
                    listadoAlojamientos = null;
                    listaAlojamientosPropietario();
                    controlUsuarios.edit(propietario);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    

    public void uploadPhoto(FileUploadEvent e) throws IOException {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");

        //VERIFICAR QUE LA CARPETA DEL ALOJAMIENTO ESTE CREADA
        File carpetaAlojamiento = new File(path + "/../../web/images/Alojamientos/" + alojamiento.getRegistroTurismo());
        if (!carpetaAlojamiento.exists()) {
            if (carpetaAlojamiento.mkdir()) {
                System.out.println("Directory is created!");
            }
        }
        UploadedFile uploadedPhoto = e.getFile();

        byte[] bytes = null;

        if (null != uploadedPhoto) {
            bytes = uploadedPhoto.getContents();
            String filename = FilenameUtils.getName(uploadedPhoto.getFileName());
            Imagen imagen = new Imagen();

            imagen.setImagen(filename);
            imagen.setRegistroTurismo(alojamiento);

            controlImagen.create(imagen);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(carpetaAlojamiento + "/" + filename)));
            stream.write(bytes);
            stream.close();
        }
        FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Tus Fotos fueron subidas Correctamente", ""));
    }

    public void eliminarFoto() {

        try {
            //ELIMINAR FILE DE LA CARPETA IMAGENES
            String codImgStr = fotoEliminar.getValue().toString();
            int codImg = Integer.parseInt(codImgStr);
            Imagen img = controlImagen.findImagen(codImg);

            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            File carpetaAlojamiento = new File(path + "/../../web/images/Alojamientos/" + alojamiento.getRegistroTurismo());

            File result = new File(carpetaAlojamiento + "/" + img.getImagen());
            result.delete();

            //ELIMINAR FILE DE LA BASE DE DATOS
            controlImagen.destroy(codImg);
            restaurarImagenes();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void crearAlojamiento() {
        try {
            //Crear alojamiento con datos vacíos
            alojamiento = new Alojamiento(registroTurismo, city, nombreAlojamiento, 0, 0, 0, 0, 0, "", "", 0, 0, "", 1);

            //Darle al alojamiento el nif de su propietario
            alojamiento.setNif(propietario);

            //------------------------------------------------------------
            //Crear condiciones del alojamiento a 0.
            Condicionesalojamiento nuevasCond = new Condicionesalojamiento(null, 0, 0, 0, 0, 0, 0, "00:00", "00:00", 0);
            nuevasCond.setRegistroTurismo(alojamiento);
            //Creamos alojamiento vacio y condiciones vacias
            controlAlojamientos.create(alojamiento);
            controlCondicionesAloj.create(nuevasCond);
            listadoAlojamientos.add(alojamiento);
            propietario.setAlojamientoList(listadoAlojamientos);
            int alojPos = listadoAlojamientos.size() - 1;
            condiciones.add(nuevasCond);
            propietario.getAlojamientoList().get(alojPos).setCondicionesalojamientoList(condiciones);
            listadoAlojamientos = null;
            listaAlojamientosPropietario();
            controlUsuarios.edit(propietario);

        } catch (Exception ex) {
            Logger.getLogger(bkOwner.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int puntuacionMediaAloj() {

        List<Opinion> listadoOpiniones = alojamiento.getOpinionList();
        double sumatorio = 0;
        for (Opinion op : listadoOpiniones) {
            sumatorio += op.getPuntuacionMedia();
        }

        int notaMediaAlojamiento = 0;
        if (sumatorio > 0) {
            sumatorio = sumatorio / 2;
            sumatorio = sumatorio / listadoOpiniones.size();

            Double calculo = Math.ceil(sumatorio);
            notaMediaAlojamiento = calculo.intValue();
        } else {
            notaMediaAlojamiento = 5;
        }
        return notaMediaAlojamiento;

    }

}
