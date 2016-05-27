package org.crypto.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.IvParameterSpec;

class Main {
    public static String transformation = "AES/CBC/PKCS5Padding";
    public static String algo = "AES";
    public static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String []args) {

        Config config = CloValidator.validate(args);
        if (config == null) {
            log.info("Input error, exiting...");
            return;
        }

        encrypt(config);
    }

    private static void encrypt(Config config) {
        try {
            process(config);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private static void process(Config config) throws Exception {
        SecretKeySpec secret = new SecretKeySpec(config.getSecretKey().getBytes(), algo);
        Cipher cipher = Cipher.getInstance(transformation);

        if (config.getMode() == Config.OperationMode.ENCRYPT) {
            cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(new byte[16]));
        } else if (config.getMode() == Config.OperationMode.DECRYPT) {
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(new byte[16]));
        } else {
            throw new RuntimeException("Invalid operation mode given");
        }

        FileInputStream inFile = new FileInputStream(config.getInputFileName());
        FileOutputStream outFile = new FileOutputStream(config.getOutputFileName());

        //50 Mb
        byte[] input = new byte[50000 * 1000];
        int bytesRead = inFile.read(input);

        while (bytesRead != -1) {
            byte[] encrypted = cipher.update(input, 0, bytesRead);

            if (encrypted != null && encrypted.length > 0) {
                outFile.write(encrypted);
            }

            bytesRead = inFile.read(input);
        }

        byte[] encrypted = cipher.doFinal();
        if (encrypted != null && encrypted.length > 0) {
            outFile.write(encrypted);
        }

        inFile.close();
        outFile.flush();
        outFile.close();
    }
}
