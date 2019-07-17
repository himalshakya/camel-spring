package net.shakya.learnCamelSpringBoot.process;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailProcessor implements Processor {

  private JavaMailSender mailSender;
  private Environment environment;

  public MailProcessor(JavaMailSender mailSender,
      Environment environment) {
    this.mailSender = mailSender;
    this.environment = environment;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
    log.info("Exception caught in mail processor : " + e.getMessage());

    String messageBody = "Exception happened in the camel Route : " + e.getMessage();

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(environment.getProperty("mailFrom"));
    message.setTo(environment.getProperty("mailTo"));
    message.setSubject("Exception in Camel Route");
    message.setText(messageBody);

    mailSender.send(message);
  }
}
