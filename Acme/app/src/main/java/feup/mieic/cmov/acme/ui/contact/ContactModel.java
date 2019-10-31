package feup.mieic.cmov.acme.ui.contact;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContactModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ContactModel() {
        mText = new MutableLiveData<>();
        mText.setValue("We're happy to hear your opinion about the app. If you have any problem or just want to leave a recomendation please send us a message using the " +
                "form below.\nWe will answer you as soon as possible.\nThank you.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}