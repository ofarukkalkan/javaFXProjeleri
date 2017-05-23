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
@Table(name = "RECETE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recete.findAll", query = "SELECT r FROM Recete r")
    , @NamedQuery(name = "Recete.findByTarih", query = "SELECT r FROM Recete r WHERE r.tarih = :tarih")})
public class Recete implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "TARIH")
    private String tarih;
    @JoinColumn(name = "ILAC_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Ilac ilacId;
    @JoinColumn(name = "RANDEVU_TARIH", referencedColumnName = "TARIH")
    @ManyToOne(optional = false)
    private Randevu randevuTarih;

    public Recete() {
    }

    public Recete(String tarih) {
        this.tarih = tarih;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public Ilac getIlacId() {
        return ilacId;
    }

    public void setIlacId(Ilac ilacId) {
        this.ilacId = ilacId;
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
        if (!(object instanceof Recete)) {
            return false;
        }
        Recete other = (Recete) object;
        if ((this.tarih == null && other.tarih != null) || (this.tarih != null && !this.tarih.equals(other.tarih))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.hastane_sistemi.Recete[ tarih=" + tarih + " ]";
    }
    
}
