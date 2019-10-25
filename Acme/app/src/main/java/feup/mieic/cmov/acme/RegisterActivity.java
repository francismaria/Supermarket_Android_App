package feup.mieic.cmov.acme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;

import feup.mieic.cmov.acme.validation.Sha256Hashing;
import feup.mieic.cmov.acme.validation.TextValidator;
import java.util.regex.Pattern;

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

    private void setUsernameValidator(EditText usernameView){
        usernameView.addTextChangedListener(new TextValidator(usernameView) {
            // Note:
            final Pattern usernamePattern = Pattern.compile("^(?!.*__.*)(\\w){6,30}$");
            @Override
            public void validate(TextView view, String text) {
                if(!usernamePattern.matcher(text).matches()){
                    view.setError("The username must have at least 6 and at most 30 characters, containing only lowercase, uppercase, digits or non-contiguous underscores.");
                }
            }
        });
    }

    private boolean passwdValid = true;
    private String encryptedPasswdSHA256 = "";

    private void setPasswordValidator(EditText passwdView){

        passwdView.addTextChangedListener(new TextValidator(passwdView) {
            final Pattern passwdPattern = Pattern.compile("^(?=.*[a-z].*)(?=.*[A-Z].*)(?=.*\\d.*)(?=.*[@#€$£%&÷=_!?].*)[a-zA-Z\\d@#€$£%&÷=_!?]{6,30}$");
            @Override
            public void validate(TextView view, String text) {
                if(!passwdPattern.matcher(text).matches()){
                    passwdValid = false;
                    view.setError("The password must have at least 6 and at most 30 characters, containing at least one lowercase, one uppercase, one special character and one digit");
                } else {
                    passwdValid = true;
                }
            }
        });

        passwdView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && passwdValid){
                    EditText passwdTextView = (EditText)v;
                    try{
                        Sha256Hashing encryptInstance = new Sha256Hashing(passwdTextView.getText().toString());
                        encryptedPasswdSHA256 = encryptInstance.getResult();

                        Log.i("PASSWORD:", encryptedPasswdSHA256);


                    } catch (Exception e){
                        Log.e("Password Encryption: ", "Algorithm does not exist.");
                    }
                }
            }
        });
    }

    private void setConfirmPasswordValidator(EditText confirmPasswdView){

        confirmPasswdView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText confirmPasswdTextView = (EditText)v;
                    try{
                        Sha256Hashing encryptInstance = new Sha256Hashing(confirmPasswdTextView.getText().toString());
                        String confirmEncryption = encryptInstance.getResult();

                        Log.i("CONFIRM PASSWORD:", encryptedPasswdSHA256);

                        if(!confirmEncryption.equals(encryptedPasswdSHA256)){
                            confirmPasswdTextView.setError("The passwords do not match.");
                        } else {
                            Drawable myIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.check_icon, null);
                            myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
                            confirmPasswdTextView.setError("The passwords match.", myIcon);
                        }

                    } catch (Exception e){
                        Log.e("Cfm Password Encryption", "Algorithm does not exist.");
                    }
                }
            }
        });
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
