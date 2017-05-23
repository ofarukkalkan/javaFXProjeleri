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
@Table(name = "MUAYENE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Muayene.findAll", query = "SELECT m FROM Muayene m")
    , @NamedQuery(name = "Muayene.findByTarih", query = "SELECT m FROM Muayene m WHERE m.tarih = :tarih")})
public class Muayene implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "TARIH")
    private String tarih;
    @JoinColumn(name = "HASTALIK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Hastalik hastalikId;
    @JoinColumn(name = "RANDEVU_TARIH", referencedColumnName = "TARIH")
    @ManyToOne(optional = false)
    private Randevu randevuTarih;

    public Muayene() {
    }

    public Muayene(String tarih) {
        this.tarih = tarih;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public Hastalik getHastalikId() {
        return hastalikId;
    }

    public void setHastalikId(Hastalik hastalikId) {
        this.hastalikId = hastalikId;
    }

    public Randevu getRandevuTarih() {
        return randevuTarih;
    }

    public void setRandevuTarih(Randevu randevuTarih) {
        this.randevuTarih = randevuTarih;
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
        if (!(object instanceof Muayene)) {
            return false;
        }
        Muayene other = (Muayene) object;
        if ((this.tarih == null && other.tarih != null) || (this.tarih != null && !this.tarih.equals(other.tarih))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.hastane_sistemi.Muayene[ tarih=" + tarih + " ]";
    }
    
}
