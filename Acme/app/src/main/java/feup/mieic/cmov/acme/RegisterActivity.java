package feup.mieic.cmov.acme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;

import feup.mieic.cmov.acme.validation.TextValidator;

public class RegisterActivity extends AppCompatActivity {

    private URL registerPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setSupportActionBarIcon();
        setInputValidators();
    }

    /** -------------------------------
     *            ACTION BAR
     *  ------------------------------- */

    private void setSupportActionBarIcon(){
        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setIcon(R.mipmap.brand_logo_foreground);
            bar.setDisplayShowHomeEnabled(true);
            bar.setTitle("Register");
        }
    }

    /** -------------------------------
     *        INPUT VALIDATORS
     *  ------------------------------- */


    private void setInputValidators(){
        setEmailValidator((EditText)this.findViewById(R.id.registerEmail));
        setUsernameValidator((EditText)this.findViewById(R.id.registerUsername));
        setPasswordValidator((EditText)this.findViewById(R.id.registerPassword));
        setConfirmPasswordValidator((EditText)this.findViewById(R.id.registerConfirmPassword));
        setCreditCardInfoValidator();
    }

    private void setEmailValidator(EditText emailView){
        emailView.addTextChangedListener(new TextValidator(emailView) {

            @Override
            public void validate(TextView view, String text) {
                if(!Patterns.EMAIL_ADDRESS.matcher(text).matches()){
                    view.setError("The e-mail format is not accepted.");
                }
            }
        });
    }

    private void setUsernameValidator(EditText textView){

    }

    private void setPasswordValidator(EditText textView){

    }

    private void setConfirmPasswordValidator(EditText textView){

    }

    private void setCreditCardInfoValidator(){
        setCreditCardNumberValidator((EditText)this.findViewById(R.id.registerCardNr));
        setCreditCardExpDateValidator((EditText)this.findViewById(R.id.registerExpDate));
        setCreditCardCCVvalidator((EditText)this.findViewById(R.id.registerCCV));
    }

    private void setCreditCardNumberValidator(EditText textView){

    }

    private void setCreditCardExpDateValidator(EditText textView){

    }

    private void setCreditCardCCVvalidator(EditText textView){

    }
}
