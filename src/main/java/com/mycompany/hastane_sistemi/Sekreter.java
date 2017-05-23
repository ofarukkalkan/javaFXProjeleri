/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hastane_sistemi;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author omerfaruk
 */
@Entity
@Table(name = "SEKRETER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sekreter.findAll", query = "SELECT s FROM Sekreter s")
    , @NamedQuery(name = "Sekreter.findById", query = "SELECT s FROM Sekreter s WHERE s.id = :id")
    , @NamedQuery(name = "Sekreter.findByAd", query = "SELECT s FROM Sekreter s WHERE s.ad = :ad")
    , @NamedQuery(name = "Sekreter.findBySoyad", query = "SELECT s FROM Sekreter s WHERE s.soyad = :soyad")
    , @NamedQuery(name = "Sekreter.findBySekreterModuluPw", query = "SELECT s FROM Sekreter s WHERE s.sekreterModuluPw = :sekreterModuluPw")})
public class Sekreter implements Serializable {

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
    @Column(name = "SEKRETER_MODULU_PW")
    private String sekreterModuluPw;
    @JoinColumn(name = "KLINIK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Klinik klinikId;

    public Sekreter() {
    }

    public Sekreter(String id) {
        this.id = id;
    }

    public Sekreter(String id, String ad, String soyad, String sekreterModuluPw) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.sekreterModuluPw = sekreterModuluPw;
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

    public String getSekreterModuluPw() {
        return sekreterModuluPw;
    }

    public void setSekreterModuluPw(String sekreterModuluPw) {
        this.sekreterModuluPw = sekreterModuluPw;
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
        if (!(object instanceof Sekreter)) {
            return false;
        }
        Sekreter other = (Sekreter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.hastane_sistemi.Sekreter[ id=" + id + " ]";
    }
    
}
