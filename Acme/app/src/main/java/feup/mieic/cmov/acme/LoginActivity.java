package feup.mieic.cmov.acme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.URL;

import feup.mieic.cmov.acme.connect.LoginAction;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setSupportActionBarIcon();
    }

    /**
     * Customizes the support action bar.
     */
    private void setSupportActionBarIcon(){
        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setIcon(R.mipmap.brand_logo_foreground);
            bar.setDisplayShowHomeEnabled(true);
            bar.setTitle("Log In");
        }
    }

    public void submitLoginInformation(View view){
        try{
            new LoginAction().execute(new URL("http://localhost:8080/AcmeServer/acme/service"),
                    new URL("http://localhost:8080/AcmeServer/acme/service"),
                    new URL("http://localhost:8080/AcmeServer/acme/service"));
        } catch(IOException e){
            System.out.println("URL ERROR");
        }

        //Log.d("RESULT:", );
    }
}
