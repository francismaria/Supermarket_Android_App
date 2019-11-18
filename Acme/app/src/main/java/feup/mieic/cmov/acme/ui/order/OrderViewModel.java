package feup.mieic.cmov.acme.ui.order;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import feup.mieic.cmov.acme.connection.OrderAction;

public class OrderViewModel extends ViewModel {

    private String id;
    private String date;
    private String totalCost;
    private String vouchers;


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

    public String getOrderID(){
        return id;
    }

    public String getOrderDate(){
        return date;
    }

    public String getOrderTotalCost(){
        return totalCost;
    }

    public String getVouchers(){
        return vouchers;
    }

    private void parseRawProducts(JSONArray rawProds) throws JSONException {
        products = new ArrayList<>();

        for(int i = 0; i < rawProds.length(); i++){
            JSONObject prodItem = rawProds.getJSONObject(i);
            UUID productID = UUID.fromString(prodItem.getString("productID"));

            ProductModel item = new ProductModel(productID, prodItem.getString("productName"), prodItem.getString("productQty"), prodItem.getString("productPrice"));
            products.add(item);
        }
    }

    public void setRequestResult(String id, String date, String totalCost, String vouchers, JSONArray rawProds){
        this.id = id;
        this.date = date;
        this.totalCost = totalCost;
        this.vouchers = vouchers;

        try{
            parseRawProducts(rawProds);
        } catch(Exception e){
            flagError();
            return;
        }
        load.setValue(true);
    }

    public void flagError(){
        mError.setValue(ERROR_MSG);
    }


}
