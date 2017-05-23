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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "DOKTOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Doktor.findAll", query = "SELECT d FROM Doktor d")
    , @NamedQuery(name = "Doktor.findById", query = "SELECT d FROM Doktor d WHERE d.id = :id")
    , @NamedQuery(name = "Doktor.findByAd", query = "SELECT d FROM Doktor d WHERE d.ad = :ad")
    , @NamedQuery(name = "Doktor.findBySoyad", query = "SELECT d FROM Doktor d WHERE d.soyad = :soyad")
    , @NamedQuery(name = "Doktor.findByDoktorModuluPw", query = "SELECT d FROM Doktor d WHERE d.doktorModuluPw = :doktorModuluPw")})
public class Doktor implements Serializable {

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
    @Basic(optional = false)
    @Column(name = "DOKTOR_MODULU_PW")
    private String doktorModuluPw;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doktorId")
    private Collection<Randevu> randevuCollection;
    @JoinColumn(name = "KLINIK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Klinik klinikId;

    public Doktor() {
    }

    public Doktor(String id) {
        this.id = id;
    }

    public Doktor(String id, String ad, String soyad, String doktorModuluPw) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.doktorModuluPw = doktorModuluPw;
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

    public String getDoktorModuluPw() {
        return doktorModuluPw;
    }

    public void setDoktorModuluPw(String doktorModuluPw) {
        this.doktorModuluPw = doktorModuluPw;
    }

    @XmlTransient
    public Collection<Randevu> getRandevuCollection() {
        return randevuCollection;
    }

    public void setRandevuCollection(Collection<Randevu> randevuCollection) {
        this.randevuCollection = randevuCollection;
    }

    public Klinik getKlinikId() {
        return klinikId;
    }

    public void setKlinikId(Klinik klinikId) {
        this.klinikId = klinikId;
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
        if (!(object instanceof Doktor)) {
            return false;
        }
        Doktor other = (Doktor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.hastane_sistemi.Doktor[ id=" + id + " ]";
    }
    
}
