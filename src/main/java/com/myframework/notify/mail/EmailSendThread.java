package com.myframework.notify.mail;

import org.apache.log4j.Logger;

import com.myframework.notify.ISendMessageThread;

/**
 * 邮件发送线程对象
 * 
 * @author zw
 * @version 1.0
 */
public class EmailSendThread implements ISendMessageThread
{
	/**
	 * 日志信息
	 */
	private static final Logger LOGGER = Logger.getLogger(EmailSendThread.class);

	/**
	 * 邮件发送者
	 */
	private String mailFrom;
	/**
	 * 发送者用户名
	 */
	private String userName;
	/**
	 * 发送者密码
	 */
	private String passWord;
	/**
	 * 邮件服务器
	 */
	private String mailServer;
	/**
	 * 邮件发送端口
	 */
	private String mailServerPort;
	/**
	 * 邮件接受者
	 */
	private String mailTo;
	/**
	 * 邮件标题
	 */
	private String title;
	/**
	 * 邮件内容
	 */
	private String content;
	/**
	 * 邮件附件
	 */
	private String[] filePath;
	/**
	 * 是否启用邮件发送
	 */
	private String enable;

	/**
	 * @return the enable
	 */
	public String getEnable()
	{
		return enable;
	}

	/**
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(String enable)
	{
		this.enable = enable;
	}

	/**
	 * 默认构造
	 */
	public EmailSendThread()
	{
	}

	/**
	 * 构造函数
	 * 
	 * @param mailFrom
	 *            邮件发送者
	 * @param passWord
	 *            发送者密码
	 * @param mailServer
	 *            邮件服务器
	 * @param mailServerPort
	 *            邮件服务器端口
	 * @param mailTo
	 *            邮件接收者
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 */
	public EmailSendThread(String mailFrom, String passWord, String mailServer, String mailServerPort, String mailTo,
                           String title, String content)
	{
		this.mailFrom = mailFrom;
		this.passWord = passWord;
		this.mailServer = mailServer;
		this.mailTo = mailTo;
		this.content = content;
		this.title = title;
	}

	/**
	 * 构造函数
	 * 
	 * @param mailFrom
	 *            邮件发送者
	 * @param passWord
	 *            发送者密码
	 * @param mailServer
	 *            邮件服务器
	 * @param mailServerPort
	 *            邮件服务器端口
	 * @param mailTo
	 *            邮件接收者
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @param filePath
	 *            附件文件路径
	 */
	public EmailSendThread(String mailFrom, String passWord, String mailServer, String mailServerPort, String mailTo,
                           String title, String content, String[] filePath)
	{
		this.mailFrom = mailFrom;
		this.passWord = passWord;
		this.mailServer = mailServer;
		this.mailTo = mailTo;
		this.content = content;
		this.title = title;
		this.filePath = filePath;
	}

	/**
	 * @return the mailFrom
	 */
	public String getMailFrom()
	{
		return mailFrom;
	}

	/**
	 * @param mailFrom
	 *            the mailFrom to set
	 */
	public void setMailFrom(String mailFrom)
	{
		this.mailFrom = mailFrom;
	}

	/**
	 * @return the passWord
	 */
	public String getPassWord()
	{
		return passWord;
	}

	/**
	 * @param passWord
	 *            the passWord to set
	 */
	public void setPassWord(String passWord)
	{
		this.passWord = passWord;
	}

	/**
	 * @return the mailServer
	 */
	public String getMailServer()
	{
		return mailServer;
	}

	/**
	 * @param mailServer
	 *            the mailServer to set
	 */
	public void setMailServer(String mailServer)
	{
		this.mailServer = mailServer;
	}

	/**
	 * @return the mailTo
	 */
	public String getMailTo()
	{
		return mailTo;
	}

	/**
	 * @param mailTo
	 *            the mailTo to set
	 */
	public void setMailTo(String mailTo)
	{
		this.mailTo = mailTo;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * @return the filePath
	 */
	public String[] getFilePath()
	{
		return filePath;
	}

	/**
	 * @param filePath
	 *            the filePath to set
	 */
	public void setFilePath(String[] filePath)
	{
		this.filePath = filePath;
	}

	@Override
	public void run()
	{
		EmailSender sendmail = new EmailSender();
		sendmail.setHost(this.mailServer);
		sendmail.setPort(this.mailServerPort);
		sendmail.setSSLPort(this.mailServerPort);
		sendmail.setUserName(this.userName);
		sendmail.setPassWord(this.passWord);
		sendmail.setTo(this.mailTo);
		sendmail.setFrom(this.mailFrom);
		sendmail.setSubject(this.title);
		sendmail.setContent(this.content);

		if(sendmail.sendMail()){
			System.out.println("发送邮件给[" + this.mailTo + "]#"+ this.title +":" + content);
		}
	}

	/**
	 * @return the mailServerPort
	 */
	public String getMailServerPort()
	{
		return mailServerPort;
	}

	/**
	 * @param mailServerPort
	 *            the mailServerPort to set
	 */
	public void setMailServerPort(String mailServerPort)
	{
		this.mailServerPort = mailServerPort;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}
}
