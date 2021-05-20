package kr.co.danal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpManager {
	
	private String url = null;
	private URLConnection urlConn = null;
	private OutputStreamWriter osw = null;
	private BufferedReader br = null;
	
	public HttpManager(String url) throws MalformedURLException, IOException {
		this(url, new HashMap<>());
	}
	
	public HttpManager(String url, Map<String, String> headers) throws MalformedURLException, IOException {
		this.url = url;
		urlConn = new URL(this.url).openConnection();
		
		for(Entry<String, String> header : headers.entrySet()) {
			urlConn.setRequestProperty(header.getKey(), header.getValue());
		}
		
		urlConn.setDoOutput(true);
		osw = new OutputStreamWriter(urlConn.getOutputStream());
	}
	
	public String post(String params) throws IOException {
		osw.write(params);
		osw.flush();
		
		br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
		
		StringBuilder response = new StringBuilder();
		String resData = null;
		while((resData = br.readLine()) != null) {
			response.append(resData);
		}
		
		osw.close();
		br.close();
		
		return response.toString();
	}

//	private String url;
//	private HttpClientHelper http = new HttpClientHelper();
//	
//	public HttpManager(String url) {
//		this.url = url;
//		
//		// Default Setting
//		http.setConnTimeout(3000);
//		http.setReadTimeout(3000);
//		setHeader(http);
////		http.setProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
////		http.setUrlEncodingCharset("UTF-8");
////		http.setLeaveNewlineInResponse(true);
//	}
//
//	public void setHeader(HttpClientHelper http) {
//		
//	}
//
//	public String post(String params) throws Exception {
//		http.executePOST(url, params);
//		return http.getResponseBody();
//	}
//	
//	public String get(String params) throws Exception {
//		http.executeGET(url, params);
//		return http.getResponseBody();
//	}
}
