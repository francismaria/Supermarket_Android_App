package feup.mieic.cmov.acme.ui.vouchers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import feup.mieic.cmov.acme.connection.VouchersAction;

public class VouchersViewModel extends ViewModel {

    private MutableLiveData<Boolean> load;
    private MutableLiveData<Integer> vouchersNum;
    private VouchersAction action;

    public VouchersViewModel() {
        vouchersNum = new MutableLiveData<>();

        this.action = new VouchersAction();

        vouchersNum.setValue(this.action.getVouchersAvailable());
    }



    public LiveData<Boolean> getText() {
        return load;
    }
}
