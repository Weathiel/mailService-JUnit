public interface MailService {
    void sendMail(String to, String from, String text, String subject) throws MailException;
}
