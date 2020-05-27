package com.example.adminstrator;

public class HomeMo {
    private String itemImage;
    private String itemName;
    private String itemPricekg;
    private String itemPricepcs;
    private String itemId;

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPricekg() {
        return itemPricekg;
    }

    public void setItemPricekg(String itemPricekg) {
        this.itemPricekg = itemPricekg;
    }

    public String getItemPricepcs() {
        return itemPricepcs;
    }

    public void setItemPricepcs(String itemPricepcs) {
        this.itemPricepcs = itemPricepcs;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public HomeMo() {
    }

    public HomeMo(String itemImage, String itemName, String itemPricekg, String itemPricepcs, String itemId) {
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemPricekg = itemPricekg;
        this.itemPricepcs = itemPricepcs;
        this.itemId = itemId;
    }
}

