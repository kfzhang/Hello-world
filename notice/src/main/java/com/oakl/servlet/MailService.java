package com.oakl.servlet;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MailService {
	static Logger logger = LoggerFactory.getLogger(HttpService.class);
	public static void main(String[] args) {
		sendMail("http://gsnfu.njfu.edu.cn/");
	}

	public static void sendMail(String url) {
		logger.info("send mail");
		Properties p = new Properties();
		p.put("mail.smtp.host", "smtp.126.com");
		p.put("mail.smtp.port", "25");
		p.put("mail.smtp.auth", "true");
		// 如果需要身份认证，则创建一个密码验证器
		MyAuthenticator authenticator = new MyAuthenticator("******@126.com", "*******");
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(p, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress("********@126.com");
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress("********@163.com");
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject("网站已修改");
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			mailMessage.setText(url);
			// 发送邮件
			Transport.send(mailMessage);
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		logger.info("邮件发送成功");
	}

}
