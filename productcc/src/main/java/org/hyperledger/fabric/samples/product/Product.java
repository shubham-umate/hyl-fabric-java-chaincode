package org.hyperledger.fabric.samples.product;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType
public class Product {

    @Property()
    private String productId;

    @Property()
    private String productName;

    @Property()
    private int productPrice;

    @Property()
    private String productOwner;

    @Property()
    private String productDetails;

    public Product(String productId, String productName, int productPrice, String productOwner, String productDetails) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productOwner = productOwner;
        this.productDetails = productDetails;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductOwner() {
        return productOwner;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductOwner(String productOwner) {
        this.productOwner = productOwner;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productOwner='" + productOwner + '\'' +
                ", productDetails='" + productDetails + '\'' +
                '}';
    }
}
