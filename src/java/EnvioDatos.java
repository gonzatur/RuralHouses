
import Backed.BkManageBeanSesion;
import DAO.AlojamientoJpaController;
import DAO.PoblacionJpaController;
import DAO.UsuarioJpaController;
import DAO.exceptions.NonexistentEntityException;
import DTO.Alojamiento;
import DTO.Poblacion;
import DTO.Usuario;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
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
public class EnvioDatos extends HttpServlet {

    public void proceso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        //boolean acceso = false;
        Alojamiento aloj = null;

        String codigoAlojamiento = request.getParameter("codigoAlojamiento");
        if (codigoAlojamiento != null) {
            try {
                //Leemos el objeto JSON y almacenamos su valor en la variable de email
                aloj = obtenerAlojamiento(codigoAlojamiento);

                //Preparamos la salida del objeto JSON
                JsonObject object = new JsonObject();
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("RuralHousesPU");
                PoblacionJpaController controlPoblacion = new PoblacionJpaController(emf);
                
                if (aloj.getLatitud() > 0 || aloj.getLongitud() > 0) {
                    object.addProperty("Latitud", aloj.getLatitud());
                    object.addProperty("Longitud", aloj.getLongitud());
                } else {
                    Poblacion pob = controlPoblacion.findPoblacion(aloj.getCodigoPoblacion());
                    object.addProperty("Latitud", pob.getLatitud());
                    object.addProperty("Longitud", pob.getLongitud());
                }

                String jsonS = object.toString();
                out.println(jsonS);

                //----------------------------------------------------------**/
                out.flush();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        String coordenadas = request.getParameter("coordenadas");

        System.out.println(coordenadas);
        System.out.println("hola");
        if (coordenadas != null) {
            try {
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("RuralHousesPU");
                AlojamientoJpaController controlAlojamiento = new AlojamientoJpaController(emf);
                String[] splitCoord = coordenadas.split(",");
                String longitud = splitCoord[0];
                String latitud = splitCoord[1];
                String codigoAloj = splitCoord[2];

                Alojamiento modificarAloj = controlAlojamiento.findAlojamiento(codigoAloj);
                float floatLong = Float.parseFloat(longitud);
                float floatLat = Float.parseFloat(latitud);
                modificarAloj.setLongitud(floatLong);
                modificarAloj.setLatitud(floatLat);

                controlAlojamiento.edit(modificarAloj);

                //OBTENER USUARIO
                UsuarioJpaController controlUsu = new UsuarioJpaController(emf);

                Usuario usu = controlUsu.findUsuario(modificarAloj.getNif().getNif());

                ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
                //Crear un objeto bTienda para añadirle despues el cliente como atributo
                BkManageBeanSesion BkManageBeanSesion = new BkManageBeanSesion();
                //Coger session del contexto
                HttpSession session = (HttpSession) ctx.getSession(false);
                
                if (session.getAttribute("BkManageBeanSesion") != null) {
                    BkManageBeanSesion = (BkManageBeanSesion) session.getAttribute("BkManageBeanSesion");
                } else {
                    session.setAttribute("BkManageBeanSesion", BkManageBeanSesion);
                }
                BkManageBeanSesion.setUsuarioLog(usu);

            } catch (NonexistentEntityException ex) {
                Logger.getLogger(EnvioDatos.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(EnvioDatos.class.getName()).log(Level.SEVERE, null, ex);
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
