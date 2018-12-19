/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gonza
 */
@Entity
@Table(name = "comunidades")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comunidades.findAll", query = "SELECT c FROM Comunidades c")
    , @NamedQuery(name = "Comunidades.findByCodigoComunidad", query = "SELECT c FROM Comunidades c WHERE c.codigoComunidad = :codigoComunidad")
    , @NamedQuery(name = "Comunidades.findByNombreComunidad", query = "SELECT c FROM Comunidades c WHERE c.nombreComunidad = :nombreComunidad")})
public class Comunidades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigoComunidad")
    private Integer codigoComunidad;
    @Basic(optional = false)
    @Column(name = "nombreComunidad")
    private String nombreComunidad;

    public Comunidades() {
    }

    public Comunidades(Integer codigoComunidad) {
        this.codigoComunidad = codigoComunidad;
    }

    public Comunidades(Integer codigoComunidad, String nombreComunidad) {
        this.codigoComunidad = codigoComunidad;
        this.nombreComunidad = nombreComunidad;
    }

    public Integer getCodigoComunidad() {
        return codigoComunidad;
    }

    public void setCodigoComunidad(Integer codigoComunidad) {
        this.codigoComunidad = codigoComunidad;
    }

    public String getNombreComunidad() {
        return nombreComunidad;
    }

    public void setNombreComunidad(String nombreComunidad) {
        this.nombreComunidad = nombreComunidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoComunidad != null ? codigoComunidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comunidades)) {
            return false;
        }
        Comunidades other = (Comunidades) object;
        if ((this.codigoComunidad == null && other.codigoComunidad != null) || (this.codigoComunidad != null && !this.codigoComunidad.equals(other.codigoComunidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Comunidades[ codigoComunidad=" + codigoComunidad + " ]";
    }
    
}
