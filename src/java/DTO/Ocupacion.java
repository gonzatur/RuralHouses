/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "ocupacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ocupacion.findAll", query = "SELECT o FROM Ocupacion o")
    , @NamedQuery(name = "Ocupacion.findByRegistroTurismo", query = "SELECT o FROM Ocupacion o WHERE o.ocupacionPK.registroTurismo = :registroTurismo")
    , @NamedQuery(name = "Ocupacion.findByNoche", query = "SELECT o FROM Ocupacion o WHERE o.ocupacionPK.noche = :noche")
    , @NamedQuery(name = "Ocupacion.findByTipo", query = "SELECT o FROM Ocupacion o WHERE o.tipo = :tipo")})
public class Ocupacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OcupacionPK ocupacionPK;
    @Basic(optional = false)
    @Column(name = "tipo")
    private int tipo;
    @JoinColumn(name = "registroTurismo", referencedColumnName = "registroTurismo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Alojamiento alojamiento;

    public Ocupacion() {
    }

    public Ocupacion(OcupacionPK ocupacionPK) {
        this.ocupacionPK = ocupacionPK;
    }

    public Ocupacion(OcupacionPK ocupacionPK, int tipo) {
        this.ocupacionPK = ocupacionPK;
        this.tipo = tipo;
    }

    public Ocupacion(String registroTurismo, Date noche, int tipo, Alojamiento alojamiento) {
        this.ocupacionPK = new OcupacionPK(registroTurismo, noche);
        this.tipo = tipo;
        this.alojamiento = alojamiento;
    }
    

    public OcupacionPK getOcupacionPK() {
        return ocupacionPK;
    }

    public void setOcupacionPK(OcupacionPK ocupacionPK) {
        this.ocupacionPK = ocupacionPK;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ocupacionPK != null ? ocupacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ocupacion)) {
            return false;
        }
        Ocupacion other = (Ocupacion) object;
        if ((this.ocupacionPK == null && other.ocupacionPK != null) || (this.ocupacionPK != null && !this.ocupacionPK.equals(other.ocupacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Ocupacion[ ocupacionPK=" + ocupacionPK + " ]";
    }
    
}
