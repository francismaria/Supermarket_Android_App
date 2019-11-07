package feup.mieic.cmov.acme.ui.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import feup.mieic.cmov.acme.connection.HTTPInfo;
import feup.mieic.cmov.acme.connection.ProfileAction;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mName;
    private MutableLiveData<String> mUsername;
    private MutableLiveData<String> mEmail;
    private MutableLiveData<String> mCCV;
    private MutableLiveData<String> mExpDate;
    private MutableLiveData<String> mNumber;

    private void initData(){
        mName = new MutableLiveData<>();
        mUsername = new MutableLiveData<>();
        mEmail = new MutableLiveData<>();
        mCCV = new MutableLiveData<>();
        mExpDate = new MutableLiveData<>();
        mNumber = new MutableLiveData<>();
    }

    public ProfileViewModel() {
        initData();
        new ProfileAction(this).execute();
    }

    // Name

    public void setName(String val){
        mName.setValue(val);
    }
    public LiveData<String> getNameTextView() {
        return mName;
    }

    // Username

    public void setUsername(String val){
        mUsername.setValue(val);
    }
    public LiveData<String> getUsernameTextView() {
        return mUsername;
    }

    // Email

    public void setEmail(String val){
        mEmail.setValue(val);
    }
    public LiveData<String> getEmailTextView() {
        return mEmail;
    }

    // CC Number

    public void setCCNumber(String val){
        mNumber.setValue(val);
    }
    public LiveData<String> getCCNumberTextView() {
        return mNumber;
    }

    // CC CCV

    public void setCCV(String val){
        mCCV.setValue(val);
    }
    public LiveData<String> getCCVTextView() {
        return mCCV;
    }

    // CC Exp Date
    public void setCCExpDate(String val){
        mExpDate.setValue(val);
    }
    public LiveData<String> getExpDateTextView() {
        return mExpDate;
    }


}