import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class MailDataTest {
    private final String RESOURCE_PATH = "target/test-classes/MailDataTest/";

    @Test
    void fileNulltxtNotFound() {
        fileNotFoundInPath("null.txt");
    }

    @Test
    void fileNullxmlNotFound() {
        fileNotFoundInPath("null.xml");
    }

    @Test
    void checkIfDataIsCorrect() throws IOException {
        isDataCorrect("correctLoad.txt", "fromnew@gmail.com", "jas", "Wilgoci", new String[]{"checksoms@gmail.com", "checksom@gmail.com"});
    }

    @Test
    void checkIfDataIsCorrectAgain() throws IOException {
        isDataCorrect("correctLoad2.txt", "fromnew2@gmail.com", "Piotr", "Butelka", new String[]{"checksoms2@gmail.com", "checksom2@gmail.com"});
    }

    private void isDataCorrect(String path, String from, String firstname, String lastname, String... to) throws IOException {
        MailData mailData = new MailData(RESOURCE_PATH + path);
        Assertions.assertEquals(mailData.getFrom(), from);
        Assertions.assertEquals(mailData.getFirstname(), firstname);
        Assertions.assertEquals(mailData.getLastname(), lastname);
        Assertions.assertEquals(Arrays.toString(mailData.getTo().toArray()), Arrays.toString(to));
    }

    @Test
    void dataShouldHaveAtLeast3Terminators() {
        Assertions.assertThrows(IOException.class, () -> new MailData("throwException.txt"), "Plik powinien zawierac conajmniej 3 terminatory ','");
    }

    @Test
    void dataShouldHaveAtLeast3TerminatorsButHas2() {
        Assertions.assertThrows(IOException.class, () -> new MailData("throwException.txt"), "Plik powinien zawierac conajmniej 3 terminatory ','");
    }

    void fileNotFoundInPath(String path) {
        Assertions.assertThrows(Exception.class, () -> new MailData("throwExceptionNotFound.txt"));
    }

    private String getPath(String path) {
        return Paths.get(path).toString();
    }
}
