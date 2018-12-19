
import Backed.BkManageBeanSesion;
import DAO.AlojamientoJpaController;
import DAO.OcupacionJpaController;
import DAO.PoblacionJpaController;
import DAO.UsuarioJpaController;
import DAO.exceptions.NonexistentEntityException;
import DTO.Alojamiento;
import DTO.Ocupacion;
import DTO.OcupacionPK;
import DTO.Poblacion;
import DTO.Usuario;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gonza
 */
public class FechasReserva extends HttpServlet {

    public void proceso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        //boolean acceso = false;
        Alojamiento aloj = null;

        String codigoAlojamiento = request.getParameter("codigoAlojamiento");
        String fechaReservaBorrar = request.getParameter("miReservaBorrar");
        String fechaReservaInsert = request.getParameter("miReservaInsert");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("RuralHousesPU");
        OcupacionJpaController controlOcupacion = new OcupacionJpaController(emf);
        AlojamientoJpaController controlAlojamiento = new AlojamientoJpaController(emf);
        if (codigoAlojamiento != null) {
            try {
                //Leemos el objeto JSON y almacenamos su valor en la variable de email
                aloj = obtenerAlojamiento(codigoAlojamiento);

                //Preparamos la salida del objeto JSON
                JsonObject object = new JsonObject();

                List listadoOcupacion = controlOcupacion.findByOcupacion(aloj.getRegistroTurismo());
                List resultadosReservas = new ArrayList();
                List resultadosPropios = new ArrayList();
                for (Object obj : listadoOcupacion) {
                    Ocupacion estaOcupacion = (Ocupacion) obj;

                    int tipo = estaOcupacion.getTipo();

                    if (tipo == 0) {

                        Date ocupado = estaOcupacion.getOcupacionPK().getNoche();
                        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                        String today = formatter.format(ocupado);
                        resultadosReservas.add(today);
                    } else {
                        Date ocupado = estaOcupacion.getOcupacionPK().getNoche();
                        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                        String today = formatter.format(ocupado);
                        resultadosPropios.add(today);

                    }

                }
                object.addProperty("resultadosReservas", resultadosReservas.toString());
                object.addProperty("resultadosPropios", resultadosPropios.toString());

                String jsonS = object.toString();
                out.println(jsonS);

                //----------------------------------------------------------**/
                out.flush();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (fechaReservaBorrar != null) {
            try {
                String[] partes = fechaReservaBorrar.split(",");

                String fechaFormato = partes[0];
                String codigoAloj = partes[1];

                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("MM/dd/yyyy");

                Date fechaDate = formatoDelTexto.parse(fechaFormato);
                System.out.println(fechaDate);
                OcupacionPK newOcu = new OcupacionPK(codigoAloj, fechaDate);
                controlOcupacion.destroy(newOcu);
            } catch (ParseException | NonexistentEntityException ex) {
                Logger.getLogger(FechasReserva.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (fechaReservaInsert != null) {

            try {
                String[] partes = fechaReservaInsert.split(",");
                
                String fechaFormato = partes[0];
                String codigoAloj = partes[1];
                
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("MM/dd/yyyy");
                
                Date fechaDate = formatoDelTexto.parse(fechaFormato);
                System.out.println(fechaDate);
                OcupacionPK newOcu = new OcupacionPK(codigoAloj, fechaDate);
                Alojamiento alojamiento = controlAlojamiento.findAlojamiento(codigoAloj);
                Ocupacion nuevaOcupacion = new Ocupacion(codigoAloj, fechaDate, 1, alojamiento);
                controlOcupacion.create(nuevaOcupacion);
            } catch (ParseException ex) {
                Logger.getLogger(FechasReserva.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(FechasReserva.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        proceso(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        proceso(req, res);
    }

    public Alojamiento obtenerAlojamiento(String codigoAlojamiento) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("RuralHousesPU");
        AlojamientoJpaController controlAlojamiento = new AlojamientoJpaController(emf);

        Alojamiento aloj = controlAlojamiento.findAlojamiento(codigoAlojamiento);

        if (aloj == null) {
            return null;
        } else {
            return aloj;
        }
    }
}
