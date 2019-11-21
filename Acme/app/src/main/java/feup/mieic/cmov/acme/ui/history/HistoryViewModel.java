package feup.mieic.cmov.acme.ui.history;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import feup.mieic.cmov.acme.connection.HistoryAction;
import feup.mieic.cmov.acme.security.SharedPrefsHolder;

public class HistoryViewModel extends ViewModel {

    private static final String ERROR_MSG = "Unavailable. Please try reloading this page";
    private MutableLiveData<String> mErrorMsg;
    private MutableLiveData<Boolean> load;
    private List<ItemModel> itemsParsed;
    private String uuid;

    public HistoryViewModel() {
        mErrorMsg = new MutableLiveData<>();
        load = new MutableLiveData<>();
    }

    public void setUUID(String uuid){
        this.uuid = uuid;
    }

    public void sendRequest(){
        new HistoryAction(this).execute(uuid);
    }

    public LiveData<String> getError() {
        return mErrorMsg;
    }

    private String roundDouble(String res) throws JSONException {
        Double totalCostValue = Double.parseDouble(res);
        totalCostValue = Math.round(totalCostValue * 100.0) / 100.0;
        return Double.toString(totalCostValue);
    }

    private void parseResponse(JSONArray items) throws JSONException {
        itemsParsed = new ArrayList<>();

        for(int i = 0; i < items.length(); i++){
            JSONObject jsonItem = items.getJSONObject(i);

            ItemModel item = new ItemModel(jsonItem.getString("id"), roundDouble(jsonItem.getString("total_cost")), jsonItem.getString("date"));
            itemsParsed.add(item);
        }
    }

    public List<ItemModel> getItems(){
        return itemsParsed;
    }

    public MutableLiveData<Boolean> getLoad(){
        return load;
    }

    public void setRequestResult(JSONArray items){
        if(items == null){
            mErrorMsg.setValue(ERROR_MSG);
            return;
        }

        try {
            parseResponse(items);
        } catch (JSONException e) {
            e.printStackTrace();
            mErrorMsg.setValue(ERROR_MSG);
            return;
        }
        load.setValue(true);
    }
}