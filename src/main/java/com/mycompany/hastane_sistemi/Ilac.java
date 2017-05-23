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
@Table(name = "ILAC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ilac.findAll", query = "SELECT i FROM Ilac i")
    , @NamedQuery(name = "Ilac.findById", query = "SELECT i FROM Ilac i WHERE i.id = :id")
    , @NamedQuery(name = "Ilac.findByProspektus", query = "SELECT i FROM Ilac i WHERE i.prospektus = :prospektus")})
public class Ilac implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @Column(name = "PROSPEKTUS")
    private String prospektus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ilacId")
    private Collection<Recete> receteCollection;

    public Ilac() {
    }

    public Ilac(String id) {
        this.id = id;
    }

    public Ilac(String id, String prospektus) {
        this.id = id;
        this.prospektus = prospektus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProspektus() {
        return prospektus;
    }

    public void setProspektus(String prospektus) {
        this.prospektus = prospektus;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ilac)) {
            return false;
        }
        Ilac other = (Ilac) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.hastane_sistemi.Ilac[ id=" + id + " ]";
    }
    
}
