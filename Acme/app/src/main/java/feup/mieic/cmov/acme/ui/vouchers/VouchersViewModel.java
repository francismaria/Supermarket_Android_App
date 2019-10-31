package feup.mieic.cmov.acme.ui.vouchers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import feup.mieic.cmov.acme.connection.VouchersAction;

public class VouchersViewModel extends ViewModel {


    private MutableLiveData<Integer> vouchersNum;
    private VouchersAction action;

    public VouchersViewModel() {
        vouchersNum = new MutableLiveData<Integer>();

        this.action = new VouchersAction();

        vouchersNum.setValue(this.action.getVouchersAvailable());

    }

    public LiveData<Integer> getText() {
        return vouchersNum;
    }
}
