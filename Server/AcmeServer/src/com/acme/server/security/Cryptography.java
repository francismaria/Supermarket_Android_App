package com.acme.server.security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Cryptography {
	
	private static final String ENC_ALG = "RSA/ECB/PKCS1Padding";

	public static String encrypt(byte[] arr, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		if(key instanceof PrivateKey) {
			key = (PrivateKey)key;
		} else {
			key = (PublicKey)key;
		}
		
		Cipher cipher = Cipher.getInstance(ENC_ALG); 
	    cipher.init(Cipher.ENCRYPT_MODE, key);  
	    
	    arr = cipher.doFinal(arr);
	    
	    return new String(arr);
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
