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
@Table(name = "imagen")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Imagen.findAll", query = "SELECT i FROM Imagen i")
    , @NamedQuery(name = "Imagen.findByCodImagen", query = "SELECT i FROM Imagen i WHERE i.codImagen = :codImagen")
    , @NamedQuery(name = "Imagen.findByImagen", query = "SELECT i FROM Imagen i WHERE i.imagen = :imagen")
    , @NamedQuery(name = "Imagen.findByCodAlojamiento", query = "SELECT i FROM Imagen i WHERE i.registroTurismo = :registroTurismo")
})
public class Imagen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codImagen")
    private Integer codImagen;
    @Basic(optional = false)
    @Column(name = "imagen")
    private String imagen;
    @JoinColumn(name = "registroTurismo", referencedColumnName = "registroTurismo")
    @ManyToOne(optional = false)
    private Alojamiento registroTurismo;

    public Imagen() {
    }

    public Imagen(Integer codImagen) {
        this.codImagen = codImagen;
    }

    public Imagen(Integer codImagen, String imagen) {
        this.codImagen = codImagen;
        this.imagen = imagen;
    }

    public Integer getCodImagen() {
        return codImagen;
    }

    public void setCodImagen(Integer codImagen) {
        this.codImagen = codImagen;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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
        hash += (codImagen != null ? codImagen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Imagen)) {
            return false;
        }
        Imagen other = (Imagen) object;
        if ((this.codImagen == null && other.codImagen != null) || (this.codImagen != null && !this.codImagen.equals(other.codImagen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Imagen[ codImagen=" + codImagen + " ]";
    }
    
}
