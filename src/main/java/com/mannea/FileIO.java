package com.mannea;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mannea on 1/22/15.
 */
public class FileIO {

// /Users/mannea/2014-05-14_10-52-38-939.log

    LocalEncryption localEncryption = new LocalEncryption();


    public void encryptFile(String path) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fileInputStream;

        File file = new File(path);
        fileInputStream = new FileInputStream(file);
        int i = 0;
        while ((i = fileInputStream.read()) != -1) {
            byteArrayOutputStream.write((byte) i);
        }

        try {
            byteArrayOutputStream = localEncryption.encrypt(byteArrayOutputStream);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(path + ".crypter");
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.flush();

        fileInputStream.close();
        byteArrayOutputStream.close();
        fileOutputStream.close();
    }


    public void decryptFile(String path, String keyString) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fileInputStream;

        File file = new File(path);
        fileInputStream = new FileInputStream(file);
        int i = 0;
        while ((i = fileInputStream.read()) != -1) {
            byteArrayOutputStream.write((byte) i);
        }

        try {
            byteArrayOutputStream = localEncryption.decrypt(byteArrayOutputStream, keyString);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        path = path.substring(0, path.length() - 8); //remove the .encrypter extension

        FileOutputStream fileOutputStream = new FileOutputStream(path);
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.flush();

        fileInputStream.close();
        byteArrayOutputStream.close();
        fileOutputStream.close();

    }
}


