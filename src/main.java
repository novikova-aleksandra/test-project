import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sandra
 * Date: 17.11.13
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */


public class main {

    public static void main (String args []) {

        HashMap<String, String> yesterday = new HashMap<String, String>();
        HashMap<String, String> today = new HashMap<String, String>();
        ArrayList<String> removedPages = new ArrayList<String>();
        ArrayList<String> newPages = new ArrayList<String>();
        ArrayList<String> editedPages = new ArrayList<String>();

        yesterday.putAll(fillTable("yesterday.txt"));
        today.putAll(fillTable("today.txt"));
        newPages.addAll(today.keySet());
        Set<String> keys = yesterday.keySet();

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

        System.out.println(newPages.toString());
        System.out.println(removedPages.toString());
        System.out.println(editedPages.toString());

    }

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
                ex.printStackTrace();
            }
        }
        return result;
    }


}
