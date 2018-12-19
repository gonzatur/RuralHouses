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
@Table(name = "cuentabancaria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuentabancaria.findAll", query = "SELECT c FROM Cuentabancaria c")
    , @NamedQuery(name = "Cuentabancaria.findByTarjeta", query = "SELECT c FROM Cuentabancaria c WHERE c.tarjeta = :tarjeta")
    , @NamedQuery(name = "Cuentabancaria.findByCvv", query = "SELECT c FROM Cuentabancaria c WHERE c.cvv = :cvv")
    , @NamedQuery(name = "Cuentabancaria.findByMesCaducidad", query = "SELECT c FROM Cuentabancaria c WHERE c.mesCaducidad = :mesCaducidad")
    , @NamedQuery(name = "Cuentabancaria.findByYearCaducidad", query = "SELECT c FROM Cuentabancaria c WHERE c.yearCaducidad = :yearCaducidad")
    , @NamedQuery(name = "Cuentabancaria.findBySaldo", query = "SELECT c FROM Cuentabancaria c WHERE c.saldo = :saldo")})
public class Cuentabancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "tarjeta")
    private String tarjeta;
    @Basic(optional = false)
    @Column(name = "cvv")
    private int cvv;
    @Basic(optional = false)
    @Column(name = "mesCaducidad")
    private int mesCaducidad;
    @Basic(optional = false)
    @Column(name = "yearCaducidad")
    private int yearCaducidad;
    @Basic(optional = false)
    @Column(name = "saldo")
    private float saldo;
    @JoinColumn(name = "nif", referencedColumnName = "nif")
    @ManyToOne(optional = false)
    private Usuario nif;

    public Cuentabancaria() {
    }

    public Cuentabancaria(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public Cuentabancaria(String tarjeta, int cvv, int mesCaducidad, int yearCaducidad, float saldo) {
        this.tarjeta = tarjeta;
        this.cvv = cvv;
        this.mesCaducidad = mesCaducidad;
        this.yearCaducidad = yearCaducidad;
        this.saldo = saldo;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public int getMesCaducidad() {
        return mesCaducidad;
    }

    public void setMesCaducidad(int mesCaducidad) {
        this.mesCaducidad = mesCaducidad;
    }

    public int getYearCaducidad() {
        return yearCaducidad;
    }

    public void setYearCaducidad(int yearCaducidad) {
        this.yearCaducidad = yearCaducidad;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public Usuario getNif() {
        return nif;
    }

    public void setNif(Usuario nif) {
        this.nif = nif;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tarjeta != null ? tarjeta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuentabancaria)) {
            return false;
        }
        Cuentabancaria other = (Cuentabancaria) object;
        if ((this.tarjeta == null && other.tarjeta != null) || (this.tarjeta != null && !this.tarjeta.equals(other.tarjeta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Cuentabancaria[ tarjeta=" + tarjeta + " ]";
    }
    
}
