package com.socialcodia.social_shopia.model;

public class ModelProduct {
    public String productTitle,productDescription,productQuantity,productPrice,productDiscount,productDiscountNote,productImage,productId,discountAvailable,shopId;

    public ModelProduct() {
    }

    public ModelProduct(String productTitle, String productDescription, String productQuantity, String productPrice, String productDiscount, String productDiscountNote, String productImage, String productId, String discountAvailable, String shopId) {
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productDiscountNote = productDiscountNote;
        this.productImage = productImage;
        this.productId = productId;
        this.discountAvailable = discountAvailable;
        this.shopId = shopId;
    }


    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductDiscountNote() {
        return productDiscountNote;
    }

    public void setProductDiscountNote(String productDiscountNote) {
        this.productDiscountNote = productDiscountNote;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(String discountAvailable) {
        this.discountAvailable = discountAvailable;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
