package DatabaseAPI;

import DatabaseCore.RecordStorage;
import Utilities.ByteSequence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static Utilities.SerializeHelper.deserialize;
import static Utilities.SerializeHelper.serialize;

public abstract class Database<T, K> {

    private static final int BLOCK_CONTENT_SIZE = 48;

    private RecordStorage recordStorage;
    private Map<K, Integer> map;

    private String fileName;

    protected Database(String fileName, boolean isReCreate){
        if(fileName == null)
            throw new IllegalArgumentException("Illegal fileName");

        map = new HashMap<>();
        this.fileName = fileName;

        if(!isReCreate){
            if(!initFromFile(fileName))
                recordStorage = new RecordStorage(BLOCK_CONTENT_SIZE);
        }else
            recordStorage = new RecordStorage(BLOCK_CONTENT_SIZE);
    }

    public final boolean insert(T item){
        byte bytes[] = serialize(item);

        if(bytes == null)
            return false;

        int recordId = recordStorage.createRecord(bytes);

        map.put(getKey(item), recordId);

        return true;
    }

    @SuppressWarnings("unchecked")
    public final T extract(K key){
        Integer recordId = map.get(key);

        if(recordId == null)
            return null;

        return (T) deserialize(recordStorage.getRecordContent(recordId));
    }

    public final boolean delete(K key){
        Integer recordId = map.get(key);

        if(recordId == null)
            return false;

        recordStorage.disposeRecord(recordId);

        map.remove(key);

        return true;
    }

    public final boolean save(){
        try(FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName))) {
            fileOutputStream.write(recordStorage.getBytes());
        }catch (IOException ex){
            return false;
        }

        return true;
    }

    protected abstract K getKey(T item);

    @Override
    public String toString() {
        return recordStorage.toString();
    }

    @SuppressWarnings("unchecked")
    private boolean initFromFile(String fileName){
        try {
            byte fileContent[] = Files.readAllBytes(new File(fileName).toPath());
            recordStorage = new RecordStorage(new ByteSequence(fileContent), BLOCK_CONTENT_SIZE);

            byte recordContent[];

            for(int i = 0; i < recordStorage.getNumBlocks(); i++){
                if((recordContent = recordStorage.getRecordContent(i)) != null){
                    map.put((getKey((T)deserialize(recordContent))), i);
                }
            }

        }catch (IOException ex){
            return false;
        }

        return true;
    }
}
