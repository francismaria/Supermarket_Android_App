package feup.mieic.cmov.acme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import feup.mieic.cmov.acme.connection.RegisterAction;
import feup.mieic.cmov.acme.security.KeyInstance;
import feup.mieic.cmov.acme.validation.Sha256Hashing;
import feup.mieic.cmov.acme.validation.TextValidator;

import java.util.regex.Pattern;

// TODO: REMOVE ALL THE LOGS

public class RegisterActivity extends AppCompatActivity {

    private Toast filledFieldsToast;

    private boolean NAME_FILLED = false;
    private boolean EMAIL_FLLED = false;
    private boolean USERNAME_FILLED = false;
    private boolean PASSWORD_FILLED = false;
    private boolean CFRM_PASSWORD_FILLED = false;
    private boolean CC_NR_FILLED = false;
    private boolean CC_EXP_DATE_FILLED = false;
    private boolean CC_CCV_FILLED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setSupportActionBarIcon();
        setInputValidators();

        filledFieldsToast =  Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
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
        setNameValidator((EditText)this.findViewById(R.id.registerName));
        setEmailValidator((EditText)this.findViewById(R.id.registerEmail));
        setUsernameValidator((EditText)this.findViewById(R.id.registerUsername));
        setPasswordValidator((EditText)this.findViewById(R.id.registerPassword));
        setConfirmPasswordValidator((EditText)this.findViewById(R.id.registerConfirmPassword));
        setCreditCardInfoValidator();
    }

    /**
     * Validates name by checking if there are at maximum 400 characters (prevents buffer overflows)
     * or if there are any special characters.
     * @param nameView
     */
    private void setNameValidator(EditText nameView){
        final Pattern namePattern = Pattern.compile("^(?!.*[\\d?!*=#€£%&-+|/@$].*)([a-zA-Z]{1,40}(\\s)?){1,10}$");


        nameView.addTextChangedListener(new TextValidator(nameView) {
            @Override
            public void validate(TextView view, String text) {
                if(!namePattern.matcher(text).matches()){
                    view.setError("You can input at most 400 characters. The name must not have digits or special characters of any kind. Enter just one space per name.");

                    NAME_FILLED = false;
                } else {
                    NAME_FILLED = true;
                }

            }
        });
    }

    /**
     * Validates e-mail.
     * @param emailView
     */
    private void setEmailValidator(EditText emailView){
        emailView.addTextChangedListener(new TextValidator(emailView) {
            @Override
            public void validate(TextView view, String text) {
                if(!Patterns.EMAIL_ADDRESS.matcher(text).matches()){
                    view.setError("The e-mail format is not accepted.");

                    EMAIL_FLLED = false;
                } else {
                    EMAIL_FLLED = true;
                }
            }
        });
    }

    /**
     * Validates username by checking if there any contiguous two or more whitespaces.
     * @param usernameView
     */
    private void setUsernameValidator(EditText usernameView){
        usernameView.addTextChangedListener(new TextValidator(usernameView) {
            final Pattern usernamePattern = Pattern.compile("^(?!.*__.*)(\\w){6,30}$");

            @Override
            public void validate(TextView view, String text) {
                if(!usernamePattern.matcher(text).matches()){
                    view.setError("The username must have at least 6 and at most 30 characters, containing only lowercase, uppercase, digits or non-contiguous underscores.");

                    USERNAME_FILLED = false;
                } else {
                    USERNAME_FILLED = true;
                }
            }
        });
    }


    /**
     * Validates password by checking if the text contains:
     *
     *      - at least 6 characters and at most 30 characters
     *      - at least one uppercase letter
     *      - at least one lowercase letter
     *      - at least one digit
     *      - at least one special character
     *
     * If the password matches this requirement, then it is used SHA-256 to encrypt it.
     */

    private boolean passwdValid = true;
    private String encryptedPasswdSHA256 = "";

    private void setPasswordValidator(EditText passwdView){
        passwdView.addTextChangedListener(new TextValidator(passwdView) {
            final Pattern passwdPattern = Pattern.compile("^(?=.*[a-z].*)(?=.*[A-Z].*)(?=.*\\d.*)(?=.*[@#€$£%&÷=_!?].*)[a-zA-Z\\d@#€$£%&÷=_!?]{6,30}$");

            @Override
            public void validate(TextView view, String text) {
                if(!passwdPattern.matcher(text).matches()){
                    passwdValid = false;

                    PASSWORD_FILLED = false;
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

                        PASSWORD_FILLED = true;

                    } catch (Exception e){
                        Log.e("Password Encryption: ", "Algorithm does not exist.");
                    }
                }
            }
        });
    }

    /**
     * Checks if the value of the password (which is now encrypted over the SHA-256 hashing
     * function) is the same of the SHA-256 hashed confirmed password.
     * @param confirmPasswdView
     */
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

                            CFRM_PASSWORD_FILLED = false;
                        } else {
                            Drawable myIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.check_icon, null);
                            myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
                            confirmPasswdTextView.setError("The passwords match.", myIcon);

                            CFRM_PASSWORD_FILLED = true;
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
                view.setText(str);
                ((EditText) view).setSelection(str.length());

                view.addTextChangedListener(this);

                if(str.length() < TOTAL_SYMBOLS){
                    view.setError("The credit card number must have 16 digits.");
                    CC_NR_FILLED = false;
                } else {
                    Drawable myIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.check_icon, null);
                    myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
                    view.setError("The credit card number is correct.", myIcon);
                    CC_NR_FILLED = true;
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
        creditcardExpDateView.addTextChangedListener(new TextValidator(creditcardExpDateView) {
            private final int TOTAL_SYMBOLS = 5;
            private final int HALF_SYMBOLS = 2;

            @Override
            public void validate(TextView view, String text) {
                view.removeTextChangedListener(this);

                String str = correctInput(text);
                view.setText(str);
                ((EditText) view).setSelection(str.length());   // moves cursor to the end of the newly added string

                // TODO: REFACTOR THIS -> VALIDATE DATE

                if(str.length() == TOTAL_SYMBOLS){
                    CC_EXP_DATE_FILLED = true;
                } else {
                    CC_EXP_DATE_FILLED = false;
                }

                view.addTextChangedListener(this);
            }

            private String correctInput(String text){
                if(text.length() > TOTAL_SYMBOLS)
                    return text.substring(0, text.length()-1);

                if(text.length() == HALF_SYMBOLS){
                    text += "/";
                }
                return text;
            }
        });
    }

    private void setCreditCardCCVvalidator(EditText creditCardCCVView){
        creditCardCCVView.addTextChangedListener(new TextValidator(creditCardCCVView) {

            private final int TOTAL_SYMBOLS = 3;

            @Override
            public void validate(TextView view, String text) {
                view.removeTextChangedListener(this);

                String str = correctInput(text);
                view.setText(str);
                ((EditText) view).setSelection(str.length());

                if(str.length() < TOTAL_SYMBOLS){
                    view.setError("The CCV must have exactly 3 digits.");
                    CC_CCV_FILLED = false;
                } else {
                    Drawable myIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.check_icon, null);
                    myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
                    view.setError("The credit card number is correct.", myIcon);
                    CC_CCV_FILLED = true;
                }

                view.addTextChangedListener(this);
            }

            private String correctInput(String text){
                if(text.length() > TOTAL_SYMBOLS)
                    return text.substring(0, text.length()-1);
                return text;
            }
        });
    }

    /** -------------------------------
     *     REGISTER BUTTON LISTENER
     *  ------------------------------- */

    private String getPublicKeyUser(String username) throws Exception {
        KeyInstance.generateKeyPair(this, username);
        String pub = KeyInstance.getPubKey();
        return pub;
    }

    private void initSharedPreferences(){
        SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("current_user", KeyInstance.KEYNAME);


        // putString("current_uuid", )
        // putString("acmePK", )
        editor.commit();
    }

    public void submitRegisterInformation(View view){
        if(!areFieldsFilled()){
            filledFieldsToast.setText("Please fill all of the registration fields correctly.");
            filledFieldsToast.show();
        } else {
            JSONObject reqBody = new JSONObject();

            String name = ((EditText)findViewById(R.id.registerName)).getText().toString(),
                    username = ((EditText)findViewById(R.id.registerUsername)).getText().toString(),
                    password = encryptedPasswdSHA256,
                    email = ((EditText)findViewById(R.id.registerEmail)).getText().toString(),
                    cardNr = ((EditText)findViewById(R.id.registerCardNr)).getText().toString(),
                    cardExpDate = ((EditText)findViewById(R.id.registerExpDate)).getText().toString(),
                    cardCCV = ((EditText)findViewById(R.id.registerCCV)).getText().toString();

            //TODO: check if username is unique (async task)

            try {
                reqBody.put("name", name);
                reqBody.put("username", username);
                reqBody.put("password", password);
                reqBody.put("email", email);
                reqBody.put("cardNr", cardNr);
                reqBody.put("cardExpDate", cardExpDate);
                reqBody.put("cardCCV", cardCCV);

                //    initSharedPreferences();

                reqBody.put("publicKey", getPublicKeyUser(username));

                initSharedPreferences();

                Log.e("pub key", reqBody.getString("publicKey"));


            } catch (JSONException e) {
                // TODO : show toast error
                e.printStackTrace();
                return;
            } catch(Exception e){
                // TODO : show toast error
                e.printStackTrace();
                return;
            }


            new RegisterAction(RegisterActivity.this).execute(reqBody);
        }
    }

    private boolean areFieldsFilled(){
        return NAME_FILLED && EMAIL_FLLED && USERNAME_FILLED
                && PASSWORD_FILLED && CFRM_PASSWORD_FILLED
                && CC_NR_FILLED && CC_EXP_DATE_FILLED && CC_CCV_FILLED;
    }
}
