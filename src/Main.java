import DatabaseCore.BlockStorage;
import Utilities.ByteSequence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        byte source[] = new byte[0];
        BlockStorage blockStorage = new BlockStorage(new ByteSequence(source), 128, 48, 8);
        blockStorage.createBlock();
        blockStorage.createBlock();

        String str = "Hello, World!";

        String str1 = "What's up?";

        blockStorage.find(0).write(str.getBytes(), 0, 0, str.length());
        blockStorage.find(1).write(str1.getBytes(), 0, 0, str1.length());

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
