import java.io.*;
import java.util.*;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Main {

    public static void main (String args []) {

        HashMap<String, String> yesterday = new HashMap<String, String>();
        HashMap<String, String> today = new HashMap<String, String>();
        ArrayList<String> removedPages = new ArrayList<String>();
        ArrayList<String> newPages = new ArrayList<String>();
        ArrayList<String> editedPages = new ArrayList<String>();
        final String sender = "softaria.test.project@gmail.com";
        final String password = "softariatest";
        final String receiver = "softaria.test.project@gmail.com";

        yesterday.putAll(fillTable("yesterday.txt"));
        today.putAll(fillTable("today.txt"));
        newPages.addAll(today.keySet());
        Set<String> keys = yesterday.keySet();

        //pages comparison, fills in the ArrayLists
        for (String key : keys) {
            if (!today.containsKey(key)) {
                removedPages.add(key);
            }
            else {
                if (!yesterday.get(key).equals(today.get(key))) {
                        editedPages.add(key);
                }
            }
            newPages.remove(key);
        }

        String message = createEmail(removedPages, editedPages, newPages);
        sendEmail(sender, password, receiver, message);

    }

    //fills the HashMap with data from file (first line - key, second line - value)
    private static HashMap<String, String> fillTable(String fileName) {

        HashMap<String, String> result = null;
        InputStream file;
        BufferedReader br = null;
        String line;
        try {
            result = new HashMap<String, String>();
            file = new FileInputStream(fileName);
            br = new BufferedReader(new InputStreamReader(file));
            while ((line = br.readLine()) != null) {
                result.put(line, br.readLine());
            }
        }

        catch (Exception ex) {
            ex.printStackTrace();
        }

        finally {
            try {
                if (br != null) {
                    br.close();
                }
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return result;
    }

    //compiles Email with new, removed and edited pages
    private static String createEmail(ArrayList<String> removedPages, ArrayList<String> editedPages, ArrayList<String> newPages) {
        final String beginning = "Здравствуйте, дорогая и.о. секретаря\n\nЗа последние сутки во вверенных Вам сайтах произошли следующие изменения:\n";
        final String firstUrlStr = "Исчезли следующие страницы:\n";
        final String secondUrlStr = "Появились следующие новые страницы:\n";
        final String thirdUrlStr = "Изменились следующие страницы:\n";
        final String ending = "\nС уважением, любящая вас,\nавтоматизированная система мониторинга.";
        String firstUrl = "";
        String secondUrl = "";
        String thirdUrl = "";
        String email = beginning;
        if (!removedPages.isEmpty()) {
            for (String url : removedPages) {
                firstUrl += url + "\n";
            }
            email += firstUrlStr + firstUrl;
        }
        if (!newPages.isEmpty()) {
            for (String url : newPages) {
                secondUrl += url + "\n";
            }
            email += secondUrlStr + secondUrl;
        }
        if (!editedPages.isEmpty()) {
            for (String url : editedPages) {
                thirdUrl += url + "\n";
            }
            email += thirdUrlStr + thirdUrl;
        }
        email += ending;
        return email;
    }

    //sends the Email, works for gmail accounts
    private static void sendEmail(String sender, String pass, String receiver, String msg) {

        final String username = sender;
        final String password = pass;
        final String subject = "Изменения страниц сайтов";
        final String printout = "Email has been sent successfully to " + receiver;

        //properties for gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);

            System.out.println(printout);

        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }


}
