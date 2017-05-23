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
@Table(name = "TAHLIL_KAYIT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TahlilKayit.findAll", query = "SELECT t FROM TahlilKayit t")
    , @NamedQuery(name = "TahlilKayit.findByTarih", query = "SELECT t FROM TahlilKayit t WHERE t.tarih = :tarih")})
public class TahlilKayit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "TARIH")
    private String tarih;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tahlilKayitTarih")
    private Collection<TahlilSonuc> tahlilSonucCollection;
    @JoinColumn(name = "RANDEVU_TARIH", referencedColumnName = "TARIH")
    @ManyToOne(optional = false)
    private Randevu randevuTarih;
    @JoinColumn(name = "TAHLIL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TahlilTur tahlilId;

    public TahlilKayit() {
    }

    public TahlilKayit(String tarih) {
        this.tarih = tarih;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    @XmlTransient
    public Collection<TahlilSonuc> getTahlilSonucCollection() {
        return tahlilSonucCollection;
    }

    public void setTahlilSonucCollection(Collection<TahlilSonuc> tahlilSonucCollection) {
        this.tahlilSonucCollection = tahlilSonucCollection;
    }

    public Randevu getRandevuTarih() {
        return randevuTarih;
    }

    public void setRandevuTarih(Randevu randevuTarih) {
        this.randevuTarih = randevuTarih;
    }

    public TahlilTur getTahlilId() {
        return tahlilId;
    }

    public void setTahlilId(TahlilTur tahlilId) {
        this.tahlilId = tahlilId;
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
        if (!(object instanceof TahlilKayit)) {
            return false;
        }
        TahlilKayit other = (TahlilKayit) object;
        if ((this.tarih == null && other.tarih != null) || (this.tarih != null && !this.tarih.equals(other.tarih))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.hastane_sistemi.TahlilKayit[ tarih=" + tarih + " ]";
    }
    
}
