package DatabaseAPI;

import DatabaseCore.RecordStorage;
import Utilities.ByteSequence;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static Utilities.SerializeHelper.deserialize;
import static Utilities.SerializeHelper.serialize;


public class CowDatabase implements Database<Cow, Integer> {

    private final static int BLOCK_CONTENT_SIZE = 48;
    private RecordStorage recordStorage;
    private Map<Integer, Integer> byId; //allow get "Cow" using their id

    private String fileName;

    public CowDatabase(String fileName, boolean isReCreate){
        if(fileName == null)
            throw new IllegalArgumentException("Illegal fileName");

        byId = new HashMap<>();
        this.fileName = fileName;

        if(isReCreate){
            recordStorage = new RecordStorage(BLOCK_CONTENT_SIZE);
            return;
        }

        try {
            byte fileContent[] = Files.readAllBytes(new File(fileName).toPath());
            recordStorage = new RecordStorage(new ByteSequence(fileContent), BLOCK_CONTENT_SIZE);

            byte recordContent[];

            for(int i = 0; i < recordStorage.getNumBlocks(); i++){
                if((recordContent = recordStorage.getRecordContent(i)) != null){
                    byId.put(((Cow)deserialize(recordContent)).getId(), i);
                }
            }

        }catch (IOException ex){
            recordStorage = new RecordStorage(BLOCK_CONTENT_SIZE);
        }
    }

    @Override
    public boolean insert(Cow cow) {
        byte bytes[] = serialize(cow);

        if(bytes == null)
            return false;

        int recordId = recordStorage.createRecord(bytes);

        byId.put(cow.getId(), recordId);

        return true;
    }

    @Override
    public Cow extract(Integer key) {
        Integer recordId = byId.get(key);

        if(recordId == null)
            return null;

        return (Cow) deserialize(recordStorage.getRecordContent(recordId));
    }

    @Override
    public boolean delete(Integer key) {
        Integer recordId = byId.get(key);

        if(recordId == null)
            return false;

        recordStorage.disposeRecord(recordId);

        byId.remove(key);

        return true;
    }

    @Override
    public boolean update(Integer key, Cow newCow) {
        Integer recordId = byId.get(key);

        if(recordId == null)
            return false;

        byte bytes[] = serialize(newCow);

        if(bytes == null)
            return false;

        recordStorage.updateRecord(recordId, bytes);

        byId.remove(key);
        byId.put(newCow.getId(), recordId);

        return true;
    }

    @Override
    public boolean save() {
        try(FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName))) {
            fileOutputStream.write(recordStorage.getBytes());
        }catch (IOException ex){
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return recordStorage.toString();
    }

}

