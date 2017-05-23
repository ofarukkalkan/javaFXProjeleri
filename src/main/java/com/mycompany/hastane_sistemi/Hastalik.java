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
@Table(name = "HASTALIK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hastalik.findAll", query = "SELECT h FROM Hastalik h")
    , @NamedQuery(name = "Hastalik.findById", query = "SELECT h FROM Hastalik h WHERE h.id = :id")
    , @NamedQuery(name = "Hastalik.findByDetay", query = "SELECT h FROM Hastalik h WHERE h.detay = :detay")})
public class Hastalik implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @Column(name = "DETAY")
    private String detay;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hastalikId")
    private Collection<Muayene> muayeneCollection;

    public Hastalik() {
    }

    public Hastalik(String id) {
        this.id = id;
    }

    public Hastalik(String id, String detay) {
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
    public Collection<Muayene> getMuayeneCollection() {
        return muayeneCollection;
    }

    public void setMuayeneCollection(Collection<Muayene> muayeneCollection) {
        this.muayeneCollection = muayeneCollection;
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
        if (!(object instanceof Hastalik)) {
            return false;
        }
        Hastalik other = (Hastalik) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.hastane_sistemi.Hastalik[ id=" + id + " ]";
    }
    
}
