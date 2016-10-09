package com.myframework.notify.mail;

import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

/** 
 * <p> 
 * Title: 使用javamail发送邮件 
 * </p> 
 */  
public class EmailSender {
	final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory"; 
	String to = "";// 收件人  
    String from = "";// 发件人  
    String host = "";// smtp主机  
    String port = "";// 端口
    String sslport = "";// ssl端口
    String username = "";  
    String password = "";  
    String filename = "";// 附件文件名  
    String subject = "";// 邮件主题  
    String content = "";// 邮件正文  
    Vector file = new Vector();// 附件文件集合  
  
    /** 
     * <br> 
     * 方法说明：默认构造器 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public EmailSender() {  
    }  
  
    /** 
     * <br> 
     * 方法说明：构造器，提供直接的参数传入 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public EmailSender(String to, String from, String smtpServer, String port,String sslport, 
            String username, String password, String subject, String content) {  
        this.to = to;  
        this.from = from;  
        this.host = smtpServer;  
        this.port = port;
        this.sslport = sslport;
        this.username = username;  
        this.password = password;  
        this.subject = subject;  
        this.content = content;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置邮件服务器地址 <br> 
     * 输入参数：String host 邮件服务器地址名称 <br> 
     * 返回类型： 
     */  
    public void setHost(String host) {  
        this.host = host;  
    }  
    
    /** 
     * <br> 
     * 方法说明：设置邮件服务器端口 <br> 
     * 输入参数：String port 邮件服务器端口 <br> 
     * 返回类型： 
     */  
    public void setPort(String port) {  
        this.port = port;  
    }  
    
    /** 
     * <br> 
     * 方法说明：设置邮件服务器ssl端口 <br> 
     * 输入参数：String sslport 邮件服务器地址ssl端口 <br> 
     * 返回类型： 
     */  
    public void setSSLPort(String sslport) {  
        this.sslport = sslport;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置登录服务器校验密码 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void setPassWord(String pwd) {  
        this.password = pwd;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置登录服务器校验用户 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void setUserName(String usn) {  
        this.username = usn;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置邮件发送目的邮箱 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void setTo(String to) {  
        this.to = to;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置邮件发送源邮箱 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void setFrom(String from) {  
        this.from = from;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置邮件主题 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void setSubject(String subject) {  
        this.subject = subject;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置邮件内容 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void setContent(String content) {  
        this.content = content;  
    }  
  
    /** 
     * <br> 
     * 方法说明：把主题转换为中文 <br> 
     * 输入参数：String strText <br> 
     * 返回类型： 
     */  
    public String transferChinese(String strText) {  
        try {  
            strText = MimeUtility.encodeText(new String(strText.getBytes(),  
                    "UTF-8"), "UTF-8", "B");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return strText;  
    }  
  
    /** 
     * <br> 
     * 方法说明：往附件组合中添加附件 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void attachfile(String fname) {  
        file.addElement(fname);  
    }  
  
    /** 
     * <br> 
     * 方法说明：发送邮件 <br> 
     * 输入参数： <br> 
     * 返回类型：boolean 成功为true，反之为false 
     */  
    public boolean sendMail() {  
  
        // 构造mail session  
        Properties props = new Properties() ;  
        props.put("mail.smtp.host", host);  
        props.put("mail.smtp.auth", "true");  
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);  
        props.put("mail.smtp.socketFactory.fallback", "false");  
        props.put("mail.smtp.port", port);  
        props.put("mail.smtp.socketFactory.port", sslport);  
        Session session = Session.getInstance(props,  
                new Authenticator() {  
                    public PasswordAuthentication getPasswordAuthentication() {  
                        return new PasswordAuthentication(username, password);  
                    }  
                });  
        //Session session = Session.getDefaultInstance(props);  
//      Session session = Session.getDefaultInstance(props, null);  
  
        try {  
            // 构造MimeMessage 并设定基本的值  
            MimeMessage msg = new MimeMessage(session);  
            //MimeMessage msg = new MimeMessage();  
            msg.setFrom(new InternetAddress(from));  
           
              
            //msg.addRecipients(Message.RecipientType.TO, address); //这个只能是给一个人发送email  
            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to)) ;  
            subject = transferChinese(subject);  
            msg.setSubject(subject);  
  
            // 构造Multipart  
            MimeMultipart mp = new MimeMultipart();  
  
            // 向Multipart添加正文  
            MimeBodyPart mbpContent = new MimeBodyPart();  
            mbpContent.setContent(content, "text/html;charset=gb2312");  
              
            // 向MimeMessage添加（Multipart代表正文）  
            mp.addBodyPart(mbpContent);  
  
            // 向Multipart添加附件  
            Enumeration efile = file.elements();  
            while (efile.hasMoreElements()) {  
  
                MimeBodyPart mbpFile = new MimeBodyPart();  
                filename = efile.nextElement().toString();  
                FileDataSource fds = new FileDataSource(filename);  
                mbpFile.setDataHandler(new DataHandler(fds));  
                //这个方法可以解决附件乱码问题。   
                String filename= new String(fds.getName().getBytes(),"ISO-8859-1");  
  
                mbpFile.setFileName(filename);  
                // 向MimeMessage添加（Multipart代表附件）  
                mp.addBodyPart(mbpFile);  
  
            }  
  
            file.removeAllElements();  
            // 向Multipart添加MimeMessage  
            msg.setContent(mp);  
            msg.setSentDate(new Date());  
            msg.saveChanges() ;  
            // 发送邮件  
              
            Transport transport = session.getTransport("smtp");  
            transport.connect(host, username, password);  
            transport.sendMessage(msg, msg.getAllRecipients());  
            transport.close();  
        } catch (Exception mex) {  
            mex.printStackTrace();  
//          Exception ex = null;  
//          if ((ex = mex.getNextException()) != null) {  
//              ex.printStackTrace();  
//          }  
            return false;  
        }  
        return true;  
    }  
    
    public boolean sendEmailTo(String recv,String subject,String content) {
    	EmailSender sendmail = new EmailSender();  
        sendmail.setHost("smtp.exmail.qq.com");  
        sendmail.setPort("465");
        sendmail.setSSLPort("465");
        sendmail.setUserName("sender@hunjuwang.com");  
        sendmail.setPassWord("Hunjuwang123456");
        sendmail.setTo(recv);  
        sendmail.setFrom("sender@hunjuwang.com"); 
        sendmail.setSubject(subject);  
        sendmail.setContent(content); 
        
        if(sendmail.sendMail()){
        	System.out.println("发送邮件给[" + recv + "]#"+ subject +":" + content);
        } else {
        	return false;
        }
        
        return true;
    }
  
  
      
    /** 
     * <br> 
     * 方法说明：主方法，用于测试 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public static void main(String[] args) {  
//    	EmailSender sendmail = new EmailSender();
    	//测试发邮件
		EmailSender sm = new EmailSender();  
		sm.sendEmailTo("zhaopeng@hunjuwang.com", "test1", "hello one");
//		sm.sendEmailTo("zhaowei@hunjuwang.com", "test2", "hello two");
    }  
}
