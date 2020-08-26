package com.inderproduction.theinderapp.Modals;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Shoes implements Parcelable {
    private String itemID;
    private String itemCategory;
    private String itemName;
    private String itemBrand;
    private double itemPrice;
    private int discount;
    private int sizeChartType;
    private int size;
    private List<String> sizesAvailable;
    private List<String> availableColors;
    private List<Integer> availableImages;
    private String productDetail;
    private String productMaterial;
    private String productImage;
    private String shoeType;
    private String soleType;
    private String gender;
    private int cartCount = 1;

    public Shoes() {}


    protected Shoes(Parcel in) {
        itemID = in.readString();
        itemCategory = in.readString();
        itemName = in.readString();
        itemBrand = in.readString();
        itemPrice = in.readDouble();
        discount = in.readInt();
        sizeChartType = in.readInt();
        size = in.readInt();
        sizesAvailable = in.createStringArrayList();
        availableColors = in.createStringArrayList();
        productDetail = in.readString();
        productMaterial = in.readString();
        productImage = in.readString();
        shoeType = in.readString();
        soleType = in.readString();
        gender = in.readString();
        cartCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemID);
        dest.writeString(itemCategory);
        dest.writeString(itemName);
        dest.writeString(itemBrand);
        dest.writeDouble(itemPrice);
        dest.writeInt(discount);
        dest.writeInt(sizeChartType);
        dest.writeInt(size);
        dest.writeStringList(sizesAvailable);
        dest.writeStringList(availableColors);
        dest.writeString(productDetail);
        dest.writeString(productMaterial);
        dest.writeString(productImage);
        dest.writeString(shoeType);
        dest.writeString(soleType);
        dest.writeString(gender);
        dest.writeInt(cartCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Shoes> CREATOR = new Creator<Shoes>() {
        @Override
        public Shoes createFromParcel(Parcel in) {
            return new Shoes(in);
        }

        @Override
        public Shoes[] newArray(int size) {
            return new Shoes[size];
        }
    };

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getSizeChartType() {
        return sizeChartType;
    }

    public void setSizeChartType(int sizeChartType) {
        this.sizeChartType = sizeChartType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getSizesAvailable() {
        return sizesAvailable;
    }

    public void setSizesAvailable(List<String> sizesAvailable) {
        this.sizesAvailable = sizesAvailable;
    }

    public List<String> getAvailableColors() {
        return availableColors;
    }

    public void setAvailableColors(List<String> availableColors) {
        this.availableColors = availableColors;
    }

    public List<Integer> getAvailableImages() {
        return availableImages;
    }

    public void setAvailableImages(List<Integer> availableImages) {
        this.availableImages = availableImages;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getProductMaterial() {
        return productMaterial;
    }

    public void setProductMaterial(String productMaterial) {
        this.productMaterial = productMaterial;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getShoeType() {
        return shoeType;
    }

    public void setShoeType(String shoeType) {
        this.shoeType = shoeType;
    }

    public String getSoleType() {
        return soleType;
    }

    public void setSoleType(String soleType) {
        this.soleType = soleType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }
}
