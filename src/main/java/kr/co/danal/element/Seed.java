package kr.co.danal.element;

import java.util.Map;

import kr.co.danal.exception.ElementExecuteException;
import kr.co.danal.util.CryptoUtil;

public class Seed implements Element {

	private String seedKey = "1ym33a8hokt84ny8";

	@Override
	public String get(String plainText) {
		return encrytText(seedKey, plainText);
	}

	@Override
	public String get(Map<String, Object> seedInfo) {
		try {
			String plainText = seedInfo.get("plainText").toString();
			seedKey = seedInfo.get("seedKey").toString();
			return encrytText(seedKey, plainText);
		} catch (NullPointerException e) {
			throw new ElementExecuteException(String.format("plainText, seedKey 중 하나가 Null이여서 Seed 암호화 실패."), Seed.class);
		}
	}
	
	private String encrytText(String seedKey, String plainText) {
		try {
			String cipherText = CryptoUtil.getInstance().encryptSeed128ECB(seedKey.getBytes(), plainText);
			return plainText.length() % 16 == 0 ? removePadding(cipherText) : cipherText;
		} catch (Exception e) {
			throw new ElementExecuteException(String.format("%s에 대한 Seed 암호화 실패", plainText), e, Seed.class);
		}
	}
	
	/**
	 * 정확하게 16 byte인 경우에(plainText) 32byte가 추가로 붙는다.
	 * @param value
	 * @return 32byte를 제거한 value
	 */
	private String removePadding(String value) {
		return value.substring(0, value.length() - 32);
	}

}
