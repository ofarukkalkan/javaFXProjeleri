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
@Table(name = "KLINIK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Klinik.findAll", query = "SELECT k FROM Klinik k")
    , @NamedQuery(name = "Klinik.findById", query = "SELECT k FROM Klinik k WHERE k.id = :id")
    , @NamedQuery(name = "Klinik.findByDetay", query = "SELECT k FROM Klinik k WHERE k.detay = :detay")})
public class Klinik implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @Column(name = "DETAY")
    private String detay;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "klinikId")
    private Collection<Yonetici> yoneticiCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "klinikId")
    private Collection<Doktor> doktorCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "klinikId")
    private Collection<Sekreter> sekreterCollection;

    public Klinik() {
    }

    public Klinik(String id) {
        this.id = id;
    }

    public Klinik(String id, String detay) {
        this.id = id;
        this.detay = detay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetay() {
        return detay;
    }

    public void setDetay(String detay) {
        this.detay = detay;
    }

    @XmlTransient
    public Collection<Yonetici> getYoneticiCollection() {
        return yoneticiCollection;
    }

    public void setYoneticiCollection(Collection<Yonetici> yoneticiCollection) {
        this.yoneticiCollection = yoneticiCollection;
    }

    @XmlTransient
    public Collection<Doktor> getDoktorCollection() {
        return doktorCollection;
    }

    public void setDoktorCollection(Collection<Doktor> doktorCollection) {
        this.doktorCollection = doktorCollection;
    }

    @XmlTransient
    public Collection<Sekreter> getSekreterCollection() {
        return sekreterCollection;
    }

    public void setSekreterCollection(Collection<Sekreter> sekreterCollection) {
        this.sekreterCollection = sekreterCollection;
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
        if (!(object instanceof Klinik)) {
            return false;
        }
        Klinik other = (Klinik) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.hastane_sistemi.Klinik[ id=" + id + " ]";
    }
    
}
