package feup.mieic.cmov.terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.nio.charset.StandardCharsets;

import feup.mieic.cmov.terminal.connection.CheckoutAction;

public class MainActivity extends AppCompatActivity {

    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.readCheckoutBtn);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(ACTION_SCAN);
                    intent.putExtra("SCAN_MODE",  "QR_CODE_MODE");
                    startActivityForResult(intent, 0);
                }
                catch (ActivityNotFoundException anfe) {
                    showDialog(MainActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
                }
            }
        });
    }

    // TODO: upon successful reading, show text showing that checkout is being processed
    public void handleReading(byte[] tag){
        Log.e("handle_reading", "READ");

        String encodedTag = Base64.encodeToString(tag, Base64.DEFAULT);
        //return encodedTag;

        new CheckoutAction().execute(encodedTag);
/*
        *  TODO: show progress bar while waiting for server's response
        *  TODO: show a dialog to let the user know if the transaction was or not successful
        * */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                if (contents != null)
                    handleReading(contents.getBytes(StandardCharsets.ISO_8859_1));
            }
        }
    }


    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                act.startActivity(intent);
            }
        });
        downloadDialog.setNegativeButton(buttonNo, null);
        return downloadDialog.show();
    }
}
