package feup.mieic.cmov.acme;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

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
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

public class KeyInstance {

    private static final int KEY_SIZE = 512;
    private static final String KEY_ALGO = "RSA";
    private static final int CERT_SERIAL = 12121212;
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    public static String KEYNAME = "acme_";

    public static boolean generateKeyPair(Context context, String username) {

        KEYNAME = KEYNAME + username;

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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getPubKey() throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance(ANDROID_KEYSTORE);
        PublicKey pub = null;

        ks.load(null);

        KeyStore.Entry entry = ks.getEntry(KEYNAME, null);

        if (entry != null) {
            pub = ((KeyStore.PrivateKeyEntry)entry).getCertificate().getPublicKey();
        }

        byte[] publicKeyBytes = Base64.encode(pub.getEncoded(),0);
        return new String(publicKeyBytes);
    }
}
