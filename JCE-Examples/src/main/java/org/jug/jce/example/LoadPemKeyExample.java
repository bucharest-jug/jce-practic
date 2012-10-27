package org.jug.jce.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

/**
 * Loads a rsa key in pem format from the disk.
 * 
 * @author Florin
 */
public class LoadPemKeyExample {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	private KeyPair getPemKey() throws IOException {
		// 1024-bit key, saved to file named mykey.pem
		// openssl genrsa -out mykey.pem 1024
		InputStream is = LoadPemKeyExample.class.getResourceAsStream("/mykey.pem");
		String keyString = "";

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = br.readLine()) != null) {
			keyString += "\n";
			keyString += line;
		}
		PEMReader pemReader = new PEMReader(new StringReader(keyString));
		return (KeyPair) pemReader.readObject();

	}

	public static void main(String[] args) throws IOException {
		LoadPemKeyExample ex = new LoadPemKeyExample();
		KeyPair pair = ex.getPemKey();
		System.out.println(pair);
	}
}
