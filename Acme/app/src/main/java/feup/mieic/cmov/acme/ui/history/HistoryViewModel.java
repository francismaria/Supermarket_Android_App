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

import feup.mieic.cmov.acme.connection.HistoryAction;

public class HistoryViewModel extends ViewModel {

    private static final String ERROR_MSG = "Unavailable. Please try reloading this page";
    private MutableLiveData<String> mErrorMsg;
    private MutableLiveData<Boolean> load;
    private List<ItemModel> itemsParsed;

    public HistoryViewModel() {
        mErrorMsg = new MutableLiveData<>();
        load = new MutableLiveData<>();

        new HistoryAction(this).execute();
    }

    public LiveData<String> getError() {
        return mErrorMsg;
    }

    private void parseResponse(JSONArray items) throws JSONException {
        itemsParsed = new ArrayList<>();

        for(int i = 0; i < items.length(); i++){
            JSONObject jsonItem = items.getJSONObject(i);

            ItemModel item = new ItemModel(jsonItem.getString("id"), jsonItem.getString("total_cost"), jsonItem.getString("date"));

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