/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espenv.storefront.Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vaden
 */
@Entity
@Table(name = "ITEMS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Items.findAll", query = "SELECT i FROM Items i WHERE i.sold = 0"),
    @NamedQuery(name = "Items.findById", query = "SELECT i FROM Items i WHERE i.id = :id"),
    @NamedQuery(name = "Items.findByUserid", query = "SELECT i FROM Items i WHERE i.userid = :userid"),
    @NamedQuery(name = "Items.findByTitle", query = "SELECT i FROM Items i WHERE i.title = :title"),
    @NamedQuery(name = "Items.findByDescription", query = "SELECT i FROM Items i WHERE i.description = :description"),
    @NamedQuery(name = "Items.findByPrice", query = "SELECT i FROM Items i WHERE i.price = :price")})
public class Items implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ID", nullable = false, length = 255)
    private String id;
    @Size(max = 255)
    @Column(name = "USERID", length = 255)
    private String userid;
    @Size(max = 255)
    @Column(name = "TITLE", length = 255)
    private String title;
    @Size(max = 255)
    @Column(name = "DESCRIPTION", length = 255)
    private String description;
    @Size(max = 255)
    @Column(name = "PRICE", length = 255)
    private String price;
    @Lob
    @Size(max = 15000000)
    @Column(name = "IMAGE", length = 15000000)
    private String image;
    @Size(max = 255)
    @Column(name = "SOLD", length = 255)
    private String sold;

    public Items() {
    }

    public Items(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getSold() {
        return sold;
    } 
    
    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        if (!(object instanceof Items)) {
            return false;
        }
        Items other = (Items) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.espenv.storefront.Entities.Items[ id=" + id + " ]";
    }
    
}
