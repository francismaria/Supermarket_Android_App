package feup.mieic.cmov.acme.ui.vouchers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import feup.mieic.cmov.acme.connection.VouchersAction;

public class VouchersViewModel extends ViewModel {

    private MutableLiveData<Boolean> load;
    private Integer vouchersNum;
    private boolean success;
    private String uuid;

    public VouchersViewModel() {
        load = new MutableLiveData<>();
        success = true;
    }

    public void setUUID(String uuid){
        this.uuid = uuid;
    }

    public void sendRequest(){
        new VouchersAction(this).execute(uuid);
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
