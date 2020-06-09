package com.example.nitipaja.ui.category;

public class CategoryModel {

    private String itemID, itemName, itemLocation, itemPrice, itemDescription, itemCategory, itemQuantity, userID, imageURL, itemStatus, takenOrderBy;

    public CategoryModel() {
    }

    public CategoryModel(String itemID, String itemName, String itemLocation, String itemPrice, String itemDescription, String itemCategory, String itemQuantity, String userID, String imageURL, String itemStatus, String takenOrderBy) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
        this.itemCategory = itemCategory;
        this.itemQuantity = itemQuantity;
        this.userID = userID;
        this.imageURL = imageURL;
        this.itemStatus = itemStatus;
        this.takenOrderBy = takenOrderBy;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getTakenOrderBy() {
        return takenOrderBy;
    }

    public void setTakenOrderBy(String takenOrderBy) {
        this.takenOrderBy = takenOrderBy;
    }

    public String getItemQuantity() { return itemQuantity; }

    public void setItemQuantity(String itemQuantity) { this.itemQuantity = itemQuantity; }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
}
