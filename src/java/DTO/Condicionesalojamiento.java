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
@Table(name = "condicionesalojamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Condicionesalojamiento.findAll", query = "SELECT c FROM Condicionesalojamiento c")
    , @NamedQuery(name = "Condicionesalojamiento.findByCodigoCond", query = "SELECT c FROM Condicionesalojamiento c WHERE c.codigoCond = :codigoCond")
    , @NamedQuery(name = "Condicionesalojamiento.findByA", query = "SELECT c FROM Condicionesalojamiento c WHERE c.a = :a")
    , @NamedQuery(name = "Condicionesalojamiento.findByB", query = "SELECT c FROM Condicionesalojamiento c WHERE c.b = :b")
    , @NamedQuery(name = "Condicionesalojamiento.findByC", query = "SELECT c FROM Condicionesalojamiento c WHERE c.c = :c")
    , @NamedQuery(name = "Condicionesalojamiento.findByD", query = "SELECT c FROM Condicionesalojamiento c WHERE c.d = :d")
    , @NamedQuery(name = "Condicionesalojamiento.findByE", query = "SELECT c FROM Condicionesalojamiento c WHERE c.e = :e")
    , @NamedQuery(name = "Condicionesalojamiento.findByF", query = "SELECT c FROM Condicionesalojamiento c WHERE c.f = :f")
    , @NamedQuery(name = "Condicionesalojamiento.findByG", query = "SELECT c FROM Condicionesalojamiento c WHERE c.g = :g")
    , @NamedQuery(name = "Condicionesalojamiento.findByH", query = "SELECT c FROM Condicionesalojamiento c WHERE c.h = :h")
    , @NamedQuery(name = "Condicionesalojamiento.findByI", query = "SELECT c FROM Condicionesalojamiento c WHERE c.i = :i")})
public class Condicionesalojamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigoCond")
    private Integer codigoCond;
    @Basic(optional = false)
    @Column(name = "A")
    private int a;
    @Basic(optional = false)
    @Column(name = "B")
    private int b;
    @Basic(optional = false)
    @Column(name = "C")
    private float c;
    @Basic(optional = false)
    @Column(name = "D")
    private int d;
    @Basic(optional = false)
    @Column(name = "E")
    private int e;
    @Basic(optional = false)
    @Column(name = "F")
    private int f;
    @Basic(optional = false)
    @Column(name = "G")
    private String g;
    @Basic(optional = false)
    @Column(name = "H")
    private String h;
    @Basic(optional = false)
    @Column(name = "I")
    private int i;
    @JoinColumn(name = "registroTurismo", referencedColumnName = "registroTurismo")
    @ManyToOne(optional = false)
    private Alojamiento registroTurismo;

    public Condicionesalojamiento() {
    }

    public Condicionesalojamiento(Integer codigoCond) {
        this.codigoCond = codigoCond;
    }

    public Condicionesalojamiento(Integer codigoCond, int a, int b, float c, int d, int e, int f, String g, String h, int i) {
        this.codigoCond = codigoCond;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
    }

    public Integer getCodigoCond() {
        return codigoCond;
    }

    public void setCodigoCond(Integer codigoCond) {
        this.codigoCond = codigoCond;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public float getC() {
        return c;
    }

    public void setC(float c) {
        this.c = c;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
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
        hash += (codigoCond != null ? codigoCond.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Condicionesalojamiento)) {
            return false;
        }
        Condicionesalojamiento other = (Condicionesalojamiento) object;
        if ((this.codigoCond == null && other.codigoCond != null) || (this.codigoCond != null && !this.codigoCond.equals(other.codigoCond))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Condicionesalojamiento[ codigoCond=" + codigoCond + " ]";
    }
    
}
