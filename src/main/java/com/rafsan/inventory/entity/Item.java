package com.rafsan.inventory.entity;

public class Item {
    private String itemName;
    private double unitPrice;
    private double quantity;
    private double total;
    private long codigo_barra;

    public Item() {}

    public Item(String itemName, double unitPrice, double quantity, double total, long codigo_barra) {
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.total = total;
        this.codigo_barra = codigo_barra;
    }

    public long getCodigo_barra() {
        return codigo_barra;
    }

    public void setCodigo_barra(long codigo_barra) {
        this.codigo_barra = codigo_barra;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Item{" + "itemName=" + itemName + 
                ", unitPrice=" + unitPrice + 
                ", quantity=" + quantity + 
                ", total=" + total + 
                ", codigo_barra =" + codigo_barra +'}';
    }
}
