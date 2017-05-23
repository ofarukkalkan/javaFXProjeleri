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
@Table(name = "TAHLIL_SONUC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TahlilSonuc.findAll", query = "SELECT t FROM TahlilSonuc t")
    , @NamedQuery(name = "TahlilSonuc.findByTarih", query = "SELECT t FROM TahlilSonuc t WHERE t.tarih = :tarih")
    , @NamedQuery(name = "TahlilSonuc.findBySonuc", query = "SELECT t FROM TahlilSonuc t WHERE t.sonuc = :sonuc")})
public class TahlilSonuc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "TARIH")
    private String tarih;
    @Basic(optional = false)
    @Column(name = "SONUC")
    private String sonuc;
    @JoinColumn(name = "TAHLIL_KAYIT_TARIH", referencedColumnName = "TARIH")
    @ManyToOne(optional = false)
    private TahlilKayit tahlilKayitTarih;

    public TahlilSonuc() {
    }

    public TahlilSonuc(String tarih) {
        this.tarih = tarih;
    }

    public TahlilSonuc(String tarih, String sonuc) {
        this.tarih = tarih;
        this.sonuc = sonuc;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getSonuc() {
        return sonuc;
    }

    public void setSonuc(String sonuc) {
        this.sonuc = sonuc;
    }

    public TahlilKayit getTahlilKayitTarih() {
        return tahlilKayitTarih;
    }

    public void setTahlilKayitTarih(TahlilKayit tahlilKayitTarih) {
        this.tahlilKayitTarih = tahlilKayitTarih;
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
        if (!(object instanceof TahlilSonuc)) {
            return false;
        }
        TahlilSonuc other = (TahlilSonuc) object;
        if ((this.tarih == null && other.tarih != null) || (this.tarih != null && !this.tarih.equals(other.tarih))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.hastane_sistemi.TahlilSonuc[ tarih=" + tarih + " ]";
    }
    
}
