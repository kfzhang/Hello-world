package com.oakl.servlet;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Servlet implementation class Auto
 */
public class Auto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(HttpServlet.class);
	private byte[] firstContent = null;

	private String url = "http://gsnfu.njfu.edu.cn/html/list/11/index.aspx";
	private boolean change = false;
	ExecutorService threadPool = Executors.newFixedThreadPool(1);
	/**
	 * Default constructor.
	 */
	public Auto() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
			firstContent = HttpService.getContent(url);
			logger.info("firstContent length: " + firstContent.length);
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(60000);
					} catch (Throwable e) {
					}
					while (!change) {
						try {
							byte[] content = HttpService.getContent(url);
							logger.info("content length: " + content.length);
							if (content.length != firstContent.length) {
								change = true;
								MailService.sendMail(url);
							}
							Thread.sleep(300000);
						} catch (Throwable e) {
							continue;
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		threadPool.shutdown();
		change = true;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getOutputStream().write("ok".getBytes());
		response.getOutputStream().close();
	}

}
