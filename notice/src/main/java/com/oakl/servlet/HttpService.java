package com.oakl.servlet;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpService {
	static Logger logger = LoggerFactory.getLogger(HttpService.class);
	public static void main(String[] args) throws Exception {
		byte[] content = HttpService.getContent("http://gsnfu.njfu.edu.cn/html/list/11/index.aspx");
		logger.info(new String(content));
	}

	public static byte[] getContent(String url) throws Exception {
		logger.info("getContent");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(url);

			logger.info("Executing request " + httpget.getRequestLine());

			// Create a custom response handler
			ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {
				public byte[] handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toByteArray(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};
			byte[] responseBody = httpclient.execute(httpget, responseHandler);
			logger.info("----------------------------------------");
			return responseBody;
		} finally {
			logger.info("release httpclient");
			httpclient.close();
		}
	}

}
