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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gonza
 */
@Entity
@Table(name = "alojamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alojamiento.findAll", query = "SELECT a FROM Alojamiento a")
    , @NamedQuery(name = "Alojamiento.findByRegistroTurismo", query = "SELECT a FROM Alojamiento a WHERE a.registroTurismo = :registroTurismo")
    , @NamedQuery(name = "Alojamiento.findByCodigoPoblacion", query = "SELECT a FROM Alojamiento a WHERE a.codigoPoblacion = :codigoPoblacion")
    , @NamedQuery(name = "Alojamiento.findByNombre", query = "SELECT a FROM Alojamiento a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "Alojamiento.findByPlazasMax", query = "SELECT a FROM Alojamiento a ORDER BY a.plazasMax desc")
    , @NamedQuery(name = "Alojamiento.findByNumeroHab", query = "SELECT a FROM Alojamiento a WHERE a.numeroHab = :numeroHab")
    , @NamedQuery(name = "Alojamiento.findByNumeroWc", query = "SELECT a FROM Alojamiento a WHERE a.numeroWc = :numeroWc")
    , @NamedQuery(name = "Alojamiento.findByMetrosCuadrados", query = "SELECT a FROM Alojamiento a WHERE a.metrosCuadrados = :metrosCuadrados")
    , @NamedQuery(name = "Alojamiento.findByPrecioNoche", query = "SELECT a FROM Alojamiento a ORDER BY a.precioNoche")
    , @NamedQuery(name = "Alojamiento.findByDireccion", query = "SELECT a FROM Alojamiento a WHERE a.direccion = :direccion")
    , @NamedQuery(name = "Alojamiento.findByComentario", query = "SELECT a FROM Alojamiento a WHERE a.comentario = :comentario")
    , @NamedQuery(name = "Alojamiento.findByLongitud", query = "SELECT a FROM Alojamiento a WHERE a.longitud = :longitud")
    , @NamedQuery(name = "Alojamiento.findByLatitud", query = "SELECT a FROM Alojamiento a WHERE a.latitud = :latitud")
    , @NamedQuery(name = "Alojamiento.findByImagenPrincipal", query = "SELECT a FROM Alojamiento a WHERE a.imagenPrincipal = :imagenPrincipal")
    , @NamedQuery(name = "Alojamiento.findByCompleto", query = "SELECT a FROM Alojamiento a WHERE a.completo = :completo")
    , @NamedQuery(name = "Alojamiento.findByNif", query = "SELECT a FROM Alojamiento a WHERE a.nif = :nif")
})
public class Alojamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "registroTurismo")
    private String registroTurismo;
    @Basic(optional = false)
    @Column(name = "codigoPoblacion")
    private int codigoPoblacion;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "plazasMax")
    private int plazasMax;
    @Basic(optional = false)
    @Column(name = "numeroHab")
    private int numeroHab;
    @Basic(optional = false)
    @Column(name = "numeroWc")
    private int numeroWc;
    @Basic(optional = false)
    @Column(name = "metrosCuadrados")
    private int metrosCuadrados;
    @Basic(optional = false)
    @Column(name = "precioNoche")
    private float precioNoche;
    @Basic(optional = false)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @Column(name = "comentario")
    private String comentario;
    @Basic(optional = false)
    @Column(name = "longitud")
    private float longitud;
    @Basic(optional = false)
    @Column(name = "latitud")
    private float latitud;
    @Column(name = "imagenPrincipal")
    private String imagenPrincipal;
    @Basic(optional = false)
    @Column(name = "completo")
    private int completo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroTurismo")
    private List<Servicio> servicioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroTurismo")
    private List<Imagen> imagenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroTurismo")
    private List<Condicionesalojamiento> condicionesalojamientoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alojamiento")
    private List<Ocupacion> ocupacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroTurismo")
    private List<Actividad> actividadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroTurismo")
    private List<Opinion> opinionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroTurismo")
    private List<Favoritos> favoritosList;
    @JoinColumn(name = "nif", referencedColumnName = "nif")
    @ManyToOne(optional = false)
    private Usuario nif;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroTurismo")
    private Puebloszona puebloszona;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroTurismo")
    private List<Reserva> reservaList;

    public Alojamiento() {
    }

    public Alojamiento(String registroTurismo) {
        this.registroTurismo = registroTurismo;
    }

    public Alojamiento(String registroTurismo, int codigoPoblacion, String nombre, int plazasMax, int numeroHab, int numeroWc, int metrosCuadrados, float precioNoche, String direccion, String comentario, float longitud, float latitud,String img, int completo) {
        this.registroTurismo = registroTurismo;
        this.codigoPoblacion = codigoPoblacion;
        this.nombre = nombre;
        this.plazasMax = plazasMax;
        this.numeroHab = numeroHab;
        this.numeroWc = numeroWc;
        this.metrosCuadrados = metrosCuadrados;
        this.precioNoche = precioNoche;
        this.direccion = direccion;
        this.comentario = comentario;
        this.longitud = longitud;
        this.latitud = latitud;
        this.imagenPrincipal = img;
        this.completo = completo;
    }

    public String getRegistroTurismo() {
        return registroTurismo;
    }

    public void setRegistroTurismo(String registroTurismo) {
        this.registroTurismo = registroTurismo;
    }

    public int getCodigoPoblacion() {
        return codigoPoblacion;
    }

    public void setCodigoPoblacion(int codigoPoblacion) {
        this.codigoPoblacion = codigoPoblacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPlazasMax() {
        return plazasMax;
    }

    public void setPlazasMax(int plazasMax) {
        this.plazasMax = plazasMax;
    }

    public int getNumeroHab() {
        return numeroHab;
    }

    public void setNumeroHab(int numeroHab) {
        this.numeroHab = numeroHab;
    }

    public int getNumeroWc() {
        return numeroWc;
    }

    public void setNumeroWc(int numeroWc) {
        this.numeroWc = numeroWc;
    }

    public int getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(int metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public float getPrecioNoche() {
        return precioNoche;
    }

    public void setPrecioNoche(float precioNoche) {
        this.precioNoche = precioNoche;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    public int getCompleto() {
        return completo;
    }

    public void setCompleto(int completo) {
        this.completo = completo;
    }

    @XmlTransient
    public List<Servicio> getServicioList() {
        return servicioList;
    }

    public void setServicioList(List<Servicio> servicioList) {
        this.servicioList = servicioList;
    }

    @XmlTransient
    public List<Imagen> getImagenList() {
        return imagenList;
    }

    public void setImagenList(List<Imagen> imagenList) {
        this.imagenList = imagenList;
    }

    @XmlTransient
    public List<Condicionesalojamiento> getCondicionesalojamientoList() {
        return condicionesalojamientoList;
    }

    public void setCondicionesalojamientoList(List<Condicionesalojamiento> condicionesalojamientoList) {
        this.condicionesalojamientoList = condicionesalojamientoList;
    }

    @XmlTransient
    public List<Ocupacion> getOcupacionList() {
        return ocupacionList;
    }

    public void setOcupacionList(List<Ocupacion> ocupacionList) {
        this.ocupacionList = ocupacionList;
    }

    @XmlTransient
    public List<Actividad> getActividadList() {
        return actividadList;
    }

    public void setActividadList(List<Actividad> actividadList) {
        this.actividadList = actividadList;
    }

    @XmlTransient
    public List<Opinion> getOpinionList() {
        return opinionList;
    }

    public void setOpinionList(List<Opinion> opinionList) {
        this.opinionList = opinionList;
    }

    @XmlTransient
    public List<Favoritos> getFavoritosList() {
        return favoritosList;
    }

    public void setFavoritosList(List<Favoritos> favoritosList) {
        this.favoritosList = favoritosList;
    }

    public Usuario getNif() {
        return nif;
    }

    public void setNif(Usuario nif) {
        this.nif = nif;
    }

    public Puebloszona getPuebloszona() {
        return puebloszona;
    }

    public void setPuebloszona(Puebloszona puebloszona) {
        this.puebloszona = puebloszona;
    }

    @XmlTransient
    public List<Reserva> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<Reserva> reservaList) {
        this.reservaList = reservaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registroTurismo != null ? registroTurismo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alojamiento)) {
            return false;
        }
        Alojamiento other = (Alojamiento) object;
        if ((this.registroTurismo == null && other.registroTurismo != null) || (this.registroTurismo != null && !this.registroTurismo.equals(other.registroTurismo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Alojamiento[ registroTurismo=" + registroTurismo + " ]";
    }
    
}
