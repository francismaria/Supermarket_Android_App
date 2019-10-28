package feup.mieic.cmov.acme.connection;

import android.os.AsyncTask;
import android.util.Log;

public class RegisterAction extends AsyncTask<String, Void, Boolean>  {

    private static final String REGISTER_PATH = "http://10.0.2.2:8080/AcmeServer/acme/api/register";

    // HTTP response codes
    private static final int SUCCESS_CODE = 200;
    private static final int UNAUTHORIZED_CODE = 401;

    @Override
    protected void onPreExecute(){}

    @Override
    protected Boolean doInBackground(String... params) {
        Log.i("REGISTER", "was called");
        return true;
    }

    @Override
    protected void onPostExecute(Boolean error) {}
}
