package br.com.study.reactivecalendar.domain.service;

import br.com.study.reactivecalendar.core.TemplateMailConfigStub;
import br.com.study.reactivecalendar.core.extension.mail.MailSender;
import br.com.study.reactivecalendar.core.extension.mail.MailServer;
import br.com.study.reactivecalendar.core.extension.mail.MailServerExtension;
import br.com.study.reactivecalendar.core.extension.mail.SMTPPort;
import br.com.study.reactivecalendar.core.factoryBot.dto.MailMessageDTOFactoryBot;
import br.com.study.reactivecalendar.domain.dto.MailMessageDTO;
import br.com.study.reactivecalendar.domain.helper.RetryHelper;
import br.com.study.reactivecalendar.domain.mapper.MailMapper;
import br.com.study.reactivecalendar.domain.mapper.MailMapperImpl;
import br.com.study.reactivecalendar.domain.mapper.MailMapperImpl_;
import com.github.javafaker.Faker;
import com.icegreen.greenmail.util.GreenMail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static br.com.study.reactivecalendar.core.RandomData.getFaker;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MailMapperImpl.class, MailMapperImpl_.class})
@ExtendWith({SpringExtension.class, MailServerExtension.class, MockitoExtension.class})
public class MailServiceTest {

    @SMTPPort
    private final int port = 1234;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private MailMapper mailMapper;
    private MailService mailService;
    private GreenMail smtpServer;
    private final Faker faker = getFaker();
    private final String sender = faker.internet().emailAddress();

    @BeforeEach
    void setup(@MailServer final GreenMail smtpServer, @MailSender final JavaMailSender mailSender){
        this.smtpServer = smtpServer;
        var templateEngine = TemplateMailConfigStub.templateEngine(applicationContext);
        mailService = new MailService(new RetryHelper(1, 2), mailSender, templateEngine, mailMapper, sender);
    }

    private static Stream<Arguments> sendTest(){
        return Stream.of(
                Arguments.of(MailMessageDTOFactoryBot.builder().buildCancelAppointment()),
                Arguments.of(MailMessageDTOFactoryBot.builder().buildEditAppointment()),
                Arguments.of(MailMessageDTOFactoryBot.builder().buildNewAppointment()),
                Arguments.of(MailMessageDTOFactoryBot.builder().buildRemoveAppointment())
        );
    }

    @ParameterizedTest
    @MethodSource
    void sendTest(final MailMessageDTO mailMessage) throws InterruptedException, MessagingException {
        StepVerifier.create(mailService.send(mailMessage)).verifyComplete();
        TimeUnit.SECONDS.sleep(3L);
        assertThat(smtpServer.getReceivedMessages()).isNotEmpty();
        var message = Arrays.stream(smtpServer.getReceivedMessages()).findFirst().orElseThrow();
        assertThat(message.getSubject()).isEqualTo(mailMessage.subject());
        var recipients = mailMessage.destinations().stream().map(d -> {
            try {
                return new InternetAddress((d));
            } catch (AddressException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        assertThat(message.getRecipients(Message.RecipientType.TO)).containsAnyElementsOf(recipients);
        assertThat(message.getHeader("FROM")).contains(sender);
        smtpServer.reset();
    }

}
