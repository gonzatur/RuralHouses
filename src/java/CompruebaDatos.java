
import DAO.UsuarioJpaController;
import DTO.Usuario;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gonza
 */
public class CompruebaDatos extends HttpServlet {

    public void proceso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        //boolean acceso = false;
        String mail = request.getParameter("mail");

        //----------------------------------------------------------**/
        

        if (mail != null) {
            JsonObject object = new JsonObject();
            object.addProperty("respuesta", compruebaEmail(mail));
            String jsonS = object.toString();
            out.println(jsonS);
            out.flush();
            System.out.println("heeey!");
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

    public boolean compruebaEmail(String email) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("RuralHousesPU");
        UsuarioJpaController controlUsuario = new UsuarioJpaController(emf);

        Usuario usuFind = controlUsuario.findByEmail(email);

        //Si es distinto a nulo, significa que ya existe alguien
        if (usuFind == null) {
            return false;
        } else {
            return true;
        }
    }
}
