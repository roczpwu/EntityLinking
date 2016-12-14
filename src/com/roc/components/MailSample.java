package com.roc.components;

import com.roc.components.mail.MailSenderInfo;
import com.roc.components.mail.SimpleMailSender;

/**
 * Created by rocwu on 2016/10/26.
 */
public class MailSample {
    public static void main(String[] args) {
        //这个类主要是设置邮件
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.163.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUserName("******@163.com");
        mailInfo.setPassword("******");//您的邮箱密码
        mailInfo.setFromAddress("******@163.com");
        mailInfo.setToAddress("******@qq.com");
        mailInfo.setSubject("关于晨会");
        mailInfo.setContent("大家好：\n明早10点请准时参加讨论会，讨论项目进展问题。\n祝好！");
        //这个类主要来发送邮件
        SimpleMailSender.sendTextMail(mailInfo);//发送文体格式
        SimpleMailSender.sendHtmlMail(mailInfo);//发送html格式
    }
}
