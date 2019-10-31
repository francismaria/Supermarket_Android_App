package feup.mieic.cmov.acme.ui.vouchers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class VouchersViewModel extends ViewModel {


    private MutableLiveData<Integer> vouchersNum;
    private VouchersAction action;

    // TO BE DELETED
    //private MutableLiveData<String> mText;



    public VouchersViewModel() {
        vouchersNum = new MutableLiveData<Integer>();

        this.action = new VouchersAction();

        vouchersNum.setValue(this.action.getVouchersAvailable());

        /*
        mText = new MutableLiveData<>();
        mText.setValue("This is tools fragment");*/
    }

    public LiveData<Integer> getText() {
        return vouchersNum;
    }
}
