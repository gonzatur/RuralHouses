package Backed;

import DAO.UsuarioJpaController;
import DAO.exceptions.NonexistentEntityException;
import DTO.Usuario;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;

public class BkLogin {

    private EntityManagerFactory emf;
    private UsuarioJpaController controlUsuarios;
    private String email;
    private String password;
    private String destinatario;

    private Usuario userLoged;
    private String mens;
    private String mensPass;

    public BkLogin() {
        emf = Persistence.createEntityManagerFactory("RuralHousesPU");
        controlUsuarios = new UsuarioJpaController(emf);
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public UsuarioJpaController getControlUsuarios() {
        return controlUsuarios;
    }

    public void setControlUsuarios(UsuarioJpaController controlUsuarios) {
        this.controlUsuarios = controlUsuarios;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario getUserLoged() {
        return userLoged;
    }

    public void setUserLoged(Usuario userLoged) {
        this.userLoged = userLoged;
    }

    public String getMens() {
        return mens;
    }

    public void setMens(String mens) {
        this.mens = mens;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getMensPass() {
        return mensPass;
    }

    public void setMensPass(String mensPass) {
        this.mensPass = mensPass;
    }

    //Metodo para comprobar el logeo
    public String compruebaLogin() {
        mens = "";

        Usuario user = controlUsuarios.findByEmail(email);
        if (user != null && user.getContraseña().equals(password)) {
            userLoged = user;
            subirJugador();
            if (user.getPerfil().equals("administrador")) {
                ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
                try {
                    ctx.redirect("/RuralHouses/faces/administrador.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(bkSearchEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (user.getPerfil().equals("propietario")) {
            }
        } else {
            mens = "Ha introducido datos incorrectos";
            return "error";
        }
        return mens;
    }

    public void subirJugador() {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();

        BkManageBeanSesion BkManageBeanSesion = new BkManageBeanSesion();

        HttpSession session = (HttpSession) ctx.getSession(false);

        if (session.getAttribute("BkManageBeanSesion") != null) {
            BkManageBeanSesion = (BkManageBeanSesion) session.getAttribute("BkManageBeanSesion");
        } else {
            session.setAttribute("BkManageBeanSesion", BkManageBeanSesion);
            PrimeFaces.current().executeScript("$('.modal').css('display','none')");
            PrimeFaces.current().executeScript("$('#user button').css('display','none')");
            PrimeFaces.current().executeScript("$('#user input').css('display','block')");
        }
        //Añadirle como propiedad el cliente que se ha logeado
        BkManageBeanSesion.setUsuarioLog(userLoged);
        BkManageBeanSesion.setLog(true);
        BkManageBeanSesion.setPerfil(userLoged.getPerfil());

    }

    public String rememberPassword() {

        Usuario usu = controlUsuarios.findByEmail(destinatario);

        if (usu != null) {
            String contraseñaAleatoria = "";
            //generacion contraseña Aleatoria
            char[] elementos = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',//NUMBERS
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',//MINUS
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',//MAYUS
                '!', '?', '*', '-', '.'};//SYMBOLS
            int longitud = elementos.length;
            char[] conjunto = new char[8];

            for (int i = 0; i < 8; i++) {
                int el = (int) (Math.random() * longitud);
                conjunto[i] = (char) elementos[el];
            }
            contraseñaAleatoria = new String(conjunto);
            try {
                // Usuario y el password
                String usuario = "ruralhousesinfo@gmail.com";
                String pass = "ruralhousesinfo00";

                String asunto = "Se ha modificado su contraseña";
                String mensaje = "Hola " + usu.getNombre() + " le enviamos una nueva contraseña, podrá cambiarla o mantenerla! "
                        + "\n \n " + contraseñaAleatoria
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
                InternetAddress[] listaDestinatarios = {new InternetAddress(destinatario)};
                correo.setRecipients(Message.RecipientType.TO, listaDestinatarios);

                // Asunto
                correo.setSubject(asunto);

                // Paso 4. Informamos la fecha de hoy
                correo.setSentDate(new Date());

                // Mensaje que queremos enviar
                correo.setText(mensaje);

                // Una vez creado el objeto Message con el email, se realiza en envío. En caso de fallo elevará una excepción
                Transport.send(correo);

                usu.setContraseña(contraseñaAleatoria);
                try {
                    controlUsuarios.edit(usu);
                    mensPass = "Le hemos enviado una nueva contraseña a su correo electrónico";
                    // Si hemos llegado a este punto significa que no se ha lanzado ninguna excepción y podemos decir que el email se ha enviado correctamente
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(BkLogin.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(BkLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (MessagingException ex) {
                Logger.getLogger(BkLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            mensPass = "Introduzca un e-mail válido";
        }
        destinatario = "";
        return mensPass;
    }

    public boolean comprobarRender(Boolean usu) {
        if (usu == null) {
            return true;
        }
        if (usu == true) {
            return false;
        } else {
            return false;
        }

    }

    public String comprobarPerfil(String perfil) {
        if (perfil.equals("propietario")) {
            return "propietario";
        } else if (perfil.equals("viajero")) {
            return "viajero";
        } else {
            return "";
        }
    }

}
