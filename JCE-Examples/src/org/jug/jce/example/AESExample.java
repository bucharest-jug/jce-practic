package org.jug.jce.example;

import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

/**
 * 
 * @author Florin
 */
public class AESExample {

	private static final String AES_ECB_PKCS7PADDING = "AES/ECB/PKCS7Padding";
	//private static final String AES_ECB_NOPADDING = "AES/ECB/NoPadding";

	private Key generateKeyForAES() {
		KeyGenerator generator;
		try {
			generator = KeyGenerator.getInstance("AES", "BC");
			generator.init(192);
			return generator.generateKey();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] encrypt(byte[] inputBytes, Key key) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(AES_ECB_PKCS7PADDING, "BC");
			System.out.println("Bytes to encrypt: " + Utils.toHex(inputBytes));
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptedBytes = new byte[cipher.getOutputSize(inputBytes.length)];			
			int lenght = cipher.update(inputBytes, 0, inputBytes.length, encryptedBytes);
			lenght += cipher.doFinal(encryptedBytes, lenght);

			System.out.println("Encrypted bytes: " + Utils.toHex(encryptedBytes));
			return encryptedBytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] decrypt(byte[] encryptedBytes, Key key) {
		try {
			Cipher cipher = Cipher.getInstance(AES_ECB_PKCS7PADDING, "BC");
			cipher.init(Cipher.DECRYPT_MODE, key);
			// estimated output size may be greater than the actual size (because of padding)
			// actual size will be indicated by lenght
			byte[] plainBytes = new byte[cipher.getOutputSize(encryptedBytes.length)];					
			int lenght = cipher.update(encryptedBytes, 0, encryptedBytes.length, plainBytes);
			lenght += cipher.doFinal(plainBytes, lenght);
			System.out.println("Decrypted bytes: " + Utils.toHex(plainBytes));
			return Arrays.copyOf(plainBytes, lenght);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void encryptExample() throws Exception {
		String secret = "some secret that needs to encrypted";
		System.out.println("Plain text: " + secret);
		Key key = generateKeyForAES();
		byte[] encryptedBytes = encrypt(secret.getBytes("UTF-8"), key);
		byte[] plainBytes = decrypt(encryptedBytes, key);
		System.out.println(new String(plainBytes, "UTF-8"));

	}
	
	public static void main(String[] args) {
		try {
			new AESExample().encryptExample();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
