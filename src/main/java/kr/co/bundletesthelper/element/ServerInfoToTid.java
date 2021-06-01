package kr.co.bundletesthelper.element;

import java.util.Map;

import kr.co.bundletesthelper.exception.ElementExecuteException;
import kr.co.bundletesthelper.rnd.exception.MalFormattedParamException;
import kr.co.bundletesthelper.rnd.exception.TeleditClientException;
import kr.co.bundletesthelper.rnd.util.Cryptography;
import kr.co.bundletesthelper.rnd.util.HexaHelper;
import kr.co.bundletesthelper.rnd.util.PacketStringHelper;

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
