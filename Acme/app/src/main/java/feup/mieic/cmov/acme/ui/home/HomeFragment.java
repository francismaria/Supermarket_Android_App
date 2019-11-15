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
                 CartViewModel.addProduct(new ProductModel("1", "exa", "1", "10"));
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

    void genTag() {
        /*ByteBuffer tag;
        String name = "Nivea Sunkissed Body Lotion";

        int tagId = 0x41636D65; // hxadecimal
        UUID productUUID = UUID.randomUUID();

        int len = 4 + 16 + 4 +     4 + 1 + name.length();
        int l = name.length();
        if (l > 30)
            name = name.substring(0, 35);

        tag = ByteBuffer.allocate(len);

        tag.putInt(tagId);

        tag.putLong(productUUID.getMostSignificantBits());
        tag.putLong(productUUID.getLeastSignificantBits());

        tag.putInt(Integer.parseInt("17"));
        tag.putInt(Integer.parseInt("99"));

        tag.put((byte)name.length());

        tag.put(name.getBytes(StandardCharsets.ISO_8859_1));

        Log.e("arr", Arrays.toString(tag.array()));
        byte[] encTag = {82, 56, 3, 83, 29, 118, 67, 99, -113, 56, 86, 3, -13, 73, -101, -7, 21, 111, 69, 97, -61, -29, 104, 92, -81, -78, -96, 107, -97, 31, 56, 4, -26, -75, 10, -89, -92, 10, -110, 105, -68, 13, 85, -61, 56, 116, 70, 86, -82, 86, -108, -1, -104, -46, -103, -30, 38, 109, 28, 60, 46, -80, 124, 21, -82, -38, -52, 27, -4, -5, -96, 79, 115, -73, -125, 13, 34, 38, -69, 29, 111, -67, -122, -37, -73, -89, -38, -50, -48, 8, -21, 58, 55, 7, 123, 45, -123, -110, -112, 28, -46, 35, 7, 83, 74, -102, -19, -42, -83, 91, 11, 15, 60, 123, 54, 111, 87, -73, -59, 40, -86, -33, 83, -43, 21, -26, -6, -106, 58, -81, 22, -75, 104, -50, -84, -78, -76, 30, 93, -6, -110, -72, 30, -32, -34, 51, 106, 120, 53, 69, -120, 6, -78, -67, 65, -86, 24, 38, -46, -43, -63, 60, 7, -28, 34, -51, -58, -26, 49, -111, -29, -80, 98, 33, 42, 70, -2, 97, -96, 40, 97, -119, -100, 65, -110, -11, 80, -101, -92, 39, 123, 76, -46, -44, 116, 12, 76, -8, -1, -115, -107, -39, -72, 104, 16, 9, 70, 94, 43, -118, -112, -38, -21, 53, 55, -92, 97, -33, -90, -70, 63, 114, 104, -41, 29, 87, 25, -54, 45, 114, -28, 107, 124, -10, -127, 8, 52, -61, -25, 107, 99, 14, 59, -99, 29, 114, 89, 14, 29, 94, 116, 38, 75, 121, -47, 20};



        Intent qrAct = new Intent(this.getActivity(), QRTag.class);
        qrAct.putExtra("data", encTag);
        startActivity(qrAct);*/
    }

    // TODO : DECODE QRCODE
    private void decodeQRCode(byte[] encTag){
        try{
            PublicKey acmePK = SharedPrefsHolder.getAcmePublicKey(Objects.requireNonNull(this.getActivity()));
            byte[] clearTag = Cryptography.decrypt(encTag, acmePK);

            ByteBuffer tag = ByteBuffer.wrap(clearTag);
            int tId = tag.getInt();
            UUID id = new UUID(tag.getLong(), tag.getLong());
            int euros = tag.getInt();
            int cents = tag.getInt();
            byte[] bName = new byte[tag.get()];
            tag.get(bName);
            String name = new String(bName, StandardCharsets.ISO_8859_1);

            Log.e("OKOK", name + " " + Integer.toString(euros));

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

