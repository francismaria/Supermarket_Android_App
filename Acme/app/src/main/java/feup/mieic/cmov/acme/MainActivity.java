package feup.mieic.cmov.acme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        //generateKeyPair("");
    }


    // TODO: remove this code


    private PrivateKey pri;
    private PublicKey pub;
    private static final int KEY_SIZE = 512;
    private static final String KEY_ALGO = "RSA";
    private static final int CERT_SERIAL = 12121212;
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    private boolean generateKeyPair(String keyname) {

        keyname = keyname + "_key";

        try {
            Calendar start = new GregorianCalendar();
            Calendar end = new GregorianCalendar();
            // set the end date to 20 years from now
            end.add(Calendar.YEAR, 20);

            KeyPairGenerator kgen = null;
            kgen = KeyPairGenerator.getInstance(KEY_ALGO, ANDROID_KEYSTORE);

            AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(this)
                    .setKeySize(KEY_SIZE)
                    .setAlias("example")                    // this can not be hardcoded
                    .setSubject(new X500Principal("CN=" + keyname))
                    .setSerialNumber(BigInteger.valueOf(CERT_SERIAL))
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            kgen.initialize(spec);

            KeyPair kp = kgen.generateKeyPair();
            pri = kp.getPrivate();
            pub = kp.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void showLoginActivity(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void showRegisterActivity(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }


}
