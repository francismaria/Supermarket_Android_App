package feup.mieic.cmov.acme.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import javax.crypto.Cipher;

import feup.mieic.cmov.acme.qrcodes.QRTag;
import feup.mieic.cmov.acme.security.Cryptography;
import feup.mieic.cmov.acme.security.KeyInstance;
import feup.mieic.cmov.acme.R;
import feup.mieic.cmov.acme.security.SharedPrefsHolder;
import feup.mieic.cmov.acme.ui.cart.CartFragment;
import feup.mieic.cmov.acme.ui.cart.CartViewModel;
import feup.mieic.cmov.acme.ui.order.ProductModel;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button readQRcodeBtn = root.findViewById(R.id.readQRbtn);

        final FragmentTransaction transaction = Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().beginTransaction();
        final CartFragment cartFragment = new CartFragment();

        readQRcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: open QR CODE READER
                try {
                    Intent intent = new Intent(ACTION_SCAN);
                    intent.putExtra("SCAN_MODE",  "QR_CODE_MODE");
                    startActivityForResult(intent, 0);
                }
                catch (ActivityNotFoundException anfe) {
                    showDialog(HomeFragment.this.getActivity(), "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
                }
                //genTag();
            }
        });

        return root;
    }

    private void decodeQRCode(byte[] encTag){
        try{
            PublicKey acmePK = SharedPrefsHolder.getAcmePublicKey(Objects.requireNonNull(this.getActivity()));
            byte[] clearTag = Cryptography.decrypt(encTag, acmePK);

            ByteBuffer tag = ByteBuffer.wrap(clearTag);
            // Tag ID
            int tId = tag.getInt();
            // UUID
            UUID id = new UUID(tag.getLong(), tag.getLong());
            // Price
            int euros = tag.getInt();
            int cents = tag.getInt();
            // Product Name
            byte[] bName = new byte[tag.get()];
            tag.get(bName);
            String name = new String(bName, StandardCharsets.ISO_8859_1);
            String priceStr = euros + "." + cents;

            CartViewModel.addProduct(new ProductModel(id, name, "1", priceStr));

        } catch(Exception e){
            // TODO: show toast with text : "A problem occured during the QR code reading"
            return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                if (contents != null)
                    decodeQRCode(contents.getBytes(StandardCharsets.ISO_8859_1));
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

