package Backed;

import DAO.PoblacionJpaController;
import DAO.UsuarioJpaController;
import DTO.Poblacion;
import DTO.Usuario;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.model.UploadedFile;

public class BkRegister {

    private static final int BUFFER_SIZE = 6124;
    private EntityManagerFactory emf;
    private PoblacionJpaController controlPoblaciones;
    private UsuarioJpaController controlUsuarios;
    private String mens;
    private boolean propietario;
    private boolean viajero;
    private String email;
    private String password;
    private String passwordCertified;
    private String perfil;
    private ArrayList listadoCities;
    private int city;
    private String nif;
    private String nombre;
    private String apellidos;
    private String telefono;
    private UploadedFile file;

    public BkRegister() {
        emf = Persistence.createEntityManagerFactory("RuralHousesPU");
        controlPoblaciones = new PoblacionJpaController(emf);
        controlUsuarios = new UsuarioJpaController(emf);
        this.propietario = true;
    }

    public boolean isPropietario() {
        return propietario;
    }

    public void setPropietario(boolean propietario) {
        this.propietario = propietario;
    }

    public boolean isViajero() {
        return viajero;
    }

    public void setViajero(boolean viajero) {
        this.viajero = viajero;
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

    public String getPasswordCertified() {
        return passwordCertified;
    }

    public void setPasswordCertified(String passwordCertified) {
        this.passwordCertified = passwordCertified;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public ArrayList getListadoCities() {
        return listadoCities;
    }

    public void setListadoCities(ArrayList listadoCities) {
        this.listadoCities = listadoCities;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getMens() {
        return mens;
    }

    public void setMens(String mens) {
        this.mens = mens;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    //------------------------------------------------------------------
    public void onActiveStatusChange() {
        if (propietario) {
            viajero = false;
        }
    }

    public void onInactiveStatusChange() {
        if (viajero) {
            propietario = false;
        }
    }

    public String compruebaRegistro() {
        String retorno = "";
        if (!email.isEmpty()) {
            if (!password.isEmpty()) {
                if (password.equals(passwordCertified)) {
                    if (propietario) {
                        perfil = "propietario";
                    } else {
                        perfil = "viajero";
                    }
                    ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
                    try {
                        ctx.redirect("/RuralHouses/faces/registro.xhtml");
                    } catch (IOException ex) {
                        Logger.getLogger(BkRegister.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    mens = "Las contraseñas deben coincidir";
                    retorno = "mantenerReg";
                }
            } else {
                mens = "Introduzca una contraseña";
                retorno = "mantenerReg";
            }

        } else {
            mens = "Introduzca email";
            retorno = "mantenerReg";
        }
        return retorno;
    }

    public ArrayList getAllCities() {
        if (listadoCities == null) {
            listadoCities = new ArrayList();
            List<Poblacion> listaPob = controlPoblaciones.findPoblacionEntities();
            for (Poblacion pob : listaPob) {
                listadoCities.add(new SelectItem(pob.getCodigoPoblacion(), pob.getNombrePoblacion() + " - " + pob.getPostal()));
            }
        }
        return listadoCities;
    }

    public void terminarRegistro() {

        String[] apellidoSplit = apellidos.split("\\s+");
        String apellido1 = "";
        String apellido2 = "";

        if (apellidoSplit.length < 2) {
            apellido1 = apellidoSplit[0];
        } else if (apellidoSplit.length >= 2) {
            apellido1 = apellidoSplit[0];
            apellido2 = apellidoSplit[1];
        }

        if (!file.getFileName().isEmpty()) {
            try {
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
                //OBTENEMOS EXTENSION DEL FICHERO QUE NOS VIENE
                String extension = "";
                int i = file.getFileName().lastIndexOf('.');
                if (i > 0) {
                    extension = file.getFileName().substring(i + 1);
                }
                //CREAMOS EL FILE CON LA RUTA ENTERA
                File result = new File(path + "/../../web/images/FotoPerfil/" + nif + "." + extension);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(result);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bulk;
                    InputStream inputStream = file.getInputstream();
                    while (true) {
                        bulk = inputStream.read(buffer);
                        if (bulk < 0) {
                            break;
                        }
                        fileOutputStream.write(buffer, 0, bulk);
                        fileOutputStream.flush();
                    }
                    fileOutputStream.close();
                    inputStream.close();

                    FacesMessage msg = new FacesMessage("Succesful", file.getFileName()
                            + " is uploaded.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                } catch (IOException e) {
                    e.printStackTrace();

                    FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "The files were not uploaded!", "");
                    FacesContext.getCurrentInstance().addMessage(null, error);
                }
                String fotoPerfil = nif + "." + extension;
                Usuario nuevoUsuario = new Usuario(nif, nombre, apellido1, apellido2, city, email, password, telefono, perfil, fotoPerfil);

                controlUsuarios.create(nuevoUsuario);

            } catch (Exception ex) {
                Logger.getLogger(BkRegister.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                Usuario nuevoUsuario = new Usuario(nif, nombre, apellido1, apellido2, city, email, password, telefono, perfil, "");
                controlUsuarios.create(nuevoUsuario);
            } catch (Exception ex) {
                Logger.getLogger(BkRegister.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void validarNif() {

        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";

        if (nif != null && nif.length() == 8) {

            int nifNumber = Integer.parseInt(nif);
            int modulo = nifNumber % 23;
            char letraNif = letras.charAt(modulo);

            String nifString = String.valueOf(nifNumber);

            nif = nifString + letraNif;
        } else {

        }

    }

}
