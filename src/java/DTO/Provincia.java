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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gonza
 */
@Entity
@Table(name = "provincia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Provincia.findAll", query = "SELECT p FROM Provincia p")
    , @NamedQuery(name = "Provincia.findByCodigoProvincia", query = "SELECT p FROM Provincia p WHERE p.codigoProvincia = :codigoProvincia")
    , @NamedQuery(name = "Provincia.findByNombreProvincia", query = "SELECT p FROM Provincia p WHERE p.nombreProvincia = :nombreProvincia")
    , @NamedQuery(name = "Provincia.findByCodigoComunidad", query = "SELECT p FROM Provincia p WHERE p.codigoComunidad = :codigoComunidad")})
public class Provincia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigoProvincia")
    private Integer codigoProvincia;
    @Basic(optional = false)
    @Column(name = "nombreProvincia")
    private String nombreProvincia;
    @Basic(optional = false)
    @Column(name = "codigoComunidad")
    private int codigoComunidad;

    public Provincia() {
    }

    public Provincia(Integer codigoProvincia) {
        this.codigoProvincia = codigoProvincia;
    }

    public Provincia(Integer codigoProvincia, String nombreProvincia, int codigoComunidad) {
        this.codigoProvincia = codigoProvincia;
        this.nombreProvincia = nombreProvincia;
        this.codigoComunidad = codigoComunidad;
    }

    public Integer getCodigoProvincia() {
        return codigoProvincia;
    }

    public void setCodigoProvincia(Integer codigoProvincia) {
        this.codigoProvincia = codigoProvincia;
    }

    public String getNombreProvincia() {
        return nombreProvincia;
    }

    public void setNombreProvincia(String nombreProvincia) {
        this.nombreProvincia = nombreProvincia;
    }

    public int getCodigoComunidad() {
        return codigoComunidad;
    }

    public void setCodigoComunidad(int codigoComunidad) {
        this.codigoComunidad = codigoComunidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoProvincia != null ? codigoProvincia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Provincia)) {
            return false;
        }
        Provincia other = (Provincia) object;
        if ((this.codigoProvincia == null && other.codigoProvincia != null) || (this.codigoProvincia != null && !this.codigoProvincia.equals(other.codigoProvincia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Provincia[ codigoProvincia=" + codigoProvincia + " ]";
    }
    
}
