package feup.mieic.cmov.acme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;
import android.view.View;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        if(userLoggedIn()){
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    private boolean userLoggedIn(){
        SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if(settings.getString("current_user", null) != null)
            return true;
        return false;
    }

    public void showLoginActivity(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void showRegisterActivity(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }


}
