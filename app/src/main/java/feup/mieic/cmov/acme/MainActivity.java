package feup.mieic.cmov.acme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
    }

    public void showLoginActivity(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void showRegisterActivity(View view){
        // show register activity
    }


}
