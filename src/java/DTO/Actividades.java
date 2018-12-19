/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gonza
 */
@Entity
@Table(name = "actividades")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividades.findAll", query = "SELECT a FROM Actividades a")
    , @NamedQuery(name = "Actividades.findByCodigoActividad", query = "SELECT a FROM Actividades a WHERE a.codigoActividad = :codigoActividad")
    , @NamedQuery(name = "Actividades.findByNombreActividad", query = "SELECT a FROM Actividades a WHERE a.nombreActividad = :nombreActividad")})
public class Actividades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigoActividad")
    private Integer codigoActividad;
    @Basic(optional = false)
    @Column(name = "nombreActividad")
    private String nombreActividad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoActividad")
    private List<Actividad> actividadList;

    public Actividades() {
    }

    public Actividades(Integer codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public Actividades(Integer codigoActividad, String nombreActividad) {
        this.codigoActividad = codigoActividad;
        this.nombreActividad = nombreActividad;
    }

    public Integer getCodigoActividad() {
        return codigoActividad;
    }

    public void setCodigoActividad(Integer codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    @XmlTransient
    public List<Actividad> getActividadList() {
        return actividadList;
    }

    public void setActividadList(List<Actividad> actividadList) {
        this.actividadList = actividadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoActividad != null ? codigoActividad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividades)) {
            return false;
        }
        Actividades other = (Actividades) object;
        if ((this.codigoActividad == null && other.codigoActividad != null) || (this.codigoActividad != null && !this.codigoActividad.equals(other.codigoActividad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Actividades[ codigoActividad=" + codigoActividad + " ]";
    }
    
}
