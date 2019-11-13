package feup.mieic.cmov.acme.ui.home;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Objects;

import javax.crypto.Cipher;

import feup.mieic.cmov.acme.KeyInstance;
import feup.mieic.cmov.acme.R;
import feup.mieic.cmov.acme.ui.cart.CartFragment;
import feup.mieic.cmov.acme.ui.cart.CartViewModel;
import feup.mieic.cmov.acme.ui.order.ProductModel;

public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button readQRcodeBtn = root.findViewById(R.id.readQRbtn);

        final FragmentTransaction transaction = Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().beginTransaction();
        final CartFragment cartFragment = new CartFragment();

        readQRcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartViewModel.addProduct(new ProductModel("1", "exa", "1", "10"));
            }
        });

        try{
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(KeyInstance.KEYNAME, null);


            PrivateKey privateKey = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
            PublicKey publicKey = ks.getCertificate("example").getPublicKey();

        /*
            byte[] publicBytes = Base64.decode(publicKey.toString(), Base64.DEFAULT);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(); //or try with "RSA"
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            encrypted = cipher.doFinal(txt.getBytes());
            encoded = Base64.encodeToString(encrypted, Base64.DEFAULT);

         */
            final String ENC_ALGO = "RSA/NONE/PKCS1Padding";

            Cipher cipher = Cipher.getInstance(ENC_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encTag = cipher.doFinal("ahahah".getBytes());

            Log.e("Encrypted", Arrays.toString(encTag));

            byte[] publicKeyBytes = Base64.encode(publicKey.getEncoded(),0);
            String pubKey = new String(publicKeyBytes);
            Log.e("KEYS", pubKey);

            /*

             // -- PRINT CERTIFICATE --

            X509Certificate cert = (X509Certificate)((KeyStore.PrivateKeyEntry)entry).getCertificate();
            byte[] encCert = cert.getEncoded();
            String strCert = cert.toString();
            String b64Cert = Base64.encodeToString(encCert, Base64.DEFAULT);
            String text = "cert(b64): " + b64Cert + "\n\n" + strCert;

            text = "-----BEGIN CERTIFICATE-----\n" + b64Cert +
                    "-----END CERTIFICATE-----\n";


            Log.e("KEYS", "jajaj" + text);

            */
            Log.e("KEYS", privateKey.toString());
        } catch(Exception e){
            e.printStackTrace();
        }

        return root;
    }
}