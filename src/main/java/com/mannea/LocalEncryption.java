package com.mannea;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.*;
import java.util.Map;
import java.util.Random;

/**
 * Created by mannea on 1/22/15.
 * <p/>
 * For information on "unlimited strength" Java Security See;
 * http://stackoverflow.com/questions/1179672/how-to-avoid-installing-unlimited-strength-jce-policy-files-when-deploying-an
 */


public class LocalEncryption {

    private static final String algorithm = "AES";
    static Logger logger = Logger.getLogger(LocalEncryption.class);

    private static void removeCryptographyRestrictions() {
        if (!isRestrictedCryptography()) {
            logger.debug("Cryptography restrictions removal not needed");
            return;
        }
        try {
        /*
         * Do the following, but with reflection to bypass access checks:
         *
         * JceSecurity.isRestricted = false;
         * JceSecurity.defaultPolicy.perms.clear();
         * JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
         */
            final Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
            final Class<?> cryptoPermissions = Class.forName("javax.crypto.CryptoPermissions");
            final Class<?> cryptoAllPermission = Class.forName("javax.crypto.CryptoAllPermission");

            final Field isRestrictedField = jceSecurity.getDeclaredField("isRestricted");
            isRestrictedField.setAccessible(true);
            isRestrictedField.set(null, false);

            final Field defaultPolicyField = jceSecurity.getDeclaredField("defaultPolicy");
            defaultPolicyField.setAccessible(true);
            final PermissionCollection defaultPolicy = (PermissionCollection) defaultPolicyField.get(null);

            final Field perms = cryptoPermissions.getDeclaredField("perms");
            perms.setAccessible(true);
            ((Map<?, ?>) perms.get(defaultPolicy)).clear();

            final Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
            instance.setAccessible(true);
            defaultPolicy.add((Permission) instance.get(null));

            logger.debug("Successfully removed cryptography restrictions (Enhanced security enabled)");
        } catch (final Exception e) {
            logger.warn("Failed to remove cryptography restrictions", e);
        }
    }

    private static boolean isRestrictedCryptography() {
        // This simply matches the Oracle JRE, but not OpenJDK.
        return "Java(TM) SE Runtime Environment".equals(System.getProperty("java.runtime.name"));
    }

    public byte[] encrypt(InputStream inputStream, byte[] keyBytes) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException {
        removeCryptographyRestrictions();
        ByteArrayOutputStream data = new ByteArrayOutputStream();

        int i = 0;
        while ((i = inputStream.read()) != -1) {
            data.write((byte) i);
        }

        Key key = generateKey(keyBytes);
        Cipher c = Cipher.getInstance(algorithm);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.toByteArray());
        byte[] encryptedValue = new Base64().encode(encVal);

        return encryptedValue;

    }

    public byte[] decrypt(InputStream inputStream, byte[] keyBytes) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        removeCryptographyRestrictions();
        ByteArrayOutputStream encryptedValue = new ByteArrayOutputStream();

        int i = 0;
        while ((i = inputStream.read()) != -1) {
            encryptedValue.write((byte) i);
        }

        Key key = generateKey(keyBytes);
        Cipher c = Cipher.getInstance(algorithm);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = new Base64().decode(encryptedValue.toByteArray());
        byte[] decryptedVal = c.doFinal(decodedValue);

        return decryptedVal;

    }

    public byte[] generateKeyValue() throws IOException {
        byte[] b = new byte[32]; // 32*8=512 bit
        new Random().nextBytes(b);

        logger.info("AES Key size : " + b.length * 8);
        logger.info("Generating .crypter.key file");

        return b;
    }

    private Key generateKey(byte[] keyByte) {
        Key key = null;
        key = new SecretKeySpec(keyByte, algorithm);
        return key;
    }

}