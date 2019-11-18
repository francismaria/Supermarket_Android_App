package feup.mieic.cmov.acme.connection;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import feup.mieic.cmov.acme.ui.profile.ProfileViewModel;

public class ProfileAction extends AsyncTask<String, Void, Boolean> {

    private JSONObject req;
    private JSONObject res;

    private ProfileViewModel model;

    public ProfileAction(ProfileViewModel model){
        this.model = model;
    }

    @Override
    protected void onPreExecute(){ }

    private void setRequestBody(String uuid){
        req = new JSONObject();

        try {
            req.put("UUID", uuid);
        } catch (JSONException e) {
            e.printStackTrace();
            req = null;
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {

        HttpURLConnection urlConnection = null;

        setRequestBody(params[0]);

        if(req == null) return false;     // error

        try {
            URL url = new URL(HTTPInfo.PROFILE_PATH);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(req.toString().getBytes("UTF-8"));
            os.close();

            Log.i("REGISTER ACTION", "request POST sent" + req.toString());

            int code = urlConnection.getResponseCode();

            if (code == HTTPInfo.SUCCESS_CODE) {
                Log.i("REGISTER ACTION", "OK");
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = rd.readLine()) != null) {
                    res = new JSONObject(line);
                    Log.i("PROFILE", res.toString());
                }
            } else {
                return false;
            }
        } catch(Exception e){
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(success){
            try {
                model.setName(res.getString("name"));
                model.setUsername(res.getString("username"));
                model.setEmail(res.getString("email"));
                model.setCCNumber(res.getString("cardNr"));
                model.setCCV(res.getString("cardCCV"));
                model.setCCExpDate(res.getString("cardExpDate"));
            } catch (JSONException e) {
                e.printStackTrace();
                // TODO - show toast saying that there was an internal error -> please reload??
            }
        } else {
            // TODO - show toast saying that there was an internal error -> please reload??
        }
    }
}
