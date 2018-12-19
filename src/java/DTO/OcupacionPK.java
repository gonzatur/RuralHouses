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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author gonza
 */
@Embeddable
public class OcupacionPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "registroTurismo")
    private String registroTurismo;
    @Basic(optional = false)
    @Column(name = "noche")
    @Temporal(TemporalType.DATE)
    private Date noche;

    public OcupacionPK() {
    }

    public OcupacionPK(String registroTurismo, Date noche) {
        this.registroTurismo = registroTurismo;
        this.noche = noche;
    }

    public String getRegistroTurismo() {
        return registroTurismo;
    }

    public void setRegistroTurismo(String registroTurismo) {
        this.registroTurismo = registroTurismo;
    }

    public Date getNoche() {
        return noche;
    }

    public void setNoche(Date noche) {
        this.noche = noche;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registroTurismo != null ? registroTurismo.hashCode() : 0);
        hash += (noche != null ? noche.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OcupacionPK)) {
            return false;
        }
        OcupacionPK other = (OcupacionPK) object;
        if ((this.registroTurismo == null && other.registroTurismo != null) || (this.registroTurismo != null && !this.registroTurismo.equals(other.registroTurismo))) {
            return false;
        }
        if ((this.noche == null && other.noche != null) || (this.noche != null && !this.noche.equals(other.noche))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.OcupacionPK[ registroTurismo=" + registroTurismo + ", noche=" + noche + " ]";
    }
    
}
