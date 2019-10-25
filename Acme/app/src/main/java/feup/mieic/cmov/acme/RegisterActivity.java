package feup.mieic.cmov.acme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
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

    private void setCreditCardNumberValidator(EditText creditCardNumView){
        creditCardNumView.addTextChangedListener(new TextValidator(creditCardNumView) {
            private final int TOTAL_SYMBOLS = 19;
            private final int MODULUS = 5;

            @Override
            public void validate(TextView view, String text) {
                view.removeTextChangedListener(this);

                String str = correctInput(text);

                int position = view.getSelectionStart();
                view.setText(str);
                ((EditText) view).setSelection(str.length());

                view.addTextChangedListener(this);

                if(str.length() < TOTAL_SYMBOLS){
                    view.setError("The credit card number must have 16 digits.");
                } else {
                    Drawable myIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.check_icon, null);
                    myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
                    view.setError("The credit card number is correct.", myIcon);
                }
            }

            private String correctInput(String text){
                if(text.length() > TOTAL_SYMBOLS)
                    return text.substring(0, text.length()-1);

                String resStr = "";
                int charCount = 1;

                for(int i = 0; i < text.length(); i++, charCount++){
                    if(i > 0 && charCount % MODULUS == 0) {
                        if(text.charAt(i) != ' '){
                            resStr += " ";
                        }
                        charCount = 0; // will be incremented right before the cycle is over
                    }
                    resStr += Character.toString(text.charAt(i));
                }
                Log.i("INFO", resStr);
                return resStr;
            }
        });
    }

    private void setCreditCardExpDateValidator(EditText creditcardExpDateView){
        /*creditcardExpDateView.addTextChangedListener(new TextValidator(creditcardExpDateView) {
            final Pattern creditCardExpDatePattern = Pattern.compile("^[\\d]{2}/[\\d]{2}$");

            @Override
            public void validate(TextView view, String text) {
                EditText creditcardExpDateViewText = (EditText)view;

                if(!creditCardExpDatePattern.matcher(text).matches()){
                    view.setError("The credit card expiration date must be of the type MM/YY.");
                } else {
                    Drawable myIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.check_icon, null);
                    myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
                    creditcardExpDateViewText.setError("The passwords match.", myIcon);
                }

                view.removeTextChangedListener(this);

                view.addTextChangedListener(this);
            }
        });*/
    }

    private void setCreditCardCCVvalidator(EditText textView){

    }
}
