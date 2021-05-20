package kr.co.danal.element;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import kr.co.danal.exception.ElementExecuteException;
import kr.co.danal.util.CryptoUtil;

public class AES256 implements Element {

	private String charset = "UTF-8";
	private String key = "dj19midw8j1gxcz07ce1vyna626m9qbt";
	private String iv = "ha2a1f8s1cjt7ui6";
	private String aesMode = "AES/CBC/PKCS5Padding";
	private String encText = "";
	
	@Override
	public String get(String encText) {
		return encrypt(encText, key, iv, charset, aesMode);
	}

	@Override
	public String get(Map<String, Object> aes256Info) {

			if (aes256Info.containsKey("charset")) {
				charset = aes256Info.get("charset").toString();
			}

			if (aes256Info.containsKey("key")) {
				key = aes256Info.get("key").toString();
			}

			if (aes256Info.containsKey("iv")) {
				iv = aes256Info.get("iv").toString();
			}

			if (aes256Info.containsKey("aesMode")) {
				aesMode = aes256Info.get("aesMode").toString();
			}

			if (aes256Info.containsKey("encText")) {
				encText = aes256Info.get("encText").toString();
			}

			return encrypt(encText, key, iv, charset, aesMode);
	}

	private String encrypt(String encText, String key, String iv, String charset, String aesMode) {
		try {
			byte[] keyByte = key.getBytes(charset);
			byte[] ivByte = iv.getBytes(charset);
			
			return CryptoUtil.getInstance().aes256DecryptBase64(encText, keyByte, ivByte, charset, aesMode);
		} catch (UnsupportedEncodingException e) {
			throw new ElementExecuteException(String.format("%s(key), %s(iv)을 String → byte로 변환 실패", key, iv), e, AES256.class);
		} catch (Exception e) {
			throw new ElementExecuteException(String.format("AES256 복호화 실패. encText : %s", encText), e, AES256.class);
		}
	}
}
