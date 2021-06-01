package kr.co.bundletesthelper.element;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import kr.co.bundletesthelper.util.AlertUtil;
import kr.co.bundletesthelper.util.CryptoUtil;

public class RSAEncrytPublic implements Element {

	private static String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtiRwrh3iwqkhmWuSy8fc\n"
			+ "+TIzDlPeHMnBm9eKQn1ttWlOwMuO0AatZhqOOFkbp189FAu7UCKnlVMB3nIgs0PG\n"
			+ "oCaU+nLr567p4ronE5ewELxY9Cp84QVz05jogOXhj9WTu/OJYD0raC5JvXUkHXuP\n"
			+ "V4FeXQmwT6tvu+4vCogB0u5LcG6E2JIeATHewVztdIKdKzrVqaULVwzJ/dFzc5QZ\n"
			+ "aw6ogtl947JbqypVGzjc9guX7LG+kWZtPyJsqQ6l/Tv9Zb8W5Ose/BaSKclc6or7\n"
			+ "A9XYs0xnA7+YiGZsqSPSATPuVi8t4zMPwuurVM5GMRY8+ggVCOH/gj+7vX5yLYI6\n" + "tQIDAQAB";

	public static RSAPublicKey publicKey = null;

	static {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}

		byte[] encoded = Base64.decodeBase64(publicKeyString);
		
		try {
			
			KeyFactory kf = KeyFactory.getInstance("RSA");
			publicKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
			
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			AlertUtil.error(e, "RSAEncryptPublic Element에서 publicKey 생성 실패.");
		}
	}

	@Override
	public String get(String value) {
		return getEncPublicKey(value.getBytes());
	}

	private String getEncPublicKey(byte[] plainData) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encrypted = cipher.doFinal(plainData);
			return CryptoUtil.getInstance().convertByteToHexString(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String get(Map<String, Object> value) {
		return Element.noApplyMapType();
	}
	
}
