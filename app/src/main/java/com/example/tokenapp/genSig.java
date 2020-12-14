package com.example.tokenapp;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;

public class genSig {

    public static void main(String[] args) {
        if (args.length != 1) {
            Log.d("tag", "keys");

        } else try {

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);

            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey priv = pair.getPrivate();
            PublicKey pub = pair.getPublic();

            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(priv);

            FileInputStream fis = new FileInputStream(args[0]);
            BufferedInputStream bufin = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufin.read(buffer)) >= 0) {
                dsa.update(buffer, 0, len);
            }
            bufin.close();
            byte[] realSig = dsa.sign();

            FileOutputStream sigfos = new FileOutputStream("sig.txt");
            sigfos.write(realSig);
            sigfos.close();

            byte[] key = pub.getEncoded();
            FileOutputStream keyfos = new FileOutputStream("suepk.txt");
            keyfos.write(key);
            keyfos.close();

        } catch (Exception e) {
            Log.d("tag", "Caught exception " + e.toString());
        }
    }
}