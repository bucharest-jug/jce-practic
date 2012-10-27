package org.jug.jce.example;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RSAExample {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	private static final String RSA_NONE_PKCS1PADDING = "RSA/None/PKCS1Padding";
	
	public byte[] encrypt(byte[] inputBytes, Key publicKey) {
		try {
			System.out.println("Bytes to enccrypt: " + Utils.toHex(inputBytes));
			Cipher cipher = Cipher.getInstance(RSA_NONE_PKCS1PADDING, "BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encryptedBytes = cipher.doFinal(inputBytes);
			System.out.println("Encrypted bytes: " + Utils.toHex(encryptedBytes));
			return encryptedBytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public byte[] decrypt(byte[] encryptedBytes, Key privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_NONE_PKCS1PADDING, "BC");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] plainBytes = cipher.doFinal(encryptedBytes);
			System.out.println("Plain bytes: " + Utils.toHex(plainBytes));
			return plainBytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void encryptExample() throws Exception {
		
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
		generator.initialize(256);
		KeyPair pair = generator.generateKeyPair();
		
		String secret = "some secret";
		
		byte[] encryptedBytes = encrypt(secret.getBytes("UTF-8"), pair.getPublic());
		byte[] plainBytes = decrypt(encryptedBytes, pair.getPrivate());
		System.out.println(new String(plainBytes, "UTF-8"));
	}
	
	public static void main(String[] args) {
		try {
			new RSAExample().encryptExample();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
