package feup.mieic.cmov.acme.ui.cart;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import feup.mieic.cmov.acme.ui.order.ProductModel;

public class CartViewModel extends ViewModel {

    private static final int MAX_SIZE = 10;
    // This will serve as a database to store the products
    private static MutableLiveData<List<ProductModel>> cart = new MutableLiveData<>();

    public static MutableLiveData<List<ProductModel>> getCart() {
        return cart;
    }

    public static void addProduct(ProductModel prod){
        if(cart.getValue() == null){
            cart.setValue(new ArrayList<ProductModel>());
        }
        if(cart.getValue().size() == MAX_SIZE){
            return;
        }

        cart.getValue().add(prod);
    }

    public static void removeProduct(String id){
        List<ProductModel> prods = cart.getValue();
        List<ProductModel> newProds = new ArrayList<>();

        for(ProductModel p : prods){
            if(!p.getID().equals(id))
                newProds.add(p);

        }
        cart.setValue(newProds);
    }
}
