import DatabaseCore.BlockStorage;
import DatabaseCore.RecordStorage;
import Utilities.ByteSequence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        RecordStorage recordStorage = new RecordStorage(10);

        String str = "Konstantin Volvenkov0";

        String str1 = "Ivan Ivanov1";

        String str2 = "Vlad Humanov2";

        String str3 = "Anya Kolanova3";

        recordStorage.createRecord(str.getBytes());

        recordStorage.createRecord(str1.getBytes());

        recordStorage.disposeRecord(0);

        recordStorage.disposeRecord(3);

        recordStorage.createRecord(str2.getBytes());

        recordStorage.createRecord(str3.getBytes());

        recordStorage.updateRecord(0, str.getBytes());

        recordStorage.createRecord(str2.getBytes());


        System.out.println(new String(recordStorage.getRecordContent(0)));
        System.out.println(new String(recordStorage.getRecordContent(2)));
        System.out.println(new String(recordStorage.getRecordContent(5)));
    }

    private void testBlockStorage(){
        byte source[] = new byte[0];

        BlockStorage blockStorage = new BlockStorage(new ByteSequence(source), 128, 48, 8);
        blockStorage.createBlock();
        blockStorage.createBlock();

        String str = "Hello, World!";

        String str1 = "What's up?";

        blockStorage.findBlock(0).write(str.getBytes(), 0, 0, str.length());
        blockStorage.findBlock(1).write(str1.getBytes(), 0, 0, str1.length());

        saveAsFile(blockStorage.byteSequence.read(0, blockStorage.byteSequence.length()));
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
