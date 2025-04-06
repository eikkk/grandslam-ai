package com.plainprog.grandslam_ai.service.email;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.IOException;
import java.util.Map;

@Component
public class EmailServiceJavaMail {
    @Autowired
    private FreeMarkerConfig freemarkerConfig;
    @Autowired
    private JavaMailSender emailSender;

    public boolean sendTemplateEmail(String to, String subject, Map<String, Object> model, String templateName)
    {
        MimeMessage message;
        try {
            message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);

            Template template = freemarkerConfig.getConfiguration().getTemplate(templateName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            helper.setText(html, true);
        } catch (Exception e) {
            return false;
        }

        emailSender.send(message);
        return true;
    }
}