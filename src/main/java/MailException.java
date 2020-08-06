import java.util.Arrays;

public class MailException extends Exception {
    String[] arrayOfNotDelivered;

    public MailException(String[] arrayOfNotDelivered){
        this.arrayOfNotDelivered = arrayOfNotDelivered;
    }

    @Override
    public void printStackTrace() {
        System.out.println("Nie dostarczono wiadomości zwrotnej. Lista nie dostarczonych wiadomości: " + Arrays.toString(arrayOfNotDelivered));
    }
}
