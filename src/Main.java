import DatabaseAPI.CowModel;
import DatabaseCore.RecordStorage;

import java.io.*;

public class Main {

    public static void main(String[] args) {

        RecordStorage recordStorage = new RecordStorage(20);

        String str = "Konstantin Volvenkov0";

        String str1 = "Ivan Ivanov1";

        String str2 = "Vlad Humanov2";

        String str3 = "Anya Kolanova3";

        int a = recordStorage.createRecord(str.getBytes());

        int b = recordStorage.createRecord(str1.getBytes());

        recordStorage.disposeRecord(a);

        recordStorage.disposeRecord(b);

        int c = recordStorage.createRecord(str2.getBytes());

        int d = recordStorage.createRecord(str3.getBytes());

        recordStorage.updateRecord(c, str.getBytes());

        int e = recordStorage.createRecord(str2.getBytes());


        System.out.println(new String(recordStorage.getRecordContent(c)));
        System.out.println(new String(recordStorage.getRecordContent(d)));
        System.out.println(new String(recordStorage.getRecordContent(e)));
        System.out.println(recordStorage);

    }

    private static void saveAsFile(byte bytes[]){
        try {
            FileWriter fileWriter = new FileWriter(new File("database.txt"));
            fileWriter.write(new String(bytes));
            fileWriter.flush();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
