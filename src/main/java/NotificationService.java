import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class NotificationService {

    private MailService mailService;
    private MailData mailData;

    public NotificationService(MailService mailService) {
        this.mailService = mailService;
    }

    public void sendNotification(String path, String subject, String text) throws MailException {
        try {
            mailData = new MailData(path);
            subject = "Powiadomienie " + subject;
            text = "Powiadomienie od " + mailData.getFirstname() + " " + mailData.getLastname() + " " + text;
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> notDelivered = new ArrayList<>();
        for (String temp :
                mailData.getTo()) {
            try {
                mailService.sendMail(temp, mailData.getFrom(), text, subject);
            } catch (MailException e) {
                notDelivered.add(temp);
            }
        }

        if (notDelivered.size() > 0)
            deliverBack(notDelivered);
    }

    public void deliverBack(ArrayList<String> to) throws MailException {
        try {
            String text = "Powiadomienie od " + mailData.getFirstname() +
                    " " + mailData.getLastname() + ". Nie dostarczono wiadomości do: "
                    + Arrays.toString(to.toArray());
            mailService.sendMail(mailData.getFrom(), mailData.getFrom(), text, "Powiadomienie: Nie dostarczono");
        } catch (MailException e) {
            throw new MailException((String[]) to.toArray());
        }

    }
}
