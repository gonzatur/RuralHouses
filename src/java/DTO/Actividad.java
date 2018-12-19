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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gonza
 */
@Entity
@Table(name = "actividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a")
    , @NamedQuery(name = "Actividad.findByCodA", query = "SELECT a FROM Actividad a WHERE a.codA = :codA")
    , @NamedQuery(name = "Actividad.findByCodAlojamiento", query = "SELECT a FROM Actividad a WHERE a.registroTurismo = :registroTurismo")
})
public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codA")
    private Integer codA;
    @JoinColumn(name = "codigoActividad", referencedColumnName = "codigoActividad")
    @ManyToOne(optional = false)
    private Actividades codigoActividad;
    @JoinColumn(name = "registroTurismo", referencedColumnName = "registroTurismo")
    @ManyToOne(optional = false)
    private Alojamiento registroTurismo;

    public Actividad() {
    }

    public Actividad(Integer codA, Actividades codigoActividad, Alojamiento registroTurismo) {
        this.codA = codA;
        this.codigoActividad = codigoActividad;
        this.registroTurismo = registroTurismo;
    }
    
    public Actividad(Integer codA) {
        this.codA = codA;
    }

    public Integer getCodA() {
        return codA;
    }

    public void setCodA(Integer codA) {
        this.codA = codA;
    }

    public Actividades getCodigoActividad() {
        return codigoActividad;
    }

    public void setCodigoActividad(Actividades codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public Alojamiento getRegistroTurismo() {
        return registroTurismo;
    }

    public void setRegistroTurismo(Alojamiento registroTurismo) {
        this.registroTurismo = registroTurismo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codA != null ? codA.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        if ((this.codA == null && other.codA != null) || (this.codA != null && !this.codA.equals(other.codA))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Actividad[ codA=" + codA + " ]";
    }
    
}
