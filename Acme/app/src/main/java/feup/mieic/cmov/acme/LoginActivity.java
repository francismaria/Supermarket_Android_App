package feup.mieic.cmov.acme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import feup.mieic.cmov.acme.connection.LoginAction;
import feup.mieic.cmov.acme.validation.TextValidator;

public class LoginActivity extends AppCompatActivity {

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setSupportActionBarIcon();

        toast =  Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
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


    /**
     * Starts a new LoginAction responsible for sending the login information
     * to the server.
     * @param view
     */
    public void submitLoginInformation(View view){
        String username = ((EditText)findViewById(R.id.loginUsername)).getText().toString();
        String password = ((EditText)findViewById(R.id.loginPassword)).getText().toString();

        Log.i("bool", Boolean.toString(isTextFieldEmpty(username)));

        if(isTextFieldEmpty(username) || isTextFieldEmpty(password)){
           toast.setText("Please fill both of the authentication fields.");
           toast.show();
        } else {
            new LoginAction(LoginActivity.this).execute(username, password);
        }
    }

    private boolean isTextFieldEmpty(String str){
        return str.equals("");
    }
}
