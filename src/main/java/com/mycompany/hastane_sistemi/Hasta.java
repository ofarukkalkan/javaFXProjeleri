/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hastane_sistemi;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author omerfaruk
 */
@Entity
@Table(name = "HASTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hasta.findAll", query = "SELECT h FROM Hasta h")
    , @NamedQuery(name = "Hasta.findById", query = "SELECT h FROM Hasta h WHERE h.id = :id")
    , @NamedQuery(name = "Hasta.findByAd", query = "SELECT h FROM Hasta h WHERE h.ad = :ad")
    , @NamedQuery(name = "Hasta.findBySoyad", query = "SELECT h FROM Hasta h WHERE h.soyad = :soyad")})
public class Hasta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @Column(name = "AD")
    private String ad;
    @Basic(optional = false)
    @Column(name = "SOYAD")
    private String soyad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hastaId")
    private Collection<Randevu> randevuCollection;

    public Hasta() {
    }

    public Hasta(String id) {
        this.id = id;
    }

    public Hasta(String id, String ad, String soyad) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    @XmlTransient
    public Collection<Randevu> getRandevuCollection() {
        return randevuCollection;
    }

    public void setRandevuCollection(Collection<Randevu> randevuCollection) {
        this.randevuCollection = randevuCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hasta)) {
            return false;
        }
        Hasta other = (Hasta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.hastane_sistemi.Hasta[ id=" + id + " ]";
    }
    
}
