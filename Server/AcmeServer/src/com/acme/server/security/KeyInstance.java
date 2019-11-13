package com.acme.server.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.sun.jersey.core.util.Base64;

public class KeyInstance {

    
    public static String getPublicKey() throws FileNotFoundException, CertificateException {
    	FileInputStream fin = new FileInputStream("///Users/francisco/Documents/FEUP/Projetos/CMOV/Server/server_certificate.cer");
    	
    	CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
        PublicKey pk = certificate.getPublicKey();
        
        
        byte[] publicKeyBytes = Base64.encode(pk.getEncoded());
        return new String(publicKeyBytes);

    }
} 
