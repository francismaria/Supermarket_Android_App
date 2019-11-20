package feup.mieic.cmov.acme.connection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import feup.mieic.cmov.acme.ui.contact.ContactViewModel;

public class ContactAction extends AsyncTask<String, Void, Boolean> {

    private JSONObject req;
    private ContactViewModel model;

    public ContactAction(ContactViewModel model){
        this.model = model;
    }

    @Override
    protected void onPreExecute(){ }

    private void setRequestBody(String uuid, String msg){
        req = new JSONObject();

        try {
            req.put("UUID", uuid);
            req.put("msg", msg);
        } catch (JSONException e) {
            e.printStackTrace();
            req = null;
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        setRequestBody(params[0], params[1]);

        if(req == null) return false;

        try {
            URL url = new URL(HTTPInfo.CONTACT_PATH);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(req.toString().getBytes("UTF-8"));
            os.close();

            Log.i("CONTACT ACTION", "request POST sent" + req.toString());

            int code = urlConnection.getResponseCode();

            if (code == HTTPInfo.SUCCESS_CODE) {
                Log.i("CONTACT ACTION", "OK");
            } else {
                Log.i("CONTACT ACTION", "SERVER ERROR");
                return false;
            }
        } catch(Exception e){
            e.printStackTrace();
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
            model.setRequestResult(true);
        } else {
            model.setRequestResult(false);
        }
    }
}
