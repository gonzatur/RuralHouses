/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gonza
 */
@Entity
@Table(name = "zona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zona.findAll", query = "SELECT z FROM Zona z")
    , @NamedQuery(name = "Zona.findByCodigoZona", query = "SELECT z FROM Zona z WHERE z.codigoZona = :codigoZona")
    , @NamedQuery(name = "Zona.findByNombreZona", query = "SELECT z FROM Zona z WHERE z.nombreZona = :nombreZona")
    , @NamedQuery(name = "Zona.findByImagen", query = "SELECT z FROM Zona z WHERE z.imagen = :imagen")})
public class Zona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigoZona")
    private Integer codigoZona;
    @Basic(optional = false)
    @Column(name = "nombreZona")
    private String nombreZona;
    @Column(name = "imagen")
    private String imagen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoZona")
    private List<Puebloszona> puebloszonaList;

    public Zona() {
    }

    public Zona(Integer codigoZona) {
        this.codigoZona = codigoZona;
    }

    public Zona(Integer codigoZona, String nombreZona) {
        this.codigoZona = codigoZona;
        this.nombreZona = nombreZona;
    }

    public Integer getCodigoZona() {
        return codigoZona;
    }

    public void setCodigoZona(Integer codigoZona) {
        this.codigoZona = codigoZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @XmlTransient
    public List<Puebloszona> getPuebloszonaList() {
        return puebloszonaList;
    }

    public void setPuebloszonaList(List<Puebloszona> puebloszonaList) {
        this.puebloszonaList = puebloszonaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoZona != null ? codigoZona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zona)) {
            return false;
        }
        Zona other = (Zona) object;
        if ((this.codigoZona == null && other.codigoZona != null) || (this.codigoZona != null && !this.codigoZona.equals(other.codigoZona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Zona[ codigoZona=" + codigoZona + " ]";
    }
    
}
