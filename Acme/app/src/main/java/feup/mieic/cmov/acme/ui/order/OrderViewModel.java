package feup.mieic.cmov.acme.ui.order;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import feup.mieic.cmov.acme.connection.OrderAction;

public class OrderViewModel extends ViewModel {

    private static final String ERROR_MSG = "Unavailable. Please try reloading this page";
    private MutableLiveData<String> mError;
    private MutableLiveData<Boolean> load;
    private List<ProductModel> products;

    public OrderViewModel(){
        mError = new MutableLiveData<>();
        load = new MutableLiveData<>();
        products = new ArrayList<>();
    }

    public void sendRequest(int orderID){
        new OrderAction(this, orderID).execute();
    }


    public MutableLiveData<Boolean> getLoad(){
        return load;
    }

    public MutableLiveData<String> getError(){
        return mError;
    }

    public List<ProductModel> getProducts(){
        return products;
    }


}
