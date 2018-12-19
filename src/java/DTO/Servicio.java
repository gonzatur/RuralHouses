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
@Table(name = "servicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Servicio.findAll", query = "SELECT s FROM Servicio s")
    , @NamedQuery(name = "Servicio.findByCodS", query = "SELECT s FROM Servicio s WHERE s.codS = :codS")
    , @NamedQuery(name = "Servicio.findByCodAlojamiento", query = "SELECT s FROM Servicio s WHERE s.registroTurismo = :registroTurismo")
})
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codS")
    private Integer codS;
    @JoinColumn(name = "registroTurismo", referencedColumnName = "registroTurismo")
    @ManyToOne(optional = false)
    private Alojamiento registroTurismo;
    @JoinColumn(name = "codigoServicio", referencedColumnName = "codigoServicio")
    @ManyToOne(optional = false)
    private Servicios codigoServicio;

    public Servicio() {
    }

    public Servicio(Integer codS, Servicios codigoServicio, Alojamiento registroTurismo) {
        this.codS = codS;
        this.codigoServicio = codigoServicio;
        this.registroTurismo = registroTurismo;
    }

    
    
    public Servicio(Integer codS) {
        this.codS = codS;
    }

    public Integer getCodS() {
        return codS;
    }

    public void setCodS(Integer codS) {
        this.codS = codS;
    }

    public Alojamiento getRegistroTurismo() {
        return registroTurismo;
    }

    public void setRegistroTurismo(Alojamiento registroTurismo) {
        this.registroTurismo = registroTurismo;
    }

    public Servicios getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(Servicios codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codS != null ? codS.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Servicio)) {
            return false;
        }
        Servicio other = (Servicio) object;
        if ((this.codS == null && other.codS != null) || (this.codS != null && !this.codS.equals(other.codS))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Servicio[ codS=" + codS + " ]";
    }

}
