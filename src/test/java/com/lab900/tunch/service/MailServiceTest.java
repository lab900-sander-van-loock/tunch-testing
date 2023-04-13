package com.lab900.tunch.service;

import com.lab900.tunch.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MailServiceTest {
    @Mock
    private JHipsterProperties jHipsterProperties;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MessageSource messageSource;

    @Mock
    private SpringTemplateEngine templateEngine;
    private MailService mailService;


    @BeforeEach
    void setUp() {
        mailService = new MailService(jHipsterProperties, javaMailSender, messageSource, templateEngine);
    }

    @Test
    void sendActivationEmail() throws MessagingException {
        // Given
        final var emailCaptor = ArgumentCaptor.forClass(MimeMessage.class);

        var user = new User();
        user.setLangKey("en");
        user.setEmail("winand.vandenbergh@lab900.com");

        when(messageSource.getMessage(eq("email.activation.title"), any(), any(Locale.class))).thenReturn("Activation link to activate your account");
        var mail = new JHipsterProperties.Mail();
        mail.setBaseUrl("http://base.url");
        mail.setFrom("test@lab900.com");
        when(jHipsterProperties.getMail()).thenReturn(mail);
        when(javaMailSender.createMimeMessage()).thenReturn(Mockito.mock(MimeMessage.class));
        when(templateEngine.process(anyString(), any())).thenReturn("test");

        // When
        this.mailService.sendActivationEmail(user);

        // Then
        Mockito.verify(javaMailSender, times(1)).send(emailCaptor.capture());

        Assertions.assertThat(emailCaptor.getValue().getSubject()).isEqualTo("Activation link to activate your account");
        Assertions.assertThat(emailCaptor.getValue().getRecipients(Message.RecipientType.TO)).hasToString("test@lab900.com");

    }

    @Test
    void sendCreationEmail() {
    }

    @Test
    void sendPasswordResetMail() {
    }
}
