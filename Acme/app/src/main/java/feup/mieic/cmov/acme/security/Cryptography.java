package feup.mieic.cmov.acme.security;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Cryptography {

    //byte[] bs = "ahahahahaha".getBytes(StandardCharsets.ISO_8859_1);

    private static final String ENC_ALG = "RSA/ECB/PKCS1Padding";

    private static byte[] getByteArray(String msg){
        return msg.getBytes(StandardCharsets.ISO_8859_1);
    }

    public static byte[] encrypt(String msg, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] arr = getByteArray(msg);

        if(key instanceof PrivateKey) {
            key = (PrivateKey)key;
        } else {
            key = (PublicKey)key;
        }

        Cipher cipher = Cipher.getInstance(ENC_ALG);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        arr = cipher.doFinal(arr);

        return arr;
    }

    public static byte[] encrypt(byte[] arr, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        if(key instanceof PrivateKey) {
            key = (PrivateKey)key;
        } else {
            key = (PublicKey)key;
        }

        Cipher cipher = Cipher.getInstance(ENC_ALG);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        arr = cipher.doFinal(arr);

        return arr;
    }

    public static String decrypt(byte[] arr, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        if(key instanceof PrivateKey) {
            key = (PrivateKey)key;
        } else {
            key = (PublicKey)key;
        }

        Cipher cipher = Cipher.getInstance(ENC_ALG);
        cipher.init(Cipher.DECRYPT_MODE, key);

        arr = cipher.doFinal(arr);

        return new String(arr);
    }
}
