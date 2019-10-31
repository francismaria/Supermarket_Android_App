package feup.mieic.cmov.acme.connection;

import androidx.lifecycle.MutableLiveData;

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

public class VouchersAction {

    private Integer vouchersNum;

    public VouchersAction(){}

    public Integer getVouchersAvailable(){

        vouchersNum = 4;
        // httpConnection
        // parse JSON result from server


        return vouchersNum;
    }
}
