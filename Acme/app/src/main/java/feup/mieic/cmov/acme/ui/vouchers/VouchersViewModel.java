package feup.mieic.cmov.acme.ui.vouchers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VouchersViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public VouchersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tools fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
