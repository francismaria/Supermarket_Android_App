package feup.mieic.cmov.acme.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import feup.mieic.cmov.acme.connection.HistoryAction;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");

        new HistoryAction(this).execute();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setRequestResult(List<ItemModel> items){
        if(items == null){
            // TODO : show unavailable toast
        } else {
            // TODO : update adapter
        }
    }
}