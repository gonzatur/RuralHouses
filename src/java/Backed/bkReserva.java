/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backed;

import DAO.AlojamientoJpaController;
import DAO.FavoritosJpaController;
import DAO.OpinionJpaController;
import DAO.PoblacionJpaController;
import DAO.ProvinciaJpaController;
import DAO.ReservaJpaController;
import DAO.UsuarioJpaController;
import DAO.exceptions.NonexistentEntityException;
import DTO.Alojamiento;
import DTO.Condicionesalojamiento;
import DTO.Favoritos;
import DTO.Opinion;
import DTO.Poblacion;
import DTO.Provincia;
import DTO.Reserva;
import DTO.Usuario;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.html.HtmlInputHidden;
import javax.servlet.http.HttpSession;

//---
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
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.FileUploadEvent;

import org.primefaces.model.UploadedFile;

/**
 *
 * @author gonza
 */
public class bkReserva {

    private EntityManagerFactory emf;
    private Alojamiento alojamiento;
    private AlojamientoJpaController controlAlojamientos;
    private ReservaJpaController controlReservas;
    private OpinionJpaController controlOpinion;
    private FavoritosJpaController controlFavoritos;
    private PoblacionJpaController controlPoblacion;
    private UsuarioJpaController controlUsuarios;
    private ProvinciaJpaController controlProvincias;
    private Usuario viajero;
    private ArrayList listadoReservas;
    private Reserva reserva;
    private HtmlInputHidden reservaHidden;

    //DESGLOSE FECHA Y PRECIOS
    private String strFechaReserva;
    private float precioPersonas;
    private float pagoTarjeta;
    private float anticipoPagado;
    private float pendientePagar;
    private float precioTotalReserva;
    //OBJETOS PARA MOSTRAR ALOJAMIENTOS FAVORITOS
    private ArrayList listadoFavoritos;

    //Datos para la modificacion del usuario
    private String modificarEmail;
    private String modificarContraseña;
    private String modificarTelefono;

    //Datos para dar una opinion
    private int limpieza;
    private int confort;
    private int ubicacion;
    private int calidadPrecio;
    private int tratoRecibido;
    private String comentarioOpinion;

    public bkReserva() {
        emf = Persistence.createEntityManagerFactory("RuralHousesPU");
        controlAlojamientos = new AlojamientoJpaController(emf);
        controlReservas = new ReservaJpaController(emf);
        controlOpinion = new OpinionJpaController(emf);
        controlFavoritos = new FavoritosJpaController(emf);
        controlPoblacion = new PoblacionJpaController(emf);
        controlUsuarios = new UsuarioJpaController(emf);
        controlProvincias = new ProvinciaJpaController(emf);
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

    public ArrayList getListadoReservas() {
        obtenerReservas();
        return listadoReservas;
    }

    public void setListadoReservas(ArrayList listadoReservas) {
        this.listadoReservas = listadoReservas;
    }

    public ReservaJpaController getControlReservas() {
        return controlReservas;
    }

    public void setControlReservas(ReservaJpaController controlReservas) {
        this.controlReservas = controlReservas;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public HtmlInputHidden getReservaHidden() {
        return reservaHidden;
    }

    public void setReservaHidden(HtmlInputHidden reservaHidden) {
        this.reservaHidden = reservaHidden;
    }

    public String getStrFechaReserva() {
        return strFechaReserva;
    }

    public void setStrFechaReserva(String strFechaReserva) {
        this.strFechaReserva = strFechaReserva;
    }

    public float getPrecioPersonas() {
        return precioPersonas;
    }

    public void setPrecioPersonas(float precioPersonas) {
        this.precioPersonas = precioPersonas;
    }

    public float getPagoTarjeta() {
        return pagoTarjeta;
    }

    public void setPagoTarjeta(float pagoTarjeta) {
        this.pagoTarjeta = pagoTarjeta;
    }

    public float getAnticipoPagado() {
        return anticipoPagado;
    }

    public void setAnticipoPagado(float anticipoPagado) {
        this.anticipoPagado = anticipoPagado;
    }

    public float getPendientePagar() {
        return pendientePagar;
    }

    public void setPendientePagar(float pendientePagar) {
        this.pendientePagar = pendientePagar;
    }

    public float getPrecioTotalReserva() {
        return precioTotalReserva;
    }

    public void setPrecioTotalReserva(float precioTotalReserva) {
        this.precioTotalReserva = precioTotalReserva;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public FavoritosJpaController getControlFavoritos() {
        return controlFavoritos;
    }

    public void setControlFavoritos(FavoritosJpaController controlFavoritos) {
        this.controlFavoritos = controlFavoritos;
    }

    public PoblacionJpaController getControlPoblacion() {
        return controlPoblacion;
    }

    public void setControlPoblacion(PoblacionJpaController controlPoblacion) {
        this.controlPoblacion = controlPoblacion;
    }

    public UsuarioJpaController getControlUsuarios() {
        return controlUsuarios;
    }

    public void setControlUsuarios(UsuarioJpaController controlUsuarios) {
        this.controlUsuarios = controlUsuarios;
    }

    public ProvinciaJpaController getControlProvincias() {
        return controlProvincias;
    }

    public void setControlProvincias(ProvinciaJpaController controlProvincias) {
        this.controlProvincias = controlProvincias;
    }

    public String getModificarEmail() {
        return modificarEmail;
    }

    public void setModificarEmail(String modificarEmail) {
        this.modificarEmail = modificarEmail;
    }

    public String getModificarContraseña() {
        return modificarContraseña;
    }

    public void setModificarContraseña(String modificarContraseña) {
        this.modificarContraseña = modificarContraseña;
    }

    public String getModificarTelefono() {
        return modificarTelefono;
    }

    public void setModificarTelefono(String modificarTelefono) {
        this.modificarTelefono = modificarTelefono;
    }

    //FAVORITOS------------------------------------------------------------------
    public ArrayList getListadoFavoritos() {
        obtenerFavoritos();
        return listadoFavoritos;
    }

    public void setListadoFavoritos(ArrayList listadoFavoritos) {
        this.listadoFavoritos = listadoFavoritos;
    }

    public int getLimpieza() {
        return limpieza;
    }

    public void setLimpieza(int limpieza) {
        this.limpieza = limpieza;
    }

    public int getConfort() {
        return confort;
    }

    public void setConfort(int confort) {
        this.confort = confort;
    }

    public int getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(int ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCalidadPrecio() {
        return calidadPrecio;
    }

    public void setCalidadPrecio(int calidadPrecio) {
        this.calidadPrecio = calidadPrecio;
    }

    public int getTratoRecibido() {
        return tratoRecibido;
    }

    public void setTratoRecibido(int tratoRecibido) {
        this.tratoRecibido = tratoRecibido;
    }

    public String getComentarioOpinion() {
        return comentarioOpinion;
    }

    public void setComentarioOpinion(String comentarioOpinion) {
        this.comentarioOpinion = comentarioOpinion;
    }
    
    //METODOS
    public void obtenerReservas() {
        listadoReservas = null;
        if (listadoReservas == null) {
            listadoReservas = new ArrayList();
            List<Reserva> listaReservas = controlReservas.findByNif(viajero);
            for (Reserva resv : listaReservas) {
                listadoReservas.add(resv);
            }
        }
    }

    public void obtenerFavoritos() {
        listadoFavoritos = null;
        if (listadoFavoritos == null) {
            listadoFavoritos = new ArrayList();
            List<Favoritos> listadoFavo = controlFavoritos.findByNif(viajero);
            for (Favoritos fav : listadoFavo) {
                listadoFavoritos.add(fav);
            }
        }
    }

    public int puntuacionMediaAloj(String registroTurismo) {
        Alojamiento aloj = controlAlojamientos.findAlojamiento(registroTurismo);
        List<Opinion> listadoOpiniones = aloj.getOpinionList();
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

    public void mostrarDatosReserva() {
        //OBTENER RESERVA QUE QUEREMOS MOSTRAR
        String codReserva = reservaHidden.getValue().toString();

        reserva = controlReservas.findReserva(codReserva);

        //OBTENER FECHAS DE ENTRADA Y SALIDA CON FORMATO STRING
        Date fecEnt = reserva.getFechaEntrada();
        Date fecSal = reserva.getFechaSalida();
        String[] monthNames = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};

        float mili = fecSal.getTime() - fecEnt.getTime();
        float daysBetween = mili / (24 * 60 * 60 * 1000);
        int daysInt = (int) daysBetween;
        int anioEnt = fecEnt.getYear() + 1900;
        int anioSal = fecSal.getYear() + 1900;
        strFechaReserva = fecEnt.getDate() + " de " + monthNames[fecEnt.getMonth()] + " del " + anioEnt + " - "
                + fecSal.getDate() + " de " + monthNames[fecSal.getMonth()] + " del " + anioSal
                + " (" + daysInt + " noches)";

        //OBTENER PRECIOS DESGLOSADOS
        precioPersonas = reserva.getRegistroTurismo().getPrecioNoche() * reserva.getNumeroPersonas() * daysInt;
        float comision = reserva.getComisionPagada();
        precioTotalReserva = reserva.getPrecioTotal();
        float anticipoRequerido = reserva.getRegistroTurismo().getCondicionesalojamientoList().get(0).getD();
        float i = 100;
        float anticipoPercent = (anticipoRequerido / i);
        anticipoPagado = anticipoPercent * precioPersonas;
        pagoTarjeta = comision + anticipoPagado;
        pendientePagar = reserva.getPrecioTotal() - anticipoPagado;

        //SUBIMOS LA RESERVA A LA SESION POR SI EL CLIENTE QUIERE GENERAR EL PDF TENER TODOS LOS DATOS
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) ctx.getSession(false);
        session.setAttribute("reserva", reserva);
        session.setAttribute("viajero", viajero);

    }

    // GENERAR PDF
    public void generarPdf() {

        try {
            //DATOS NECESARIOS PARA COMPLETAR DINAMICAMENTE EL PDF
            /**
             * **************************************************
             */

            //FECHAS DE ENTRADA Y SALIDA
            String nombrePoblacion = controlPoblacion.findPoblacion(reserva.getRegistroTurismo().getCodigoPoblacion()).getNombrePoblacion();
            String[] fechaArray = strFechaReserva.split("-");
            String fechaEntrada = fechaArray[0];
            String fechaSalidaArray = fechaArray[1];
            String[] fechaSegParte = fechaSalidaArray.split("\\(");
            String fechaSalida = fechaSegParte[0];
            //POBLACION Y PROVINCIA 
            Poblacion pobla = controlPoblacion.findPoblacion(viajero.getCodigoPoblacion());
            String poblacionUsuario = pobla.getNombrePoblacion();
            Provincia provinciaViajero = controlProvincias.findProvincia(pobla.getCodigoProvincia());
            Provincia provinciaAlojamiento = controlProvincias.findProvincia(controlPoblacion.findPoblacion(reserva.getRegistroTurismo().getCodigoPoblacion()).getCodigoProvincia());
            //ALOJAMIENTO, CONDICIONES DEL MISMO Y PROPIETARIO
            Alojamiento aloj = controlAlojamientos.findAlojamiento(reserva.getRegistroTurismo().getRegistroTurismo());
            Condicionesalojamiento cond = aloj.getCondicionesalojamientoList().get(0);
            Usuario propietario = controlUsuarios.findByEmail(reserva.getRegistroTurismo().getNif().getEmail());

            //------------------------------------------------------------------
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline=filename=" + "hola" + ".pdf");
            Document document = new Document();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
                PdfWriter.getInstance(document, new FileOutputStream(path + "/../../web/images/Other/reserva.pdf"));
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
            BarcodeQRCode my_code = new BarcodeQRCode("https://maps.google.com/maps?q=" + aloj.getLongitud() + "," + aloj.getLatitud() + "", 140, 140, null);

            Image qr_image = my_code.getImage();
            qr_image.setAbsolutePosition(5f, 700f);

            //PARRAFO CONTRATO + LOCALIDAD + FECHA
            Paragraph parrafoArribaDer = new Paragraph();

            parrafoArribaDer.add(new Phrase("CONTRATO DE ARRENDAMIENTO POR TEMPORADA \n \n", font1));
            parrafoArribaDer.add(new Phrase("  " + nombrePoblacion + ", a " + fechaEntrada + "                          \n", font3));

            parrafoArribaDer.setAlignment(Element.ALIGN_RIGHT);

            //PARRAFO DATOS INQUILINO 
            Paragraph parrafDatosInquilino = new Paragraph();
            parrafDatosInquilino.add(new Phrase("\n \nQR Localización \n \n D./Dña. " + viajero.getNombre() + " " + viajero.getApellido1() + " " + viajero.getApellido2() + ", con DNI número " + viajero.getNif() + ", y con móvil de contacto " + viajero.getTelefono() + ", con"
                    + " domicilio en " + poblacionUsuario + " (" + provinciaViajero.getNombreProvincia() + "), alquila por temporada el alojamiento detallado a continuación con las siguientes condiciones: \n \n", font2));

            //TABLA DE CONDICIONES 
            PdfPTable table = new PdfPTable(2); // 2 columns.

            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);//celdas sin bordes
            table.setHorizontalAlignment(Element.ALIGN_LEFT);//alineamos la tabla a la izquierda
            table.setWidthPercentage(100);//porcentaje que ocupa la tabla

            Phrase p1 = new Phrase("Alojamiento ", font2);
            p1.add(new Chunk(aloj.getNombre(), font3));
            Phrase p2 = new Phrase("Localidad ", font2);
            p2.add(new Chunk(nombrePoblacion + " (" + provinciaAlojamiento.getNombreProvincia() + ")", font3));
            Phrase p3 = new Phrase("Precio ", font2);
            p3.add(new Chunk(" #" + String.valueOf(precioTotalReserva) + "€", font3));
            Phrase p4 = new Phrase("Anticipo abonado ", font2);
            p4.add(new Chunk(" #" + String.valueOf(anticipoPagado) + "€", font3));
            Phrase p5 = new Phrase("Comisión abonada ", font2);
            p5.add(new Chunk(" #" + String.valueOf(reserva.getComisionPagada()) + "€", font3));
            Phrase p6 = new Phrase("Cantidad pendiente de pago ", font2);
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
                    + " concreto el alquiler para " + reserva.getNumeroPersonas() + " personas, siendo " + aloj.getPlazasMax() + " su capacidad máxima, su incumplimiento puede dar lugar al desalojo"
                    + " total de las personas que estén en la vivienda y sin derecho a devolución económica de ninguna clase.\n \n"
                    + "CUARTA. - El presente contrato quedará automáticamente resuelto el" + fechaSalida + " sin necesidad de"
                    + " previo aviso.\n \n"
                    + "Ambas partes se ratifican en el presente contrato y firman por duplicado, a un solo efecto, en el lugar y fecha indicados en"
                    + " el encabezamiento.\n \n", font5));

            //FIRMA DEL ARRENDATARIO Y DEL ARRENDADOR
            Paragraph firmaPropViaj = new Paragraph();
            firmaPropViaj.add(new Phrase("Propietario o persona que gestiona la casa                                                        Inquilino/a \n ", font3));
            firmaPropViaj.add(new Phrase("          " + propietario.getNombre() + " " + propietario.getApellido1() + " " + propietario.getApellido2() + "                                                        " + viajero.getNombre() + " " + viajero.getApellido1() + " " + viajero.getApellido2() + " \n ", font3));

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

    public void cargarseReserva() {

        reserva = null;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ctx.redirect("/RuralHouses/faces/viajeroReserva.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(bkSearchEngine.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void cambiarEmail() {
        try {
            viajero.setEmail(modificarEmail);
            controlUsuarios.edit(viajero);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(bkReserva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(bkReserva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cambiarTelefono() {
        try {
            viajero.setTelefono(modificarTelefono);
            controlUsuarios.edit(viajero);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(bkReserva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(bkReserva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cambiarPassword() {

        try {
            viajero.setContraseña(modificarContraseña);
            controlUsuarios.edit(viajero);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(bkReserva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(bkReserva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void uploadPhoto(FileUploadEvent e) throws IOException {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");

        //VERIFICAR QUE LA CARPETA DEL ALOJAMIENTO ESTE CREADA
        File carpetaAlojamiento = new File(path + "/../../web/images/FotoPerfil/");

        UploadedFile uploadedPhoto = e.getFile();

        String extension = "";
        int i = uploadedPhoto.getFileName().lastIndexOf('.');
        if (i > 0) {
            extension = uploadedPhoto.getFileName().substring(i + 1);
        }

        byte[] bytes = null;

        if (null != uploadedPhoto) {
            try {
                bytes = uploadedPhoto.getContents();
                String filename = viajero.getNif() + "." + extension;
                viajero.setFotoPerfil(filename);
                controlUsuarios.edit(viajero);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(carpetaAlojamiento + "/" + filename)));
                stream.write(bytes);
                stream.close();
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(bkReserva.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(bkReserva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Tus Fotos fueron subidas Correctamente", ""));
    }

}
