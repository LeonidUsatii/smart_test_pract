package de.ait.smarttestit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("mail.host"));
        mailSender.setPort(env.getProperty("mail.port", Integer.class));
        mailSender.setUsername(env.getProperty("mail.username"));
        mailSender.setPassword(env.getProperty("mail.password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", env.getProperty("mail.protocol"));
        props.put("mail.smtp.auth", env.getProperty("mail.properties.mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", env.getProperty("mail.properties.mail.smtp.starttls.enable"));
        props.put("mail.debug", env.getProperty("mail.debug"));

        System.out.println("Mail host: " + env.getProperty("MAIL_HOST"));
        System.out.println("Mail port: " + env.getProperty("MAIL_PORT"));

        return mailSender;
    }
}
