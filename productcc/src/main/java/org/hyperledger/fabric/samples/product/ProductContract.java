package org.hyperledger.fabric.samples.product;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;


@Contract(
        name = "productcc",
        info = @Info(
                title = "Product Chaincode",
                description = "The hyperleger Chaincode",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "a.transfer@example.com",
                        name = "Adrian Transfer",
                        url = "https://hyperledger.example.com")))
@Default
public final class ProductContract implements ContractInterface  {

    private final Gson gson = new Gson();

    private enum ProductContractErrors {
        PRODUCT_NOT_FOUND,
        PRODUCT_ALREADY_EXISTS
    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        CreateProduct(ctx, "product1", "Iphone", 5000, "Tomoko", "Apple");
        CreateProduct(ctx, "product2", "MacBook", 7000, "Brad", "Apple");
        CreateProduct(ctx, "product3", "Prestige", 4000, "Jin Soo", "MSI");
        CreateProduct(ctx, "product4", "Predator", 3000, "Max", "Acer");
        CreateProduct(ctx, "product5", "AlienWare", 4000, "Adrian", "Dell");
        CreateProduct(ctx, "product6", "ROG", 35000, "Michel", "Asus");

    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Product CreateProduct(final Context ctx, final String productID, final String productName, final int productPrice,
                                 final String productOwner, final String productDetails) {
        ChaincodeStub stub = ctx.getStub();

        if (ProductExists(ctx, productID)) {
            String errorMessage = String.format("Product %s already exists", productID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ProductContractErrors.PRODUCT_ALREADY_EXISTS.toString());
        }

        Product product = new Product(productID, productName, productPrice, productOwner, productDetails);
        String productJSON = gson.toJson(product);
        stub.putStringState(productID, productJSON);

        return product;
    }


    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Product ReadProduct(final Context ctx, final String productID) {
        ChaincodeStub stub = ctx.getStub();
        String productJSON = stub.getStringState(productID);

        if (productJSON == null || productJSON.isEmpty()) {
            String errorMessage = String.format("Product %s does not exist", productID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ProductContractErrors.PRODUCT_NOT_FOUND.toString());
        }

        Product product = gson.fromJson(productJSON, Product.class);
        return product;
    }




    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Product UpdateProduct(final Context ctx, final String productID, final String productName, final int productPrice,
                             final String productOwner, final String productDetails) {
        ChaincodeStub stub = ctx.getStub();

        if (!ProductExists(ctx, productID)) {
            String errorMessage = String.format("Product %s does not exist", productID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ProductContractErrors.PRODUCT_NOT_FOUND.toString());
        }

        Product newProduct = new Product(productID, productName, productPrice, productOwner, productDetails);
        String newProductJSON = gson.toJson(newProduct);
        stub.putStringState(productID, newProductJSON);

        return newProduct;
    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteProduct(final Context ctx, final String productID) {
        ChaincodeStub stub = ctx.getStub();

        if (!ProductExists(ctx, productID)) {
            String errorMessage = String.format("Product %s does not exist", productID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ProductContractErrors.PRODUCT_NOT_FOUND.toString());
        }

        stub.delState(productID);
    }





    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean ProductExists(final Context ctx, final String productID) {
        ChaincodeStub stub = ctx.getStub();
        String productJSON = stub.getStringState(productID);

        return (productJSON != null && !productJSON.isEmpty());
    }



    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllProducts(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Product> queryResults = new ArrayList<Product>();

        // To retrieve all products from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'product0', endKey = 'product9' ,
        // then getStateByRange will retrieve product with keys between product0 (inclusive) and product9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result: results) {
            Product product = gson.fromJson(result.getStringValue(), Product.class);
            queryResults.add(product);
            System.out.println(product);
        }

        final String response = gson.toJson(queryResults);

        return response;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String DeleteAllProducts(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Product> queryResults = new ArrayList<Product>();

        // To retrieve all products from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'product0', endKey = 'product9' ,
        // then getStateByRange will retrieve product with keys between product0 (inclusive) and product9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result: results) {
            Product product = gson.fromJson(result.getStringValue(), Product.class);
            queryResults.add(product);
            System.out.println(product);
        }

        final String response = gson.toJson(queryResults);

        Type productTypeList = new TypeToken<ArrayList<Product>>(){}.getType();

        List<Product> productList = gson.fromJson(response,productTypeList);

        for(Product product: productList){
            stub.delState(product.getProductId());
        }

        return response;
    }



}


