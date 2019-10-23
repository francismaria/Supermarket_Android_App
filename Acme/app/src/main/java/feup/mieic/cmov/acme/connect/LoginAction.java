package feup.mieic.cmov.acme.connect;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

public class LoginAction extends AsyncTask<URL, Integer, Long > {

    protected Long doInBackground(URL... urls){
        return 1L;
    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
        Log.d("PROGRESS", progress[0].toString());
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
        Log.d("FINISHED", result.toString());
    }
}
