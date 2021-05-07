package com.rafsan.inventory.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String productName;
    @Column(name = "price")
    private double price;
    @Column(name = "quantity")
    private double quantity;
    @Column(name = "description")
    private String description;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "categoryId")
    private Category category;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "supplierId")
    private Supplier supplier;
    @Column(name = "codigo_barra")
    private long codigo_barra;
    @Column(name = "costo_adqui")
    private double costo_adqui;
    
    public Product() {}

    public Product(long id,String productName,double price,double quantity,String description,
            Category category,Supplier supplier,long codigo_barra,double costo_adqui) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category;
        this.supplier = supplier;
        this.codigo_barra = codigo_barra;
        this.costo_adqui = costo_adqui;
    }

    public Product(String productName,double price,double quantity, String description,
            Category category, Supplier supplier,long codigo_barra, double costo_adqui) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category;
        this.supplier = supplier;
        this.codigo_barra = codigo_barra;
        this.costo_adqui = costo_adqui;        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public long getCodigo_barra() {
        return codigo_barra;
    }

    public void setCodigo_barra(long codigo_barra) {
        this.codigo_barra = codigo_barra;
    }

    public double getCosto_adqui() {
        return costo_adqui;
    }
 
    public void setCosto_adqui(double costo_adqui) {
        this.costo_adqui = costo_adqui;
    }

    @Override
    public String toString() {
        return "Product{" + " id=" + id + 
            ", productName=" + productName + 
            ", price=" + price + 
            ", quantity=" + quantity + 
            ", description=" + description + 
            ", category=" + category + 
            ", supplier=" + supplier + 
            ", codigo_barra="+ codigo_barra +
            ", costo_adqui=" + costo_adqui +'}';
    }
}
