package feup.mieic.cmov.acme.security;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

public class KeyInstance {

    private static boolean isKeySet = false;
    private static final int KEY_SIZE = 512;
    private static final String KEY_ALGO = "RSA";
    private static final int CERT_SERIAL = 12121212;
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    public static String KEYNAME = "acme_";

    public static void setKeyname(String username) {
        KEYNAME = KEYNAME + username;
    }

    // TODO: return public key
    public static void generateKeyPair(Context context, String username) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {

        setKeyname(username);

        KeyStore ks = KeyStore.getInstance(ANDROID_KEYSTORE);
        ks.load(null);

        KeyStore.Entry entry = ks.getEntry(KEYNAME, null);


        Log.e("generateKeyPair", "entrou");
        if (entry != null) {
            // TODO: if it already exists then call (get raw user public key)
            return;
        }

        Log.e("generateKeyPair", "não exxiste");
        try {
            Calendar start = new GregorianCalendar();
            Calendar end = new GregorianCalendar();
            // set the end date to 20 years from now
            end.add(Calendar.YEAR, 20);

            KeyPairGenerator kgen = null;
            kgen = KeyPairGenerator.getInstance(KEY_ALGO, ANDROID_KEYSTORE);

            AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setKeySize(KEY_SIZE)
                    .setAlias(KEYNAME)
                    .setSubject(new X500Principal("CN=" + KEYNAME))
                    .setSerialNumber(BigInteger.valueOf(CERT_SERIAL))
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            kgen.initialize(spec);

            KeyPair kp = kgen.generateKeyPair();

            isKeySet = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * finally {
         *  getRawPublicKey()
         * }
         */
    }

    public static PublicKey getPublicKey() throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance(ANDROID_KEYSTORE);
        PublicKey pub = null;
        ks.load(null);

        KeyStore.Entry entry = ks.getEntry(KEYNAME, null);

        if (entry != null) {
            pub = ((KeyStore.PrivateKeyEntry)entry).getCertificate().getPublicKey();
            return pub;
        }
        //generateKeyPair(sharedPreferences.getUsername())
        return null;
    }

    public static String getRawPublicKey() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableEntryException {
        KeyStore ks = KeyStore.getInstance(ANDROID_KEYSTORE);
        PublicKey pub = null;
        ks.load(null);

        KeyStore.Entry entry = ks.getEntry(KEYNAME, null);

        if (entry != null) {
            pub = ((KeyStore.PrivateKeyEntry)entry).getCertificate().getPublicKey();

            byte[] publicKeyBytes = Base64.encode(pub.getEncoded(), Base64.NO_WRAP);
            return new String(publicKeyBytes);
        }
        //generateKeyPair(sharedPreferences.getUsername())
        return null;
    }


    public static PrivateKey getPrivateKey() throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance(ANDROID_KEYSTORE);
        PublicKey pub = null;
        ks.load(null);

        KeyStore.Entry entry = ks.getEntry(KEYNAME, null);

        if (entry != null) {
            return ((KeyStore.PrivateKeyEntry)entry).getPrivateKey();
        }
        return null;
    }
}
