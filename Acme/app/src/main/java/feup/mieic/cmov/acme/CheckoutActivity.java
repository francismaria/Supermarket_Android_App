package feup.mieic.cmov.acme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.util.Objects;

import feup.mieic.cmov.acme.qrcodes.QRTag;
import feup.mieic.cmov.acme.security.KeyInstance;
import feup.mieic.cmov.acme.security.SharedPrefsHolder;
import feup.mieic.cmov.acme.ui.cart.CartFragment;

public class CheckoutActivity extends AppCompatActivity {

    private JSONObject obj;
    private int MAX_NUM_VOUCHERS;
    private static final int UUID_BYTES = 36;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try{
            if(getIntent().hasExtra("data")) {
                obj = new JSONObject(getIntent().getStringExtra("data"));

                MAX_NUM_VOUCHERS = obj.getInt("vouchers");

                final TextView vouchersNumField = findViewById(R.id.vouchersNumbersText);

                String newVouchersNum = Integer.toString(MAX_NUM_VOUCHERS);
                vouchersNumField.setText(newVouchersNum);

                Button removeBtn = findViewById(R.id.removeVoucherBtn);
                removeBtn.setOnClickListener(new View.OnClickListener(){

                    private int getCurrentSelectedVouchersNumber(){
                        return Integer.parseInt(vouchersNumField.getText().toString());
                    }

                    @Override
                    public void onClick(View v) {
                        int numVouchers = getCurrentSelectedVouchersNumber();
                        if(numVouchers == 0)
                            return;

                        numVouchers--;

                        String newVouchersNumber = Integer.toString(numVouchers);
                        vouchersNumField.setText(newVouchersNumber);

                    }
                });

                Button addBtn = findViewById(R.id.addVoucherBtn);
                addBtn.setOnClickListener(new View.OnClickListener(){

                    private int getCurrentSelectedVouchersNumber(){
                        return Integer.parseInt(vouchersNumField.getText().toString());
                    }

                    @Override
                    public void onClick(View v) {
                        int numVouchers = getCurrentSelectedVouchersNumber();
                        if(numVouchers == MAX_NUM_VOUCHERS)
                            return;

                        numVouchers++;

                        String newVouchersNumber = Integer.toString(numVouchers);
                        vouchersNumField.setText(newVouchersNumber);
                    }
                });

                Button checkoutBtn = findViewById(R.id.generateQRcodeBtn);
                checkoutBtn.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        String currenVouchersNum  = vouchersNumField.getText().toString();
                        try {
                            obj.put("vouchers", currenVouchersNum);
                        } catch (JSONException e) {
                            // TODO : SHOW TOAST??
                            e.printStackTrace();
                            return;
                        }
                        generateQRcode();
                    }
                });

            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }


    private void generateQRcode(){
        try {
            String prodsArrStr = obj.toString();

            final int PRODS_LENGTH = prodsArrStr.length();
            final int MSG_LENGTH = UUID_BYTES + PRODS_LENGTH;
            final int len = MSG_LENGTH + (512 / 8);

            ByteBuffer tag = ByteBuffer.allocate(len);

            String uuid = SharedPrefsHolder.getUUID(Objects.requireNonNull(this));

            tag.put(uuid.getBytes(StandardCharsets.ISO_8859_1));
            tag.put(prodsArrStr.getBytes(StandardCharsets.ISO_8859_1));

            byte[] msg = tag.array();

            // Create Digital Signature

            Signature sg = Signature.getInstance("SHA256WithRSA");
            sg.initSign(KeyInstance.getPrivateKey(SharedPrefsHolder.getUsername(this)));
            sg.update(msg, 0, MSG_LENGTH);
            sg.sign(msg, MSG_LENGTH, 512 / 8);

            startActivity(new Intent(this, QRTag.class).putExtra("data", msg));

        } catch(Exception e){
            e.printStackTrace();
        }

        //
    }
}
