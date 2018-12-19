/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backed;

import DTO.Usuario;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Lola
 */
@ManagedBean(name = "bSesion")
@SessionScoped
public class BkManageBeanSesion {

    private Usuario usuarioLog;
    private boolean log;
    private String perfil;
    
    public BkManageBeanSesion() {
    }

    public Usuario getUsuarioLog() {
        return usuarioLog;
    }

    public void setUsuarioLog(Usuario usuarioLog) {
        this.usuarioLog = usuarioLog;
    }

    public boolean isLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    
    public static void redireccionar(String url) {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ctx.redirect(url);
        } catch (Exception ex) {
        }
    }

    public static void logout(String surl) {

        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ((HttpSession) ctx.getSession(false)).invalidate();
            ctx.redirect(surl);
        } catch (Exception ex) {
        }
    }
}
