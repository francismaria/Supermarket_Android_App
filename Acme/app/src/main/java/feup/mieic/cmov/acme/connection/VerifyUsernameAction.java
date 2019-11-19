package feup.mieic.cmov.acme.connection;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class VerifyUsernameAction extends AsyncTask<String, Void, Boolean> {

    private JSONObject req;
    private JSONObject res;
    private String MSG = "";

    public VerifyUsernameAction(){
        req = new JSONObject();
    }

    public void setRequestObject(String username) throws JSONException {
        req.put("username", username);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        boolean unique = false;

        try {
            URL url = new URL(HTTPInfo.LOGIN_PATH);

            setRequestObject(params[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(req.toString().getBytes("UTF-8"));
            os.close();

            int code = urlConnection.getResponseCode();

            switch(code){
                case HTTPInfo.SUCCESS_CODE:
                    BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;

                    while ((line = rd.readLine()) != null) {
                        res = new JSONObject(line);
                    }
                    unique = true;
                    break;
                case HTTPInfo.UNAUTHORIZED_CODE:
                    MSG = "The username already exists";
                    res = null;
                    unique = false;
                    break;
                case HTTPInfo.INTERNAL_ERROR_CODE:
                    MSG = "Internal server error. Please try again";
                    res = null;
                    unique = false;
                    break;
                default:
                    unique = false;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return unique;
    }

    @Override
    protected void onPostExecute(Boolean unique) {
        super.onPostExecute(unique);
    }
}
