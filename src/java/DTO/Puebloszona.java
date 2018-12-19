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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gonza
 */
@Entity
@Table(name = "puebloszona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Puebloszona.findAll", query = "SELECT p FROM Puebloszona p")
    , @NamedQuery(name = "Puebloszona.findByCodigoPueblosZona", query = "SELECT p FROM Puebloszona p WHERE p.codigoPueblosZona = :codigoPueblosZona")
    , @NamedQuery(name = "Puebloszona.findByCodigoZona", query = "SELECT p FROM Puebloszona p WHERE p.codigoZona = :codigoZona")
})
public class Puebloszona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigoPueblosZona")
    private Integer codigoPueblosZona;
    @JoinColumn(name = "codigoZona", referencedColumnName = "codigoZona")
    @ManyToOne(optional = false)
    private Zona codigoZona;
    @JoinColumn(name = "registroTurismo", referencedColumnName = "registroTurismo")
    @OneToOne(optional = false)
    private Alojamiento registroTurismo;

    public Puebloszona() {
    }

    public Puebloszona(Integer codigoPueblosZona) {
        this.codigoPueblosZona = codigoPueblosZona;
    }

    public Integer getCodigoPueblosZona() {
        return codigoPueblosZona;
    }

    public void setCodigoPueblosZona(Integer codigoPueblosZona) {
        this.codigoPueblosZona = codigoPueblosZona;
    }

    public Zona getCodigoZona() {
        return codigoZona;
    }

    public void setCodigoZona(Zona codigoZona) {
        this.codigoZona = codigoZona;
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
        hash += (codigoPueblosZona != null ? codigoPueblosZona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Puebloszona)) {
            return false;
        }
        Puebloszona other = (Puebloszona) object;
        if ((this.codigoPueblosZona == null && other.codigoPueblosZona != null) || (this.codigoPueblosZona != null && !this.codigoPueblosZona.equals(other.codigoPueblosZona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Puebloszona[ codigoPueblosZona=" + codigoPueblosZona + " ]";
    }

}
