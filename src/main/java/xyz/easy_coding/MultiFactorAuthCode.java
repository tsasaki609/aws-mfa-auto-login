/**
 *
 */
package xyz.easy_coding;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import net.eckenfels.etotp.Base32;
import net.eckenfels.etotp.RFC4226;

/**
 * et-OTPの内部クラスを呼び出してMFAのコードを取得する
 *
 * @author Takashi Sasaki
 *
 */
public class MultiFactorAuthCode {
	private static File etOtpConfig = new File(".et-otp.properties");

	/**
	 * @param args [0] -> et-OTPに設定したパスワード
	 */
	public static void main(String[] args) throws Exception {
		final char[] etOtpPassword = args[0].toCharArray();
		byte[] secret = readSecret(etOtpPassword);

		long seconds = System.currentTimeMillis() / 1000;
		long t0 = 0l;
		long step = 30l;
		long counter = (seconds - t0) / step;

		String otp = RFC4226.generateOTP(secret, counter, 6, false, -1);
		System.out.println(otp);
	}

	/**
	 * @see net.eckenfels.etotp.GUI#readSecret
	 */
	private static byte[] readSecret(char[] pass) throws Exception {
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

		byte[] salt;
		SecretKey password;
		Cipher c;
		byte[] enc;
		Properties p;
		InputStream is = new FileInputStream(etOtpConfig);
		p = new Properties();
		p.load(is); is.close();

		salt = Base32.decode((String)p.get("key.1.salt"));
		enc = Base32.decode((String)p.get("key.1.encoded"));

		password = f.generateSecret(new PBEKeySpec(pass, salt, 1000, 128));
		// TODO: overwrite pass
		password = new SecretKeySpec(password.getEncoded(), "AES");

		c = Cipher.getInstance("AES");
		c.init(Cipher.DECRYPT_MODE, password);
		byte[] dec = c.doFinal(enc);

		return dec;
	}

}
