package kr.co.danal.element;

import java.util.Map;

import kr.co.danal.exception.ElementExecuteException;
import kr.co.danal.rnd.exception.MalFormattedParamException;
import kr.co.danal.rnd.exception.TeleditClientException;
import kr.co.danal.rnd.util.Cryptography;
import kr.co.danal.rnd.util.HexaHelper;
import kr.co.danal.rnd.util.PacketStringHelper;

public class ServerInfoToTid implements Element {

	@Override
	public String get(String serverInfo) {
		try {
			byte[] dehexaByte = HexaHelper.getDeHexaDumpBytes(serverInfo.toUpperCase());

			byte[] tempByte = new byte[2000];
			int byteLength = Cryptography.getEncodedLength(dehexaByte.length);
			Cryptography.decrypt(tempByte, dehexaByte, Cryptography.FIXED_KEY, byteLength);
			
			String TID = PacketStringHelper.getString(new String(tempByte), "TID");
			return TID;
			
		} catch (TeleditClientException e) {
			throw new ElementExecuteException("ServerInfo HexaByte로 변경 실패.", e, ServerInfoToTid.class);
		} catch (MalFormattedParamException e) {
			throw new ElementExecuteException("ServerInfo에서  TID 추출 실패.", e, ServerInfoToTid.class);
		}
	}

	@Override
	public String get(Map<String, Object> value) {
		return Element.noApplyMapType();
	}

}
