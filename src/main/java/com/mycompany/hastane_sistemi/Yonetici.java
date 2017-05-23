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
@Table(name = "YONETICI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Yonetici.findAll", query = "SELECT y FROM Yonetici y")
    , @NamedQuery(name = "Yonetici.findById", query = "SELECT y FROM Yonetici y WHERE y.id = :id")
    , @NamedQuery(name = "Yonetici.findByAd", query = "SELECT y FROM Yonetici y WHERE y.ad = :ad")
    , @NamedQuery(name = "Yonetici.findBySoyad", query = "SELECT y FROM Yonetici y WHERE y.soyad = :soyad")
    , @NamedQuery(name = "Yonetici.findByYoneticiModuluPw", query = "SELECT y FROM Yonetici y WHERE y.yoneticiModuluPw = :yoneticiModuluPw")})
public class Yonetici implements Serializable {

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
    @Column(name = "YONETICI_MODULU_PW")
    private String yoneticiModuluPw;
    @JoinColumn(name = "KLINIK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Klinik klinikId;

    public Yonetici() {
    }

    public Yonetici(String id) {
        this.id = id;
    }

    public Yonetici(String id, String ad, String soyad, String yoneticiModuluPw) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.yoneticiModuluPw = yoneticiModuluPw;
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

    public String getYoneticiModuluPw() {
        return yoneticiModuluPw;
    }

    public void setYoneticiModuluPw(String yoneticiModuluPw) {
        this.yoneticiModuluPw = yoneticiModuluPw;
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
        if (!(object instanceof Yonetici)) {
            return false;
        }
        Yonetici other = (Yonetici) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.hastane_sistemi.Yonetici[ id=" + id + " ]";
    }
    
}
