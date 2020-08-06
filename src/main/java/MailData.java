import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class MailData {
    private String from, firstname, lastname;
    private ArrayList<String> to = new ArrayList<>();

    public MailData(String path) throws IOException {
        setMailDataFromString(loadStringFromFile(path));
    }

    public ArrayList<String> getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    private String loadStringFromFile(String path) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (Exception e) {
            throw new IOException("Nie znaleziono pliku " + path);
        }
        return contentBuilder.toString();
    }

    private void setMailDataFromString(String data) throws ArrayIndexOutOfBoundsException {
        data = data.trim();
        String[] temp = data.split(",");
        if (temp.length < 4)
            throw new ArrayIndexOutOfBoundsException("Plik powinien zawierac conajmniej 3 terminatory ','");
        firstname = temp[0];
        lastname = temp[1];
        from = temp[2];
        for (int i = 3; i < temp.length; i++) {
            to.add(temp[i]);
        }
    }


}
