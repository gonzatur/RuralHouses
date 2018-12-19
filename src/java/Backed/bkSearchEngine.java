package Backed;

import DAO.AlojamientoJpaController;
import DAO.FavoritosJpaController;
import DAO.ImagenJpaController;
import DAO.OcupacionJpaController;
import DAO.PoblacionJpaController;
import DAO.ProvinciaJpaController;
import DAO.PuebloszonaJpaController;
import DAO.ReservaJpaController;
import DAO.UsuarioJpaController;
import DAO.ZonaJpaController;
import DAO.exceptions.NonexistentEntityException;
import DTO.Alojamiento;
import DTO.Comunidades;
import DTO.Condicionesalojamiento;
import DTO.Favoritos;
import DTO.Imagen;
import DTO.ObjetoBusqueda;
import DTO.Ocupacion;
import DTO.OcupacionPK;
import DTO.Poblacion;
import DTO.Provincia;
import DTO.Puebloszona;
import DTO.Reserva;
import DTO.Usuario;
import DTO.Zona;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author gonza
 */
public class bkSearchEngine {

    private EntityManagerFactory emf;
    private AlojamientoJpaController controlAlojamientos;
    private PoblacionJpaController controlPoblaciones;
    private ProvinciaJpaController controlProvincias;
    private ZonaJpaController controlZonas;
    private OcupacionJpaController controlOcupacion;
    private PuebloszonaJpaController controlZonaAloj;
    private ImagenJpaController controlImagen;
    private ReservaJpaController controlReservas;
    private UsuarioJpaController controlUsuarios;
    private FavoritosJpaController controlFavoritos;

    //DATOS NECESARIOS PARA LA BUSQUEDA
    private Object objetoBuscado;
    private Date fechaEnt;
    private Date fechaSal;
    private Date minDateSal;
    private int numeroPersonas;

    private Date fechaEntAloj;
    private Date fechaSalAloj;
    private int numeroPersonasAloj;

    //LISTADOS A MOSTRAR
    private ArrayList listadoAlojamientosBuscados;

    //UBICACION DEL ALOJAMIENTO
    private Poblacion poblacion;
    private Provincia provincia;
    private Comunidades comunidad;

    //Datos para cargar la pagina del ALOJAMIENTO
    private Alojamiento alojamiento;
    private ArrayList listadoPersonasDisponiblesAlojamiento = null;
    private ArrayList listadoImagenes;
    private ArrayList listadoAnios;
    private ArrayList listadoMeses;
    private boolean respuestaBusqueda;
    private String botonPagar;
    private String codigoPago;
    private String codigoPagoIntroducido;
    private String respuesta;
    private String solucion;

    //Datos de la reserva
    private int numeroNoches;
    private float precioSinComision;
    private float comision;
    private float precioConComision;
    private float anticipo;
    private String fechaAntesCancelacion;
    private int nochesRebuscar;
    private String mensajeSacar;

    //Recuperar cliente
    private Usuario usu;

    public bkSearchEngine() {
        emf = Persistence.createEntityManagerFactory("RuralHousesPU");
        controlAlojamientos = new AlojamientoJpaController(emf);
        controlPoblaciones = new PoblacionJpaController(emf);
        controlZonas = new ZonaJpaController(emf);
        controlOcupacion = new OcupacionJpaController(emf);
        controlZonaAloj = new PuebloszonaJpaController(emf);
        controlImagen = new ImagenJpaController(emf);
        controlProvincias = new ProvinciaJpaController(emf);
        controlReservas = new ReservaJpaController(emf);
        controlUsuarios = new UsuarioJpaController(emf);
        controlFavoritos = new FavoritosJpaController(emf);
        respuestaBusqueda = true;
        fechaEnt = new Date();
        //MGC
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaEnt);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        fechaSal = calendar.getTime();
        listadoPersonasDisponiblesAlojamiento = null;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public AlojamientoJpaController getControlAlojamientos() {
        return controlAlojamientos;
    }

    public void setControlAlojamientos(AlojamientoJpaController controlAlojamientos) {
        this.controlAlojamientos = controlAlojamientos;
    }

    public PoblacionJpaController getControlPoblaciones() {
        return controlPoblaciones;
    }

    public void setControlPoblaciones(PoblacionJpaController controlPoblaciones) {
        this.controlPoblaciones = controlPoblaciones;
    }

    public ZonaJpaController getControlZonas() {
        return controlZonas;
    }

    public void setControlZonas(ZonaJpaController controlZonas) {
        this.controlZonas = controlZonas;
    }

    public Date getFechaEnt() {
        return fechaEnt;
    }

    public void setFechaEnt(Date fechaEnt) {
        this.fechaEnt = fechaEnt;
        obtenerFechaSalida();
    }

    public Date getFechaSal() {
        return fechaSal;
    }

    public void setFechaSal(Date fechaSal) {
        this.fechaSal = fechaSal;
    }

    public Date getMinDateSal() {
        return minDateSal;
    }

    public void setMinDateSal(Date minDateSal) {
        this.minDateSal = minDateSal;
    }

    public Object getObjetoBuscado() {
        return objetoBuscado;
    }

    public void setObjetoBuscado(Object objetoBuscado) {
        this.objetoBuscado = objetoBuscado;
    }

    public int getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(int numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }

    public ArrayList getListadoAlojamientosBuscados() {
        return listadoAlojamientosBuscados;
    }

    public void setListadoAlojamientosBuscados(ArrayList listadoAlojamientosBuscados) {
        this.listadoAlojamientosBuscados = listadoAlojamientosBuscados;
    }

    public OcupacionJpaController getControlOcupacion() {
        return controlOcupacion;
    }

    public void setControlOcupacion(OcupacionJpaController controlOcupacion) {
        this.controlOcupacion = controlOcupacion;
    }

    public PuebloszonaJpaController getControlZonaAloj() {
        return controlZonaAloj;
    }

    public void setControlZonaAloj(PuebloszonaJpaController controlZonaAloj) {
        this.controlZonaAloj = controlZonaAloj;
    }

    public Poblacion getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(Poblacion poblacion) {
        this.poblacion = poblacion;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Comunidades getComunidad() {
        return comunidad;
    }

    public void setComunidad(Comunidades comunidad) {
        this.comunidad = comunidad;
    }

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public ArrayList getListadoPersonasDisponiblesAlojamiento() {
        return listadoPersonasDisponiblesAlojamiento;
    }

    public void setListadoPersonasDisponiblesAlojamiento(ArrayList listadoPersonasDisponiblesAlojamiento) {
        this.listadoPersonasDisponiblesAlojamiento = listadoPersonasDisponiblesAlojamiento;
    }

    public int getNumeroNoches() {
        return numeroNoches;
    }

    public void setNumeroNoches(int numeroNoches) {
        this.numeroNoches = numeroNoches;
    }

    public float getPrecioSinComision() {
        return precioSinComision;
    }

    public void setPrecioSinComision(float precioSinComision) {
        this.precioSinComision = precioSinComision;
    }

    public float getComision() {
        return comision;
    }

    public void setComision(float comision) {
        this.comision = comision;
    }

    public float getPrecioConComision() {
        return precioConComision;
    }

    public void setPrecioConComision(float precioConComision) {
        this.precioConComision = precioConComision;
    }

    public float getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(float anticipo) {
        this.anticipo = anticipo;
    }

    public String getFechaAntesCancelacion() {
        return fechaAntesCancelacion;
    }

    public void setFechaAntesCancelacion(String fechaAntesCancelacion) {
        this.fechaAntesCancelacion = fechaAntesCancelacion;
    }

    public ArrayList getListadoImagenes() {
        return listadoImagenes;
    }

    public void setListadoImagenes(ArrayList listadoImagenes) {
        this.listadoImagenes = listadoImagenes;
    }

    public ImagenJpaController getControlImagen() {
        return controlImagen;
    }

    public void setControlImagen(ImagenJpaController controlImagen) {
        this.controlImagen = controlImagen;
    }

    public boolean isRespuestaBusqueda() {
        return respuestaBusqueda;
    }

    public void setRespuestaBusqueda(boolean respuestaBusqueda) {
        this.respuestaBusqueda = respuestaBusqueda;
    }

    public Date getFechaEntAloj() {
        return fechaEntAloj;
    }

    public void setFechaEntAloj(Date fechaEntAloj) {
        this.fechaEntAloj = fechaEntAloj;
        obtenerFechaSalida();
    }

    public Date getFechaSalAloj() {
        return fechaSalAloj;
    }

    public void setFechaSalAloj(Date fechaSalAloj) {
        this.fechaSalAloj = fechaSalAloj;
    }

    public int getNumeroPersonasAloj() {
        return numeroPersonasAloj;
    }

    public void setNumeroPersonasAloj(int numeroPersonasAloj) {
        this.numeroPersonasAloj = numeroPersonasAloj;
    }

    public ProvinciaJpaController getControlProvincias() {
        return controlProvincias;
    }

    public void setControlProvincias(ProvinciaJpaController controlProvincias) {
        this.controlProvincias = controlProvincias;
    }

    public ArrayList getListadoAnios() {
        return listadoAnios;
    }

    public void setListadoAnios(ArrayList listadoAnios) {
        this.listadoAnios = listadoAnios;
    }

    public ArrayList getListadoMeses() {
        return listadoMeses;
    }

    public void setListadoMeses(ArrayList listadoMeses) {
        this.listadoMeses = listadoMeses;
    }

    public ReservaJpaController getControlReservas() {
        return controlReservas;
    }

    public void setControlReservas(ReservaJpaController controlReservas) {
        this.controlReservas = controlReservas;
    }

    public UsuarioJpaController getControlUsuarios() {
        return controlUsuarios;
    }

    public void setControlUsuarios(UsuarioJpaController controlUsuarios) {
        this.controlUsuarios = controlUsuarios;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }

    public Usuario getUsu() {
        usu = null;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        BkManageBeanSesion BkManageBeanSesion = new BkManageBeanSesion();

        HttpSession session = (HttpSession) ctx.getSession(false);
        BkManageBeanSesion = (BkManageBeanSesion) session.getAttribute("BkManageBeanSesion");
        if (BkManageBeanSesion != null) {
            usu = (Usuario) BkManageBeanSesion.getUsuarioLog();
        }
        return usu;
    }

    public void setUsu(Usuario usu) {
        this.usu = usu;
    }

    public String getBotonPagar() {
        return botonPagar;
    }

    public void setBotonPagar(String botonPagar) {
        this.botonPagar = botonPagar;
    }

    public String getCodigoPago() {
        return codigoPago;
    }

    public void setCodigoPago(String codigoPago) {
        this.codigoPago = codigoPago;
    }

    public String getCodigoPagoIntroducido() {
        return codigoPagoIntroducido;
    }

    public void setCodigoPagoIntroducido(String codigoPagoIntroducido) {
        this.codigoPagoIntroducido = codigoPagoIntroducido;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public int getNochesRebuscar() {
        return nochesRebuscar;
    }

    public void setNochesRebuscar(int nochesRebuscar) {
        this.nochesRebuscar = nochesRebuscar;
    }

    public String getMensajeSacar() {
        return mensajeSacar;
    }

    public void setMensajeSacar(String mensajeSacar) {
        this.mensajeSacar = mensajeSacar;
    }

    //METODOS ------------------------------------------------------------------
    public void obtenerFechaSalida() {
        Date fecEnt = fechaEnt;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecEnt);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        //Fecha mínima de salida = Fecha de entrada +1 
        minDateSal = calendar.getTime();
        fechaSal = calendar.getTime();

        Date fecEntAj = fechaEntAloj;

        if (fecEntAj != null) {
            //minDateSal = new Date(); //NO ??
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(fecEntAj);
            //Fecha mínima de salida = Fecha de entrada +1 
            //minDateSal = calendar2.getTime(); //NO ??
            fechaSalAloj = calendar2.getTime();

        }
    }

    public String buscar() {
        String respuesta = "";
        solucion = "";

        if (objetoBuscado != null && fechaEnt != null && fechaSal != null && numeroPersonas > 0) {
            //Obtener numero de noches
            float mili = fechaSal.getTime() - fechaEnt.getTime();
            float daysBetween = mili / (24 * 60 * 60 * 1000);
            numeroNoches = (int) daysBetween;

            String objetoBuscarStr = objetoBuscado.toString();
            Alojamiento aloj = null;
            Poblacion pob = null;
            Zona zon = null;

            if (objetoBuscarStr.matches("[0-9]+") && objetoBuscarStr.length() > 0) {
                pob = controlPoblaciones.findPoblacion(Integer.parseInt(objetoBuscarStr));
                zon = controlZonas.findZona(Integer.parseInt(objetoBuscarStr));

            } else {
                aloj = controlAlojamientos.findAlojamiento(objetoBuscarStr);
            }

            if (aloj == null && pob == null && zon == null) {
                respuesta = "error";
                solucion = "Lo sentimos, nuestro sistema no reconoce este destino";
            }

            //Obtener todos los dias seleccionados
            List<Date> dates = new ArrayList();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(fechaEnt);

            while (calendar.getTime().before(fechaSal)) {
                Date result = calendar.getTime();
                dates.add(result);
                calendar.add(Calendar.DATE, 1);
            }

            if (pob != null) {
                listadoAlojamientosBuscados = new ArrayList();
                List<Alojamiento> allAlojamientos = controlAlojamientos.findResultadosBuscador(pob.getCodigoPoblacion());
                boolean algunaNocheOcupada = false;

                for (int i = 0; i < allAlojamientos.size(); i++) {
                    Alojamiento esteAloj = allAlojamientos.get(i);
                    algunaNocheOcupada = false;
                    for (int j = 0; j < dates.size(); j++) {
                        Date fch = dates.get(j);

                        OcupacionPK dateNight = new OcupacionPK(esteAloj.getRegistroTurismo(), fch);
                        Ocupacion nchOcupada = controlOcupacion.findOcupacion(dateNight);
                        if (nchOcupada != null) {
                            algunaNocheOcupada = true;
                        }
                    }

                    if (!algunaNocheOcupada && esteAloj.getPlazasMax() >= numeroPersonas) {
                        listadoAlojamientosBuscados.add(esteAloj);
                    }
                }

            }

            if (aloj != null) {
                listadoAlojamientosBuscados = new ArrayList();
                Alojamiento esteAloj = controlAlojamientos.findAlojamiento(objetoBuscarStr);
                boolean algunaNocheOcupada = false;
                for (int j = 0; j < dates.size(); j++) {
                    Date fch = dates.get(j);

                    OcupacionPK dateNight = new OcupacionPK(esteAloj.getRegistroTurismo(), fch);
                    Ocupacion nchOcupada = controlOcupacion.findOcupacion(dateNight);
                    if (nchOcupada != null) {
                        algunaNocheOcupada = true;
                    }
                }

                if (!algunaNocheOcupada && esteAloj.getPlazasMax() >= numeroPersonas) {
                    listadoAlojamientosBuscados.add(esteAloj);
                }

            }

            if (zon != null) {
                List<Puebloszona> relacionZonaAloj = controlZonaAloj.findByZona(zon);
                ArrayList<Alojamiento> alojamientosZona = new ArrayList();
                listadoAlojamientosBuscados = new ArrayList();

                for (Puebloszona puebloszona : relacionZonaAloj) {
                    Alojamiento alojamientoBuscar = controlAlojamientos.findAlojamiento(puebloszona.getRegistroTurismo().getRegistroTurismo());
                    alojamientosZona.add(alojamientoBuscar);
                }

                //COMPROBACION DE FECHAS OCUPADAS Y ESPACIO EN LA VIVIENDA
                boolean algunaNocheOcupada = false;

                for (int i = 0; i < alojamientosZona.size(); i++) {
                    Alojamiento esteAloj = alojamientosZona.get(i);
                    algunaNocheOcupada = false;
                    for (int j = 0; j < dates.size(); j++) {
                        Date fch = dates.get(j);

                        OcupacionPK dateNight = new OcupacionPK(esteAloj.getRegistroTurismo(), fch);
                        Ocupacion nchOcupada = controlOcupacion.findOcupacion(dateNight);
                        if (nchOcupada != null) {
                            algunaNocheOcupada = true;
                        }
                    }

                    if (!algunaNocheOcupada && esteAloj.getPlazasMax() >= numeroPersonas) {
                        listadoAlojamientosBuscados.add(esteAloj);
                    }
                }
            }

            if (aloj != null || pob != null || zon != null) {
                ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
                try {
                    ctx.redirect("/RuralHouses/faces/search.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(bkSearchEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            respuesta = "error";
            solucion = "Indique destino y fechas de entrada y salida";
        }
        objetoBuscado = null;

        return respuesta;

    }

    public boolean rebuscarAlojamiento() {
        //Obtener todos los dias seleccionados
        List<Date> dates = new ArrayList();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(fechaEntAloj);

        while (calendar.getTime().before(fechaSalAloj)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        boolean algunaNocheOcupada = false;
        for (int j = 0; j < dates.size(); j++) {
            Date fch = dates.get(j);

            OcupacionPK dateNight = new OcupacionPK(alojamiento.getRegistroTurismo(), fch);
            Ocupacion nchOcupada = controlOcupacion.findOcupacion(dateNight);
            if (nchOcupada != null) {
                algunaNocheOcupada = true;
            }
        }

        float mili = fechaSalAloj.getTime() - fechaEntAloj.getTime();
        float daysBetween = mili / (24 * 60 * 60 * 1000);
        nochesRebuscar = (int) daysBetween;

        if (nochesRebuscar > 0) {

            if (!algunaNocheOcupada && alojamiento.getPlazasMax() >= numeroPersonasAloj && numeroPersonasAloj > 0) {
                respuestaBusqueda = true;
                calcularPrecioReserva();
                mensajeSacar = "";
            } else {
                respuestaBusqueda = false;
            }

        } else {
            mensajeSacar = "El número de noches mínimas es 1";
            respuestaBusqueda = false;
        }

        return respuestaBusqueda;
    }

    public String saberPueblo(String registroTurismo) {

        int codigoPoblacion = controlAlojamientos.findAlojamiento(registroTurismo).getCodigoPoblacion();
        String nombrePoblacion = controlPoblaciones.findPoblacion(codigoPoblacion).getNombrePoblacion();

        return nombrePoblacion;
    }

    public String saberProvincia(String registroTurismo) {

        int codigoPoblacion = controlAlojamientos.findAlojamiento(registroTurismo).getCodigoPoblacion();
        Poblacion pobla = controlPoblaciones.findPoblacion(codigoPoblacion);
        int codigoProvincia = pobla.getCodigoProvincia();
        Provincia prov = controlProvincias.findProvincia(codigoProvincia);

        return prov.getNombreProvincia();
    }

    public void mostrarAlojamiento(String registroTurismo) {
        alojamiento = controlAlojamientos.findAlojamiento(registroTurismo);

        //Llamada a calcular precio reserva para obtener datos
        fechaEntAloj = fechaEnt;
        fechaSalAloj = fechaSal;
        numeroPersonasAloj = numeroPersonas;
        calcularPrecioReserva();

        //Coger contexto
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ctx.redirect("/RuralHouses/faces/house.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(bkSearchEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void irAReserva() {
        //Coger contexto
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ctx.redirect("/RuralHouses/faces/reserva.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(bkSearchEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList getListaImagenes() {

        listadoImagenes = new ArrayList();

        List<Imagen> listaImgAloj = controlImagen.findByAlojamiento(alojamiento);
        for (Imagen img : listaImgAloj) {
            listadoImagenes.add(img);
        }

        return listadoImagenes;

    }

    public ArrayList getListaPersonasAlojamiento() {
        listadoPersonasDisponiblesAlojamiento = null;
        if (listadoPersonasDisponiblesAlojamiento == null) {
            listadoPersonasDisponiblesAlojamiento = new ArrayList();

            int mayorNumPersonas = alojamiento.getPlazasMax();
            for (int i = 1; i <= mayorNumPersonas; i++) {
                listadoPersonasDisponiblesAlojamiento.add(new SelectItem(i, String.valueOf(i)));
            }
        }
        return listadoPersonasDisponiblesAlojamiento;
    }

    public String getSpecialDays() {
        String ocupadosArray = "[";
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        for (Ocupacion dia : alojamiento.getOcupacionList()) {
            String today = formatter.format(dia.getOcupacionPK().getNoche());
            ocupadosArray += "'" + today + "',";
        }
        return ocupadosArray += "]";
    }

    public void calcularPrecioReserva() {

        //NOCHES SELECCIONADAS
        float mili = fechaSalAloj.getTime() - fechaEntAloj.getTime();
        float daysBetween = mili / (24 * 60 * 60 * 1000);
        numeroNoches = (int) daysBetween;

        //PRECIO TOTAL SIN COMISION
        int plazasMinCobrar = alojamiento.getCondicionesalojamientoList().get(0).getA();
        if (numeroPersonasAloj <= plazasMinCobrar) {
            precioSinComision = alojamiento.getPrecioNoche() * plazasMinCobrar * numeroNoches;
        } else {
            precioSinComision = alojamiento.getPrecioNoche() * numeroPersonasAloj * numeroNoches;
        }

        //PRECIO TOTAL CON COMISION
        comision = (precioSinComision * 8) / 100;
        precioConComision = precioSinComision + comision;

        //ANTICIPO
        float anticipoRequerido = alojamiento.getCondicionesalojamientoList().get(0).getD();
        float i = 100;
        float anticipoPercent = (anticipoRequerido / i);
        anticipo = precioSinComision * anticipoPercent;

        //OBTENER FECHA DE CANCELACION
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaEntAloj); // Configuramos la fecha que se recibe

        int diasAntesCancelar = alojamiento.getCondicionesalojamientoList().get(0).getE();
        calendar.add(Calendar.DAY_OF_YEAR, -diasAntesCancelar); // numero de días a añadir, o restar en caso de días<0
        Date fechaAhora = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fechaAntesCancelacion = sdf.format(fechaAhora);
    }

    public String getDevolverFechaEntrada() {

        String[] monthNames = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        String[] daysWeek = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        //AÑO DE ENTRADA Y SALIDA
        int anioEnt = fechaEntAloj.getYear() + 1900;

        //DIA DE LA SEMANA
        Calendar c = Calendar.getInstance();
        c.setTime(fechaEntAloj);
        int numeroDiaSemana = c.get(Calendar.DAY_OF_WEEK);
        numeroDiaSemana = numeroDiaSemana - 1;

        return daysWeek[numeroDiaSemana] + ", " + fechaEntAloj.getDate() + " de " + monthNames[fechaEntAloj.getMonth()] + " del " + anioEnt
                + " a partir de las " + alojamiento.getCondicionesalojamientoList().get(0).getG();
    }

    public String getDevolverFechaSalida() {

        String[] monthNames = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        String[] daysWeek = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        //AÑO DE ENTRADA Y SALIDA
        int anioSal = fechaSalAloj.getYear() + 1900;

        //DIA DE LA SEMANA
        Calendar c = Calendar.getInstance();
        c.setTime(fechaSalAloj);
        int numeroDiaSemana = c.get(Calendar.DAY_OF_WEEK);
        numeroDiaSemana = numeroDiaSemana - 1;

        return daysWeek[numeroDiaSemana] + ", " + fechaSalAloj.getDate() + " de " + monthNames[fechaSalAloj.getMonth()] + " del " + anioSal
                + " antes de las " + alojamiento.getCondicionesalojamientoList().get(0).getG();
    }

    public int getPorcentajeAPagar() {
        int porcentajeComision = 8;
        int porcentajeAnticipo = alojamiento.getCondicionesalojamientoList().get(0).getD();

        int porcentajeTotal = porcentajeComision + porcentajeAnticipo;
        return porcentajeTotal;

    }

    public float getTotalPagarAntesReserva() {
        float pagoTotal = anticipo + comision;

        return pagoTotal;
    }

    public String saberNombre(Usuario usuario) {
        String respuesta = "";
        if (usuario != null) {
            respuesta = usu.getNombre();
        }

        return respuesta;
    }

    public String saberApellidos(Usuario usuario) {
        String respuesta = "";
        if (usuario != null) {
            respuesta = usu.getApellido1() + " " + usu.getApellido2();
        }

        return respuesta;
    }

    public String saberTelefono(Usuario usuario) {
        String respuesta = "";
        if (usuario != null) {
            respuesta = usu.getTelefono();
        }

        return respuesta;
    }

    public String saberMail(Usuario usuario) {
        String respuesta = "";
        if (usuario != null) {
            respuesta = usu.getEmail();
        }

        return respuesta;
    }

    public ArrayList getObtenerAnios() {
        if (listadoAnios == null) {
            listadoAnios = new ArrayList();

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            for (int i = 0; i <= 15; i++) {
                listadoAnios.add(new SelectItem(year, String.valueOf(year)));
                year++;
            }
        }
        return listadoAnios;
    }

    public ArrayList getObtenerMeses() {
        if (listadoMeses == null) {
            listadoMeses = new ArrayList();

            listadoMeses.add(new SelectItem("01", "01"));
            listadoMeses.add(new SelectItem("02", "02"));
            listadoMeses.add(new SelectItem("03", "03"));
            listadoMeses.add(new SelectItem("04", "04"));
            listadoMeses.add(new SelectItem("05", "05"));
            listadoMeses.add(new SelectItem("06", "06"));
            listadoMeses.add(new SelectItem("07", "07"));
            listadoMeses.add(new SelectItem("08", "08"));
            listadoMeses.add(new SelectItem("09", "09"));
            listadoMeses.add(new SelectItem("10", "10"));
            listadoMeses.add(new SelectItem("11", "11"));
            listadoMeses.add(new SelectItem("12", "12"));

        }
        return listadoMeses;
    }

    public void finalizarReserva() {
        if (usu != null) {
            codigoPago = "";
            //generacion contraseña Aleatoria
            char[] elementos = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};//SYMBOLS
            int longitud = elementos.length;
            char[] conjunto = new char[8];

            for (int i = 0; i < 6; i++) {
                int el = (int) (Math.random() * longitud);
                conjunto[i] = (char) elementos[el];
            }
            codigoPago = new String(conjunto);
            try {
                // Usuario y el password
                String usuario = "ruralhousesinfo@gmail.com";
                String pass = "ruralhousesinfo00";

                String asunto = "Verificar pago con tarjeta";
                String mensaje = "Hola " + usu.getNombre() + " le enviamos el código que deberá introducir para verificar el pago "
                        + "\n \n " + codigoPago
                        + "\n \n Equipo de Rural Houses";

                Properties props = new Properties();
                props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                // Creamos una sesión que nos permita identificarnos en el servidor con el usuario y pass informados previamente
                Authenticator autenticador = new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(usuario, pass);
                    }
                };

                Session session = Session.getInstance(props, autenticador);

                Message correo = new MimeMessage(session);

                // Entidad que envia el email
                correo.setFrom(new InternetAddress(usuario));

                // Destinatarios
                InternetAddress[] listaDestinatarios = {new InternetAddress(usu.getEmail())};
                correo.setRecipients(Message.RecipientType.TO, listaDestinatarios);

                // Asunto
                correo.setSubject(asunto);

                // Paso 4. Informamos la fecha de hoy
                correo.setSentDate(new Date());

                // Mensaje que queremos enviar
                correo.setText(mensaje);

                // Una vez creado el objeto Message con el email, se realiza en envío. En caso de fallo elevará una excepción
                Transport.send(correo);

            } catch (MessagingException ex) {
                Logger.getLogger(BkLogin.class.getName()).log(Level.SEVERE, null, ex);
            }

            botonPagar = "clicado";
        }

    }

    public void realizarPago() {
        System.out.println(codigoPago + " -codigoPago");
        System.out.println(codigoPagoIntroducido + " -codigoPagoIntroducido");
        if (!codigoPago.equals(codigoPagoIntroducido)) {
            try {
                respuesta = "Código correcto. Su reserva se ha completado satisfactoriamente. Le hemos enviado un correo con un pdf de la reserva";

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                String fechaCod = sdf.format(fechaEntAloj);
                String[] fechaSplit = fechaCod.split("/");
                String dd = fechaSplit[0];
                String mm = fechaSplit[1];
                String yy = fechaSplit[2];
                String codigoReserva = alojamiento.getRegistroTurismo() + "/" + dd + mm + yy;

                Reserva nuevaReserva = new Reserva(codigoReserva, usu, alojamiento, fechaEntAloj, fechaSalAloj, numeroPersonasAloj, anticipo, comision, precioConComision);
                controlReservas.create(nuevaReserva);
                generarPdf();
                enviarReservaEmail();
                crearOcupacion();
            } catch (Exception ex) {
                Logger.getLogger(bkSearchEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            respuesta = "Código incorrecto";

        }
    }

    public String verNombre(ObjetoBusqueda theme) {
        if (theme == null) {
            return "";
        } else {
            return theme.getNombre();
        }
    }

    public void crearOcupacion() {

        List<Date> dates = new ArrayList();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(fechaEntAloj);

        while (calendar.getTime().before(fechaSalAloj)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        for (int j = 0; j < dates.size(); j++) {
            try {
                Date fch = dates.get(j);

                Ocupacion nchOcupada = new Ocupacion(alojamiento.getRegistroTurismo(), fch, 0, alojamiento);

                controlOcupacion.create(nchOcupada);
            } catch (Exception ex) {
                Logger.getLogger(bkSearchEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /*
    -------------------
    -----------
    ----- PDF GENERATOR
    -----------
    -------------------
     */
    public void generarPdf() {
        try {
            //DATOS NECESARIOS PARA COMPLETAR DINAMICAMENTE EL PDF
            /**
             * **************************************************
             */

            //FECHAS DE ENTRADA Y SALIDA
            String nombrePoblacion = controlPoblaciones.findPoblacion(alojamiento.getCodigoPoblacion()).getNombrePoblacion();
            String[] monthNames = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};

            int anioSal = fechaSalAloj.getYear() + 1900;
            int anioEnt = fechaEntAloj.getYear() + 1900;

            String fechaEntrada = fechaEntAloj.getDate() + " de " + monthNames[fechaEntAloj.getMonth()] + " del " + anioEnt;
            String fechaSalida = fechaSalAloj.getDate() + " de " + monthNames[fechaSalAloj.getMonth()] + " del " + anioSal;

            //POBLACION Y PROVINCIA 
            Poblacion pobla = controlPoblaciones.findPoblacion(usu.getCodigoPoblacion());
            String poblacionUsuario = pobla.getNombrePoblacion();
            Provincia provinciaViajero = controlProvincias.findProvincia(pobla.getCodigoProvincia());
            Provincia provinciaAlojamiento = controlProvincias.findProvincia(controlPoblaciones.findPoblacion(alojamiento.getCodigoPoblacion()).getCodigoProvincia());
            //ALOJAMIENTO, CONDICIONES DEL MISMO Y PROPIETARIO
            Condicionesalojamiento cond = alojamiento.getCondicionesalojamientoList().get(0);
            Usuario propietario = controlUsuarios.findByEmail(alojamiento.getNif().getEmail());

            //------------------------------------------------------------------
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.setHeader("Content-disposition", "inline=filename=" + "hola" + ".pdf");
            Document document = new Document();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
                PdfWriter.getInstance(document, new FileOutputStream(path + "/../../web/images/Other/reservilla.pdf"));
                PdfWriter.getInstance(document, baos);
            } catch (DocumentException ex) {
                System.out.println(ex.getMessage());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(bkReserva.class.getName()).log(Level.SEVERE, null, ex);
            }
            document.open();

            document.setMargins(0, 0, 0, 0);
            Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL);
            Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
            Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Font font4 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
            Font font5 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
            Font font6 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
            font4.setColor(BaseColor.RED);

            //Phrase encabezado = new Phrase("\n \n \n \n \n \n \n   Localización QR", font1);
            BarcodeQRCode my_code = new BarcodeQRCode("https://maps.google.com/maps?q=" + alojamiento.getLongitud() + "," + alojamiento.getLatitud() + "", 140, 140, null);

            Image qr_image = my_code.getImage();
            qr_image.setAbsolutePosition(5f, 700f);

            //PARRAFO CONTRATO + LOCALIDAD + FECHA
            Paragraph parrafoArribaDer = new Paragraph();

            parrafoArribaDer.add(new Phrase("CONTRATO DE ARRENDAMIENTO POR TEMPORADA \n \n", font1));
            parrafoArribaDer.add(new Phrase("  " + nombrePoblacion + ", a " + fechaEntrada + "                          \n", font3));

            parrafoArribaDer.setAlignment(Element.ALIGN_RIGHT);

            //PARRAFO DATOS INQUILINO 
            Paragraph parrafDatosInquilino = new Paragraph();
            parrafDatosInquilino.add(new Phrase("\n \nQR Localización \n \n D./Dña. " + usu.getNombre() + " " + usu.getApellido1() + " " + usu.getApellido2() + ", con DNI número " + usu.getNif() + ", y con móvil de contacto " + usu.getTelefono() + ", con"
                    + " domicilio en " + poblacionUsuario + " (" + provinciaViajero.getNombreProvincia() + "), alquila por temporada el alojamiento detallado a continuación con las siguientes condiciones: \n \n", font2));

            //TABLA DE CONDICIONES 
            PdfPTable table = new PdfPTable(2); // 2 columns.

            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);//celdas sin bordes
            table.setHorizontalAlignment(Element.ALIGN_LEFT);//alineamos la tabla a la izquierda
            table.setWidthPercentage(100);//porcentaje que ocupa la tabla

            Phrase p1 = new Phrase("Alojamiento ", font2);
            p1.add(new Chunk(alojamiento.getNombre(), font3));
            Phrase p2 = new Phrase("Localidad ", font2);
            p2.add(new Chunk(nombrePoblacion + " (" + provinciaAlojamiento.getNombreProvincia() + ")", font3));
            Phrase p3 = new Phrase("Precio ", font2);
            p3.add(new Chunk(" #" + String.valueOf(precioConComision) + "€", font3));
            Phrase p4 = new Phrase("Anticipo abonado ", font2);
            p4.add(new Chunk(" #" + String.valueOf(anticipo) + "€", font3));
            Phrase p5 = new Phrase("Comisión abonada ", font2);
            p5.add(new Chunk(" #" + String.valueOf(comision) + "€", font3));
            Phrase p6 = new Phrase("Cantidad pendiente de pago ", font2);
            float pendientePagar = precioConComision - anticipo;
            p6.add(new Chunk(" #" + String.valueOf(pendientePagar) + "€", font3));
            Phrase p7 = new Phrase(" La entrada es el ", font2);
            p7.add(new Chunk(fechaEntrada, font3));
            Phrase p8 = new Phrase(" Hora de entrada ", font2);
            p8.add(new Chunk(cond.getG(), font3));
            Phrase p9 = new Phrase(" La salida es el ", font2);
            p9.add(new Chunk(fechaSalida, font3));
            Phrase p10 = new Phrase(" Hora de salida ", font2);
            p10.add(new Chunk(cond.getH(), font3));

            PdfPCell cell1 = new PdfPCell(p1);
            PdfPCell cell2 = new PdfPCell(p2);
            PdfPCell cell3 = new PdfPCell(p3);
            PdfPCell cell4 = new PdfPCell(p4);
            PdfPCell cell5 = new PdfPCell(p5);
            PdfPCell cell6 = new PdfPCell(p6);
            PdfPCell cell7 = new PdfPCell(p7);
            PdfPCell cell8 = new PdfPCell(p8);
            PdfPCell cell9 = new PdfPCell(p9);
            PdfPCell cell10 = new PdfPCell(p10);

            cell1.setPadding(5);
            cell2.setPadding(5);
            cell3.setPadding(5);
            cell4.setPadding(5);
            cell5.setPadding(5);
            cell6.setPadding(5);
            cell7.setPadding(5);
            cell8.setPadding(5);
            cell9.setPadding(5);
            cell10.setPadding(5);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
            table.addCell(cell7);
            table.addCell(cell8);
            table.addCell(cell9);
            table.addCell(cell10);

            // PERSONA DE CONTACTO
            Paragraph personaCtcCondiciones = new Paragraph();

            personaCtcCondiciones.add(new Phrase("La persona de contacto para la recogida de llaves es " + propietario.getNombre() + " " + propietario.getApellido1() + " y su teléfono es " + propietario.getTelefono() + " \n \n", font4));
            personaCtcCondiciones.add(new Phrase("Se ha acordado concertar el arrendamiento por temporada de la vivienda con las siguientes estipulaciones: \n \n", font3));
            personaCtcCondiciones.add(new Phrase("PRIMERA. - El domicilio del inquilino es el que se indica en la parte primera del contrato, por lo que arrienda la vivienda"
                    + " por motivo vacacional, y bajo ningún concepto y situación que se produzca, el arrendamiento del objeto de este contrato"
                    + " podrá usarlo de forma habitual. \n \n"
                    + "SEGUNDA. - La parte arrendataria declara conocer las características y estado de conservación del inmueble y aceptarlas"
                    + " expresamente y se obliga a conservarla en perfecto estado durante el plazo de duración libremente pactado entre ambas"
                    + " partes, siendo de su cargo los deterioros o pérdidas que se produzcan en la misma, ya sean causados por la parte"
                    + " arrendataria o por las personas que convivan con él en la vivienda.\n \n"
                    + "TERCERA. - La parte arrendataria tiene la obligación de respetar la capacidad contratada de la casa siendo en este caso"
                    + " concreto el alquiler para " + numeroPersonasAloj + " personas, siendo " + alojamiento.getPlazasMax() + " su capacidad máxima, su incumplimiento puede dar lugar al desalojo"
                    + " total de las personas que estén en la vivienda y sin derecho a devolución económica de ninguna clase.\n \n"
                    + "CUARTA. - El presente contrato quedará automáticamente resuelto el " + fechaSalida + " sin necesidad de"
                    + " previo aviso.\n \n"
                    + "Ambas partes se ratifican en el presente contrato y firman por duplicado, a un solo efecto, en el lugar y fecha indicados en"
                    + " el encabezamiento.\n \n", font5));

            //FIRMA DEL ARRENDATARIO Y DEL ARRENDADOR
            Paragraph firmaPropViaj = new Paragraph();
            firmaPropViaj.add(new Phrase("Propietario o persona que gestiona la casa                                                        Inquilino/a \n ", font3));
            firmaPropViaj.add(new Phrase("          " + propietario.getNombre() + " " + propietario.getApellido1() + " " + propietario.getApellido2() + "                                                        " + usu.getNombre() + " " + usu.getApellido1() + " " + usu.getApellido2() + " \n ", font3));

            //FINAL DEL CONTRATO.PDF
            Paragraph finalPdf = new Paragraph();
            finalPdf.add(new Phrase("\n* Debe acompañar una fotocopia del DNI. Imprima y firme esta copia del contrato*", font6));
            finalPdf.setAlignment(Element.ALIGN_CENTER);

            try {
                document.add(qr_image);
                document.add(parrafoArribaDer);
                document.add(parrafDatosInquilino);
                //AÑADO TABLA FORMATEADA
                document.add(table);
                document.add(personaCtcCondiciones);
                document.add(firmaPropViaj);
                document.add(finalPdf);
                document.close();
            } catch (DocumentException ex) {
                System.out.println(ex.getMessage());
            }
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control",
                    "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            // setting the content type
            response.setContentType("application/pdf");
            // the contentlength
            response.setContentLength(baos.size());
            // write ByteArrayOutputStream to the ServletOutputStream
            try {
                OutputStream os = response.getOutputStream();
                baos.writeTo(os);
                os.flush();
                os.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            context.responseComplete();

        } catch (BadElementException ex) {
            Logger.getLogger(bkReserva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarReservaEmail() {

        try {
            // Usuario y el password
            String usuario = "ruralhousesinfo@gmail.com";
            String pass = "ruralhousesinfo00";

            Properties props = new Properties();
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // Creamos una sesión que nos permita identificarnos en el servidor con el usuario y pass informados previamente
            Authenticator autenticador = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(usuario, pass);
                }
            };

            Session session = Session.getInstance(props, autenticador);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(usu.getEmail()));
            message.setSubject("Documento de reserva");

            //3) create MimeBodyPart object and set your message text        
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("Le enviamos el documento de reserva en formato de PDF");

            //4) create new MimeBodyPart object and set DataHandler object to this object        
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            String filename = path + "/../../web/images/Other/reservilla.pdf";
            DataSource source = new FileDataSource(filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName("reserva.pdf");

            //5) create Multipart object and add MimeBodyPart objects to this object        
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);

            //6) set the multiplart object to the message object    
            message.setContent(multipart);

            // Una vez creado el objeto Message con el email, se realiza en envío. En caso de fallo elevará una excepción
            Transport.send(message);

        } catch (MessagingException ex) {
            Logger.getLogger(BkLogin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void marcarFavorito(String registroTurismo) {
        usu = getUsu();
        Alojamiento esteAloj = controlAlojamientos.findAlojamiento(registroTurismo);
        Favoritos nuevoFavorito = new Favoritos(null, usu, esteAloj);
        controlFavoritos.create(nuevoFavorito);

    }

    public void desmarcarFavorito(String registroTurismo) {
        try {
            usu = getUsu();
            Alojamiento esteAloj = controlAlojamientos.findAlojamiento(registroTurismo);
            Favoritos esFavorito = controlFavoritos.findByNifAloj(usu, esteAloj);

            controlFavoritos.destroy(esFavorito.getCodigoFav());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(bkSearchEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean comprobarFavorito(String registroTurismo) {
        boolean devolver;

        usu = getUsu();
        Alojamiento esteAloj = controlAlojamientos.findAlojamiento(registroTurismo);
        Favoritos esFavorito = controlFavoritos.findByNifAloj(usu, esteAloj);

        if (esFavorito != null) {
            devolver = true;
        } else {
            devolver = false;
        }

        return devolver;
    }

}
