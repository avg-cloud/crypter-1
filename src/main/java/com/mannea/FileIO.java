package com.mannea;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

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

    Logger logger = Logger.getLogger(FileIO.class);
    LocalEncryption localEncryption = new LocalEncryption();

    public void encryptFile(File file) {

        try {
            InputStream originalFileInputStream = new FileInputStream(file);
            byte[] keyByte = localEncryption.generateKeyValue();
            FileOutputStream crypterKey = new FileOutputStream(new File(file.getAbsolutePath() + ".crypter.key"));
            IOUtils.write(keyByte, crypterKey);
            logger.info("Successfully generated " + file.getAbsolutePath() + ".crypter.key");

            FileOutputStream encryptedFile = new FileOutputStream(new File(file.getAbsolutePath() + ".crypter"));
            IOUtils.write(localEncryption.encrypt(originalFileInputStream, keyByte), encryptedFile);
            logger.info("Successfully generated " + file.getAbsolutePath() + ".crypter");
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void decryptFile(File file) throws IOException {
        if (file.getAbsolutePath().endsWith(".crypter")) {
            File keyFile = new File(file.getAbsolutePath() + ".key");
            if (keyFile.exists() && !keyFile.isDirectory()) {
                try {
                    InputStream keyInputStream = new FileInputStream(keyFile);
                    ByteArrayOutputStream keyBAOS = new ByteArrayOutputStream();
                    int i = 0;
                    while ((i = keyInputStream.read()) != -1) {
                        keyBAOS.write((byte) i);
                    }
                    byte[] keyBytes = keyBAOS.toByteArray();

                    String decryptedFileName = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 8);
                    InputStream encryptedInputStream = new FileInputStream(file);
                    FileOutputStream decryptedFile = new FileOutputStream(new File(decryptedFileName));
                    IOUtils.write(localEncryption.decrypt(encryptedInputStream, keyBytes), decryptedFile);
                    logger.info("Successfully created unencrypted file " + file.getAbsolutePath());
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
            } else {
                logger.error("Could not find " + file.getAbsolutePath() + ".key");
            }

        } else {
            logger.error("File is not a .crypter :: " + file.getAbsolutePath());
            //Not .crypter
        }

    }

}


