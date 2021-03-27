package com.newgen.utils;


import com.newgen.iforms.custom.IFormReference;
import org.apache.log4j.Logger;

public class MailSetup {
    private static Logger logger = LogGen.getLoggerInstance(MailSetup.class);
    private String wiName;
    private String sendMail;
    private String copyMail;
    private String mailSubject;
    private String mailMessage;

    public MailSetup (IFormReference ifr, String wiName, String sendMail, String copyMail,String mailSubject, String mailMessage){
    this.wiName = wiName;
    this.sendMail = sendMail;
    this.copyMail = copyMail;
    this.mailSubject = mailSubject;
    setMailMessage(mailMessage);
    sendMail(ifr);
    }
    private void setMailMessage(String mailMessage) {
        this.mailMessage = "<html>" +
                "<body>" +
                "Dear User, <br>" +
                "<br>"+mailMessage+"<br>" +
                "<br> Please do not reply, this is a system generated mail. <br>" +
                "</body>" +
                "</html>";
    }

    private void sendMail(IFormReference ifr){
        try {
            if (isMailSent(ifr)) logger.info("Mail sent successfully.");
            else logger.info("Mail not sent.");
        } catch (Exception e){ logger.info("Exception occurred -- Mail was not sent-- "+ e.getMessage());}
    }

    private boolean isMailSent(IFormReference ifr){
        return new DbConnect(ifr, new Query().getMailQuery(wiName,sendMail,copyMail,mailSubject,mailMessage)).saveQuery() >= 0;
    }
}
