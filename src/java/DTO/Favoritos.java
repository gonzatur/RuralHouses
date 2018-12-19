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
@Table(name = "favoritos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Favoritos.findAll", query = "SELECT f FROM Favoritos f")
    , @NamedQuery(name = "Favoritos.findByCodigoFav", query = "SELECT f FROM Favoritos f WHERE f.codigoFav = :codigoFav")
    , @NamedQuery(name = "Favoritos.findByNif", query = "SELECT f FROM Favoritos f WHERE f.nif = :nif")
    , @NamedQuery(name = "Favoritos.findByNifAndRegistroTurismo", query = "SELECT f FROM Favoritos f WHERE f.nif = :nif AND f.registroTurismo = :registroTurismo")
})
public class Favoritos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigoFav")
    private Integer codigoFav;
    @JoinColumn(name = "nif", referencedColumnName = "nif")
    @ManyToOne(optional = false)
    private Usuario nif;
    @JoinColumn(name = "registroTurismo", referencedColumnName = "registroTurismo")
    @ManyToOne(optional = false)
    private Alojamiento registroTurismo;

    public Favoritos() {
    }

    public Favoritos(Integer codigoFav, Usuario nif, Alojamiento registroTurismo) {
        this.codigoFav = codigoFav;
        this.nif = nif;
        this.registroTurismo = registroTurismo;
    }

    public Integer getCodigoFav() {
        return codigoFav;
    }

    public void setCodigoFav(Integer codigoFav) {
        this.codigoFav = codigoFav;
    }

    public Usuario getNif() {
        return nif;
    }

    public void setNif(Usuario nif) {
        this.nif = nif;
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
        hash += (codigoFav != null ? codigoFav.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Favoritos)) {
            return false;
        }
        Favoritos other = (Favoritos) object;
        if ((this.codigoFav == null && other.codigoFav != null) || (this.codigoFav != null && !this.codigoFav.equals(other.codigoFav))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Favoritos[ codigoFav=" + codigoFav + " ]";
    }

}
