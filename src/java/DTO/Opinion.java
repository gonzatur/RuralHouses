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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "opinion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Opinion.findAll", query = "SELECT o FROM Opinion o")
    , @NamedQuery(name = "Opinion.findByCodigoOpinion", query = "SELECT o FROM Opinion o WHERE o.codigoOpinion = :codigoOpinion")
    , @NamedQuery(name = "Opinion.findByLimpieza", query = "SELECT o FROM Opinion o WHERE o.limpieza = :limpieza")
    , @NamedQuery(name = "Opinion.findByConfort", query = "SELECT o FROM Opinion o WHERE o.confort = :confort")
    , @NamedQuery(name = "Opinion.findByUbicacion", query = "SELECT o FROM Opinion o WHERE o.ubicacion = :ubicacion")
    , @NamedQuery(name = "Opinion.findByRelacionCP", query = "SELECT o FROM Opinion o WHERE o.relacionCP = :relacionCP")
    , @NamedQuery(name = "Opinion.findByTratoRecibido", query = "SELECT o FROM Opinion o WHERE o.tratoRecibido = :tratoRecibido")
    , @NamedQuery(name = "Opinion.findByComentario", query = "SELECT o FROM Opinion o WHERE o.comentario = :comentario")
    , @NamedQuery(name = "Opinion.findByPuntuacionMedia", query = "SELECT o FROM Opinion o WHERE o.puntuacionMedia = :puntuacionMedia")
    , @NamedQuery(name = "Opinion.findByFecha", query = "SELECT o FROM Opinion o WHERE o.fecha = :fecha")
    , @NamedQuery(name = "Opinion.findByDenuncia", query = "SELECT o FROM Opinion o WHERE o.denuncia = :denuncia")
    , @NamedQuery(name = "Opinion.findByCodAlojamiento", query = "SELECT o FROM Opinion o WHERE o.registroTurismo = :registroTurismo")
    , @NamedQuery(name = "Opinion.findByNif", query = "SELECT o FROM Opinion o WHERE o.nif = :nif")
})
public class Opinion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigoOpinion")
    private Integer codigoOpinion;
    @Basic(optional = false)
    @Column(name = "limpieza")
    private int limpieza;
    @Basic(optional = false)
    @Column(name = "confort")
    private int confort;
    @Basic(optional = false)
    @Column(name = "ubicacion")
    private int ubicacion;
    @Basic(optional = false)
    @Column(name = "relacionCP")
    private int relacionCP;
    @Basic(optional = false)
    @Column(name = "tratoRecibido")
    private int tratoRecibido;
    @Basic(optional = false)
    @Column(name = "comentario")
    private String comentario;
    @Basic(optional = false)
    @Column(name = "puntuacionMedia")
    private int puntuacionMedia;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "denuncia")
    private int denuncia;
    @JoinColumn(name = "nif", referencedColumnName = "nif")
    @ManyToOne(optional = false)
    private Usuario nif;
    @JoinColumn(name = "registroTurismo", referencedColumnName = "registroTurismo")
    @ManyToOne(optional = false)
    private Alojamiento registroTurismo;

    public Opinion() {
    }

    public Opinion(Integer codigoOpinion) {
        this.codigoOpinion = codigoOpinion;
    }

    public Opinion(Integer codigoOpinion, int limpieza, int confort, int ubicacion, int relacionCP, int tratoRecibido, String comentario, int puntuacionMedia, Date fecha, int denuncia) {
        this.codigoOpinion = codigoOpinion;
        this.limpieza = limpieza;
        this.confort = confort;
        this.ubicacion = ubicacion;
        this.relacionCP = relacionCP;
        this.tratoRecibido = tratoRecibido;
        this.comentario = comentario;
        this.puntuacionMedia = puntuacionMedia;
        this.fecha = fecha;
        this.denuncia = denuncia;
    }

    public Integer getCodigoOpinion() {
        return codigoOpinion;
    }

    public void setCodigoOpinion(Integer codigoOpinion) {
        this.codigoOpinion = codigoOpinion;
    }

    public int getLimpieza() {
        return limpieza;
    }

    public void setLimpieza(int limpieza) {
        this.limpieza = limpieza;
    }

    public int getConfort() {
        return confort;
    }

    public void setConfort(int confort) {
        this.confort = confort;
    }

    public int getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(int ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getRelacionCP() {
        return relacionCP;
    }

    public void setRelacionCP(int relacionCP) {
        this.relacionCP = relacionCP;
    }

    public int getTratoRecibido() {
        return tratoRecibido;
    }

    public void setTratoRecibido(int tratoRecibido) {
        this.tratoRecibido = tratoRecibido;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getPuntuacionMedia() {
        return puntuacionMedia;
    }

    public void setPuntuacionMedia(int puntuacionMedia) {
        this.puntuacionMedia = puntuacionMedia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(int denuncia) {
        this.denuncia = denuncia;
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
        hash += (codigoOpinion != null ? codigoOpinion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Opinion)) {
            return false;
        }
        Opinion other = (Opinion) object;
        if ((this.codigoOpinion == null && other.codigoOpinion != null) || (this.codigoOpinion != null && !this.codigoOpinion.equals(other.codigoOpinion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Opinion[ codigoOpinion=" + codigoOpinion + " ]";
    }
    
}
