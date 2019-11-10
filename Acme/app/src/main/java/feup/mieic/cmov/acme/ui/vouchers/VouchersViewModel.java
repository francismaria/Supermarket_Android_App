package feup.mieic.cmov.acme.ui.vouchers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import feup.mieic.cmov.acme.connection.VouchersAction;

public class VouchersViewModel extends ViewModel {

    private MutableLiveData<Boolean> load;
    private Integer vouchersNum;
    private boolean success;

    public VouchersViewModel() {
        load = new MutableLiveData<>();
        success = true;

        new VouchersAction(this).execute();
    }

    public LiveData<Boolean> getLoad() {
        return load;
    }

    public int getVouchersNum(){ return vouchersNum; }

    public void setVouchersNum(Integer vouchersNum){
        if(vouchersNum == -1){
            success = false;
        }
        this.vouchersNum = vouchersNum;
        load.setValue(true);
    }

    public boolean isRequestSuccessful(){
        return success;
    }
}
