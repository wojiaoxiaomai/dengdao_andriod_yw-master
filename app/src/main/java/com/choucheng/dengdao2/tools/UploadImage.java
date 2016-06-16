package com.choucheng.dengdao2.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class UploadImage {
	private static final String TAG="UploadImage";
	private String BOUNDARY = java.util.UUID.randomUUID().toString();
	private String PREFIX = "--", LINEND = "\r\n";
	private String MULTIPART_FROM_DATA = "multipart/form-data";
	
	/**
	 * 图片的封装
	 * @param outStream
	 * @param files
	 * @throws Exception
	 */
	public void addImages(DataOutputStream outStream,Map<String, File> files )throws Exception{
		String srcPath;
		StringBuilder sb1;
		InputStream is;
		for (Map.Entry<String, File> file : files.entrySet()) {
			srcPath=file.getValue().getAbsolutePath();
			sb1 = new StringBuilder();
			sb1.append(PREFIX);
			sb1.append(BOUNDARY);
			sb1.append(LINEND);
			sb1.append("Content-Disposition: form-data; name=\""+file.getKey()+"\"; filename=\""
					+  srcPath.substring(srcPath.lastIndexOf("/") + 1) + "\"" + LINEND);
			sb1.append("Content-Type: image/jpg"+LINEND);
			sb1.append("Content-Transfer-Encoding: binary"+LINEND);
			sb1.append(LINEND);
			Logger.i(TAG, sb1.toString());
			outStream.write(sb1.toString().getBytes());

			is = new FileInputStream(file.getValue());
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			Logger.i(TAG, "image");
			is.close();
			Logger.i(TAG,LINEND);
			outStream.write(LINEND.getBytes());
		}
	}
	
	  /**
	   * 参数封装
	   * @param outStream
	   * @param params
	   * @throws Exception
	   */
	  public void addFormField(DataOutputStream outStream,Map<String, String> params) throws Exception{  
	        StringBuilder sb = new StringBuilder();  
	        for (Map.Entry<String, String> entry : params.entrySet()) {
	        	sb.append(PREFIX);
	        	sb.append(BOUNDARY);
	        	sb.append(LINEND);  
	            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);  
	            sb.append(LINEND);    
	            sb.append(entry.getValue() + LINEND);  
	        }  
	        Logger.i(TAG, sb.toString());
        	outStream.writeBytes(sb.toString());// 发送表单字段数据  
	    } 

	
	/**
	 * 图片上传
	 * @param actionUrl 为要使用的URL
	 * @param params 为表单内容
	 * @param files 要上传的文件，可以上传多个文件
	 * @return
	 * @throws Exception
	 */
	public  String upload(String actionUrl,Map<String, String> params,Map<String, File> files) throws Exception{
		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(5 * 1000);
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false);
		conn.setRequestMethod("POST"); // Post方式
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");

		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);
		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		addFormField(outStream,params);
		addImages(outStream,files);
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		Logger.i(TAG, PREFIX + BOUNDARY + PREFIX + LINEND);
		outStream.write(end_data);
		outStream.flush();
		// 得到响应码
		int code = conn.getResponseCode();
		if (code != 200) {
			conn.disconnect();
			return "";
		} else {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String result = "";
			String line = null;
			while ((line = br.readLine()) != null) {
				result += line;
			}
			br.close();
			conn.disconnect();
			return result;
		}
	}

}
