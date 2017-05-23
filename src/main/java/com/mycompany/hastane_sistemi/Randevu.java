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
@Table(name = "RANDEVU")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Randevu.findAll", query = "SELECT r FROM Randevu r")
    , @NamedQuery(name = "Randevu.findByTarih", query = "SELECT r FROM Randevu r WHERE r.tarih = :tarih")})
public class Randevu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "TARIH")
    private String tarih;
    @JoinColumn(name = "DOKTOR_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Doktor doktorId;
    @JoinColumn(name = "HASTA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Hasta hastaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "randevuTarih")
    private Collection<Muayene> muayeneCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "randevuTarih")
    private Collection<TahlilKayit> tahlilKayitCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "randevuTarih")
    private Collection<Recete> receteCollection;

    public Randevu() {
    }

    public Randevu(String tarih) {
        this.tarih = tarih;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public Doktor getDoktorId() {
        return doktorId;
    }

    public void setDoktorId(Doktor doktorId) {
        this.doktorId = doktorId;
    }

    public Hasta getHastaId() {
        return hastaId;
    }

    public void setHastaId(Hasta hastaId) {
        this.hastaId = hastaId;
    }

    @XmlTransient
    public Collection<Muayene> getMuayeneCollection() {
        return muayeneCollection;
    }

    public void setMuayeneCollection(Collection<Muayene> muayeneCollection) {
        this.muayeneCollection = muayeneCollection;
    }

    @XmlTransient
    public Collection<TahlilKayit> getTahlilKayitCollection() {
        return tahlilKayitCollection;
    }

    public void setTahlilKayitCollection(Collection<TahlilKayit> tahlilKayitCollection) {
        this.tahlilKayitCollection = tahlilKayitCollection;
    }

    @XmlTransient
    public Collection<Recete> getReceteCollection() {
        return receteCollection;
    }

    public void setReceteCollection(Collection<Recete> receteCollection) {
        this.receteCollection = receteCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tarih != null ? tarih.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Randevu)) {
            return false;
        }
        Randevu other = (Randevu) object;
        if ((this.tarih == null && other.tarih != null) || (this.tarih != null && !this.tarih.equals(other.tarih))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.hastane_sistemi.Randevu[ tarih=" + tarih + " ]";
    }
    
}
