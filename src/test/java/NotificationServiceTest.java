import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    private final String RESOURCE_PATH = "target/test-classes/NotificationTest/";
    private final String DELIVERY_BACK_SUBJECT = "Powiadomienie: Nie dostarczono";
    private NotificationService notificationService;
    private MailService mailService;

    public NotificationServiceTest() throws Exception {
        mailService = mock(MailService.class);

        doThrow(MailException.class)
                .when(mailService)
                .sendMail("exception@gmail.com", "fromme@gmail.com", getNoticeText("Imie", "Nazwisko", "Success text"), getNoticeSubject("Success subject"));

        doThrow(MailException.class)
                .when(mailService)
                .sendMail("exception@gmail.com", "fromhim@gmail.com", getNoticeText("Waga", "Plec", "Success text"), getNoticeSubject("Success subject"));

        doThrow(MailException.class)
                .when(mailService)
                .sendMail("exception@gmail.com", "frommeexc@gmail.com", getNoticeText("Imie", "Nazwisko", "exception"), getNoticeSubject("exception"));

        doThrow(MailException.class)
                .when(mailService)
                .sendMail("frommeexc@gmail.com", "frommeexc@gmail.com", getNoticeDeliveryBackText("Imie", "Nazwisko", new String[]{"exception@gmail.com"}), DELIVERY_BACK_SUBJECT);

        doThrow(MailException.class)
                .when(mailService)
                .sendMail("exception2@gmail.com", "frommeexc2@gmail.com", getNoticeText("Imie", "Nazwisko", "exception2"), getNoticeSubject("exception2"));

        doThrow(MailException.class)
                .when(mailService)
                .sendMail("frommeexc2@gmail.com", "frommeexc2@gmail.com", getNoticeDeliveryBackText("Imie", "Nazwisko", new String[]{"exception2@gmail.com"}), DELIVERY_BACK_SUBJECT);

        notificationService = new NotificationService(mailService);
    }

    @Test
    void send3NotificationWithoutDeliveringBack() throws Exception {
        checkSendMail(3, "successNotification3.txt", "Imie", "Nazwisko", "Success subject", "Success text", "fromme@gmail.com",
                new String[]{"nos@gmail.com", "no@gmail.com", "n@gmail.com"});
    }

    @Test
    void send2NotificationWithDeliveringBack() throws Exception {
        checkSendMail(5, "successNotificationBack2.txt", "Imie", "Nazwisko", "Success subject", "Success text", "fromme@gmail.com",
                       new String[]{"exception@gmail.com","se@gmail.com","ses@gmail.com","sesss@gmail.com"});
        checkNotDeliveredTo("Imie", "Nazwisko", "fromme@gmail.com", new String[]{"exception@gmail.com"});
    }

    @Test
    void sendNotificationWithErrorAndDeliveryError() {
        Assertions.assertThrows(Exception.class,
                () -> notificationService.sendNotification(getPath(RESOURCE_PATH + "throwException.txt"), "exception", "exception"),
                "Nie dostarczono wiadomości zwrotnej. Lista nie dostarczonych wiadomości: [exception@gmail.com]");
    }

    @Test
    void sendSecondNotificationWithErrorAndDeliveryError() {
        Assertions.assertThrows(Exception.class,
                () -> notificationService.sendNotification(getPath(RESOURCE_PATH + "throwException2.txt"), "exception2", "exception2"),
                "Nie dostarczono wiadomości zwrotnej. Lista nie dostarczonych wiadomości: [exception2@gmail.com]");
    }

    @Test
    void send31NotificationWithoutDeliveringBack() throws Exception {
        checkSendMail(3, "successNotification31.txt", "Waga", "Plec", "Subject", "Text", "fromhim@gmail.com",
                new String[]{"nos@gmail.com", "no@gmail.com", "n@gmail.com"});
    }

    @Test
    void send21NotificationWithDeliveringBack() throws Exception {
        checkSendMail(4, "successNotificationBack21.txt", "Waga", "Plec", "Success subject", "Success text", "fromhim@gmail.com",
                new String[]{"exception@gmail.com","e@gmail.com","se@email.com"});
        checkNotDeliveredTo( "Waga", "Plec",  "fromhim@gmail.com", new String[]{"exception@gmail.com"});
    }

    private void checkSendMail(int timesUsed, String file, String firstname, String lastname, String subject, String text, String from, String... to) throws MailException {
        notificationService.sendNotification(getPath(RESOURCE_PATH + file), subject, text);
        verify(mailService, times(timesUsed)).sendMail(anyString(), anyString(), anyString(), anyString());
        for (String tempTo :
                to) {
            verify(mailService).sendMail(tempTo, from, getNoticeText(firstname, lastname, text), getNoticeSubject(subject));
        }
    }

    private void checkNotDeliveredTo(String firstname, String lastname, String from, String... to) throws MailException {
        verify(mailService).sendMail(from, from, getNoticeDeliveryBackText(firstname, lastname, to), "Powiadomienie: Nie dostarczono");
    }

    private String getNoticeText(String firstname, String lastname, String text) {
        return "Powiadomienie od " + firstname + " " + lastname + " " + text;
    }

    private String getNoticeSubject(String subject) {
        return "Powiadomienie " + subject;
    }

    private String getPath(String path) {
        return Paths.get(path).toString();
    }

    private String getNoticeDeliveryBackText(String firstname, String lastname, String... emails) {
        return "Powiadomienie od " + firstname +
                " " + lastname + ". Nie dostarczono wiadomości do: "
                + Arrays.toString(emails);
    }
}
