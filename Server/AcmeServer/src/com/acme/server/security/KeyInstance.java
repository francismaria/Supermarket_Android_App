package com.acme.server.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.sun.jersey.core.util.Base64;

public class KeyInstance {

	private static final String KEYSTORE_PATH = "///Users/francisco/acme-keystore.p12";
    private static final String CERTIFICATE_PATH = "///Users/francisco/Documents/FEUP/Projetos/CMOV/Server/server_certificate.cer";
    
    public static String getPublicKey() throws FileNotFoundException, CertificateException, UnsupportedEncodingException {
    	FileInputStream fin = new FileInputStream(CERTIFICATE_PATH);
    	
    	CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
        PublicKey pk = certificate.getPublicKey();
        
        byte[] publicKeyBytes = Base64.encode(pk.getEncoded());
        return new String(publicKeyBytes, "UTF-8");
    }
    
    public static PrivateKey getPrivateKey() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
    	FileInputStream is = new FileInputStream(KEYSTORE_PATH);

        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(is, "password".toCharArray());

        String alias = "senderKeyPair";

        return (PrivateKey) keystore.getKey(alias, "password".toCharArray());
    }
} 
