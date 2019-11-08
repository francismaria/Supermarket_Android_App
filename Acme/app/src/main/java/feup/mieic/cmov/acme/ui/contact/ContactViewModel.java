package feup.mieic.cmov.acme.ui.contact;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import feup.mieic.cmov.acme.connection.ContactAction;

public class ContactViewModel extends ViewModel {

    private boolean requestFinished;
    private MutableLiveData<Boolean> result;

    public ContactViewModel() {
        result = new MutableLiveData<>();
        requestFinished = true;
    }


    public void sendMessage(String msg){
        requestFinished = false;
        new ContactAction(this).execute(msg);
    }

    public boolean isRequestFinished() {
        return requestFinished;
    }

    public void setRequestResult(boolean res){
        result.setValue(res);
        requestFinished = true;
    }

    public LiveData<Boolean> getRequestResult() {
        return result;
    }
}