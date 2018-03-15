package com.dl.shop.lottery.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 读取ini文件
 */
public class IniOperator {

	private Map<String, String> kvMap = new HashMap<String, String>();
	private String fileName;
	private InputStream inputStream;
	
	public IniOperator(){
	}
	
	public IniOperator(InputStream inputStream){
        this.inputStream = inputStream;
	}
	
	public IniOperator(String fileName, Map<String, String> kvMap){
		this.fileName = fileName;
		this.kvMap = kvMap;
	}

	public void iniRead() throws IOException{
        BufferedReader reader =  new BufferedReader(new InputStreamReader(this.inputStream));     
        try {
			read(reader);
		} catch (IOException e) {
			throw e;
		}finally{
			reader.close();
			reader = null;
		}       
	}
	
	/**
	 * 获取对应key的value
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
        return kvMap.get(key);
	}
	
	/**
	 * 获取对应iniMap
	 * @param key
	 * @return
	 */
	public Map<String, String> getIniMap() {
        return this.kvMap;
	}
	
//	/**
//	 * 写ini文件
//	 * @author jaybai
//	 * @since 2013-11-6
//	 * @throws IOException
//	 */
//	public void iniWrite() throws IOException {
//		StringBuilder sb = new StringBuilder();
//		boolean isFrist = true;
//		
//		//组装kv值
//		for (Entry<String, String> kv : kvMap.entrySet()) {
//			if (!isFrist) {
//				sb.append("\r\n");			
//			}
//			sb.append(kv.getKey()).append("=").append(kv.getValue());
//			isFrist = false;
//		}
//
//		//写入文件
//		FileUtils.writeStr(fileName, sb.toString());
//	}
	
	/**
	 * 读ini文件
	 * @param reader
	 * @throws IOException
	 */
	protected void read(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
                parseLine(line);
        }
	}
	
	/**
	 * 处理每行数据
	 * @param line
	 */
	protected void parseLine(String line) {
        line = line.trim();
        String[] kv = line.split("=");
	    if (kv.length == 2) {
	    	kvMap.put(kv[0], kv[1]);
		}
	}

	public Map<String, String> getKvMap() {
		return kvMap;
	}

	public void setKvMap(Map<String, String> kvMap) {
		this.kvMap = kvMap;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	


}
