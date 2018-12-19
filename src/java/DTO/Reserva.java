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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gonza
 */
@Entity
@Table(name = "reserva")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reserva.findAll", query = "SELECT r FROM Reserva r")
    , @NamedQuery(name = "Reserva.findByCodigoReserva", query = "SELECT r FROM Reserva r WHERE r.codigoReserva = :codigoReserva")
    , @NamedQuery(name = "Reserva.findByFechaEntrada", query = "SELECT r FROM Reserva r WHERE r.fechaEntrada = :fechaEntrada")
    , @NamedQuery(name = "Reserva.findByFechaSalida", query = "SELECT r FROM Reserva r WHERE r.fechaSalida = :fechaSalida")
    , @NamedQuery(name = "Reserva.findByNumeroPersonas", query = "SELECT r FROM Reserva r WHERE r.numeroPersonas = :numeroPersonas")
    , @NamedQuery(name = "Reserva.findByPersonasSupletorias", query = "SELECT r FROM Reserva r WHERE r.personasSupletorias = :personasSupletorias")
    , @NamedQuery(name = "Reserva.findByAnticipo", query = "SELECT r FROM Reserva r WHERE r.anticipo = :anticipo")
    , @NamedQuery(name = "Reserva.findByComisionPagada", query = "SELECT r FROM Reserva r WHERE r.comisionPagada = :comisionPagada")
    , @NamedQuery(name = "Reserva.findByPrecioTotal", query = "SELECT r FROM Reserva r WHERE r.precioTotal = :precioTotal")
    , @NamedQuery(name = "Reserva.findByNif", query = "SELECT r FROM Reserva r WHERE r.nif = :nif")})
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigoReserva")
    private String codigoReserva;
    @Basic(optional = false)
    @Column(name = "fechaEntrada")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrada;
    @Basic(optional = false)
    @Column(name = "fechaSalida")
    @Temporal(TemporalType.DATE)
    private Date fechaSalida;
    @Basic(optional = false)
    @Column(name = "numeroPersonas")
    private int numeroPersonas;
    @Basic(optional = false)
    @Column(name = "personasSupletorias")
    private int personasSupletorias;
    @Basic(optional = false)
    @Column(name = "anticipo")
    private float anticipo;
    @Basic(optional = false)
    @Column(name = "comisionPagada")
    private float comisionPagada;
    @Basic(optional = false)
    @Column(name = "precioTotal")
    private float precioTotal;
    @JoinColumn(name = "nif", referencedColumnName = "nif")
    @ManyToOne(optional = false)
    private Usuario nif;
    @JoinColumn(name = "registroTurismo", referencedColumnName = "registroTurismo")
    @ManyToOne(optional = false)
    private Alojamiento registroTurismo;

    public Reserva() {
    }

    public Reserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public Reserva(String codigoReserva, Usuario nif, Alojamiento registroTurismo, Date fechaEntrada, Date fechaSalida, int numeroPersonas, float anticipo, float comisionPagada, float precioTotal) {
        this.codigoReserva = codigoReserva;
        this.nif = nif;
        this.registroTurismo = registroTurismo;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.numeroPersonas = numeroPersonas;
        this.anticipo = anticipo;
        this.comisionPagada = comisionPagada;
        this.precioTotal = precioTotal;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public int getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(int numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }

    public int getPersonasSupletorias() {
        return personasSupletorias;
    }

    public void setPersonasSupletorias(int personasSupletorias) {
        this.personasSupletorias = personasSupletorias;
    }

    public float getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(float anticipo) {
        this.anticipo = anticipo;
    }

    public float getComisionPagada() {
        return comisionPagada;
    }

    public void setComisionPagada(float comisionPagada) {
        this.comisionPagada = comisionPagada;
    }

    public float getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(float precioTotal) {
        this.precioTotal = precioTotal;
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
        hash += (codigoReserva != null ? codigoReserva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reserva)) {
            return false;
        }
        Reserva other = (Reserva) object;
        if ((this.codigoReserva == null && other.codigoReserva != null) || (this.codigoReserva != null && !this.codigoReserva.equals(other.codigoReserva))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Reserva[ codigoReserva=" + codigoReserva + " ]";
    }
    
}
