package org.jug.jce.example;

import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AESWithIVExample {
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static final String AES_ECB_PKCS7PADDING = "AES/CBC/PKCS7Padding";

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

	public byte[] encrypt(byte[] inputBytes, Key key, byte[] ivBytes) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(AES_ECB_PKCS7PADDING, "BC");
			System.out.println("Bytes to encrypt: " + Utils.toHex(inputBytes));
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
			//IvParameterSpec ivSpec = new IvParameterSpec(cipher.getIV());
			
			byte[] encryptedBytes = new byte[cipher.getOutputSize(ivBytes.length + inputBytes.length)];
			int lenght = cipher.update(ivBytes, 0, ivBytes.length, encryptedBytes, 0);
			lenght = cipher.update(inputBytes, 0, inputBytes.length, encryptedBytes, lenght);
			lenght += cipher.doFinal(encryptedBytes, lenght);

			System.out.println("Encrypted bytes: " + Utils.toHex(encryptedBytes));
			return encryptedBytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] decrypt(byte[] encryptedBytes, Key key, byte[] ivBytes) {
		try {
			Cipher cipher = Cipher.getInstance(AES_ECB_PKCS7PADDING, "BC");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
			// estimated output size may be greater than the actual size
			// (because of padding)
			// actual size will be indicated by lenght
			byte[] buffer = new byte[cipher.getOutputSize(encryptedBytes.length)];
			int lenght = cipher.update(encryptedBytes, 0, encryptedBytes.length, buffer);
			lenght += cipher.doFinal(buffer, lenght);
			
			byte[] plainBytes = new byte[buffer.length - ivBytes.length];
			System.arraycopy(buffer, ivBytes.length, plainBytes, 0, plainBytes.length);
			
			
			System.out.println("Decrypted bytes: " + Utils.toHex(plainBytes));
			return plainBytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void encryptExample() throws Exception {
		SecureRandom random = new SecureRandom();
		byte[] ivBytes = new byte[8];
		random.nextBytes(ivBytes);
		
		String secret = "some secret that needs to encrypted";
		System.out.println("Plain text: " + secret);
		Key key = generateKeyForAES();
		byte[] encryptedBytes = encrypt(secret.getBytes("UTF-8"), key, ivBytes);
		byte[] plainBytes = decrypt(encryptedBytes, key, ivBytes);
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
