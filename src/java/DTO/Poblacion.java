/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "poblacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Poblacion.findAll", query = "SELECT p FROM Poblacion p")
    , @NamedQuery(name = "Poblacion.findByCodigoPoblacion", query = "SELECT p FROM Poblacion p WHERE p.codigoPoblacion = :codigoPoblacion")
    , @NamedQuery(name = "Poblacion.findByCodigoProvincia", query = "SELECT p FROM Poblacion p WHERE p.codigoProvincia = :codigoProvincia")
    , @NamedQuery(name = "Poblacion.findByNombrePoblacion", query = "SELECT p FROM Poblacion p WHERE p.nombrePoblacion = :nombrePoblacion")
    , @NamedQuery(name = "Poblacion.findByPostal", query = "SELECT p FROM Poblacion p WHERE p.postal = :postal")
    , @NamedQuery(name = "Poblacion.findByLatitud", query = "SELECT p FROM Poblacion p WHERE p.latitud = :latitud")
    , @NamedQuery(name = "Poblacion.findByLongitud", query = "SELECT p FROM Poblacion p WHERE p.longitud = :longitud")
    , @NamedQuery(name = "Poblacion.findByBusquedas", query = "SELECT p FROM Poblacion p ORDER BY p.busquedas DESC")})
public class Poblacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigoPoblacion")
    private Integer codigoPoblacion;
    @Basic(optional = false)
    @Column(name = "codigoProvincia")
    private int codigoProvincia;
    @Basic(optional = false)
    @Column(name = "nombrePoblacion")
    private String nombrePoblacion;
    @Column(name = "postal")
    private Integer postal;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "latitud")
    private BigDecimal latitud;
    @Column(name = "longitud")
    private BigDecimal longitud;
    @Basic(optional = false)
    @Column(name = "busquedas")
    private int busquedas;

    public Poblacion() {
    }

    public Poblacion(Integer codigoPoblacion) {
        this.codigoPoblacion = codigoPoblacion;
    }

    public Poblacion(Integer codigoPoblacion, int codigoProvincia, String nombrePoblacion, int busquedas) {
        this.codigoPoblacion = codigoPoblacion;
        this.codigoProvincia = codigoProvincia;
        this.nombrePoblacion = nombrePoblacion;
        this.busquedas = busquedas;
    }

    public Integer getCodigoPoblacion() {
        return codigoPoblacion;
    }

    public void setCodigoPoblacion(Integer codigoPoblacion) {
        this.codigoPoblacion = codigoPoblacion;
    }

    public int getCodigoProvincia() {
        return codigoProvincia;
    }

    public void setCodigoProvincia(int codigoProvincia) {
        this.codigoProvincia = codigoProvincia;
    }

    public String getNombrePoblacion() {
        return nombrePoblacion;
    }

    public void setNombrePoblacion(String nombrePoblacion) {
        this.nombrePoblacion = nombrePoblacion;
    }

    public Integer getPostal() {
        return postal;
    }

    public void setPostal(Integer postal) {
        this.postal = postal;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public int getBusquedas() {
        return busquedas;
    }

    public void setBusquedas(int busquedas) {
        this.busquedas = busquedas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoPoblacion != null ? codigoPoblacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Poblacion)) {
            return false;
        }
        Poblacion other = (Poblacion) object;
        if ((this.codigoPoblacion == null && other.codigoPoblacion != null) || (this.codigoPoblacion != null && !this.codigoPoblacion.equals(other.codigoPoblacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Poblacion[ codigoPoblacion=" + codigoPoblacion + " ]";
    }
    
}
