package us.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	private static SecretKeySpec key;

	private static String decryptedString;
	private static String encryptedString;

	private static String padding = "AES/ECB/PKCS5Padding";

	public static void setKey(String myKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest sha = null;
		byte[] keyBytes = myKey.getBytes("UTF-8");
		sha = MessageDigest.getInstance("SHA-1");
		keyBytes = sha.digest(keyBytes);
		keyBytes = Arrays.copyOf(keyBytes, 16);
		key = new SecretKeySpec(keyBytes, "AES");

	}

	public static String encrypt(String str) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

		Cipher c = Cipher.getInstance(padding);
		c.init(Cipher.ENCRYPT_MODE, key);
		encryptedString = Base64.getEncoder().encodeToString((c.doFinal(str.getBytes("UTF-8"))));

		return null;
	}

	public static String decrypt(String str) throws NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

		Cipher c = Cipher.getInstance(padding);
		c.init(Cipher.DECRYPT_MODE, key);
		decryptedString = new String(c.doFinal(Base64.getMimeDecoder().decode(str)));

		return null;
	}

	public static String getDecryptedString() {
		return decryptedString;
	}

	public static String getEncryptedString() {
		return encryptedString;
	}

}