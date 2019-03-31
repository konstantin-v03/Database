package DatabaseAPI;

import DatabaseCore.RecordStorage;

import java.util.HashMap;
import java.util.Map;

import static Utilities.SerializeHelper.deserialize;
import static Utilities.SerializeHelper.serialize;


public class CowDatabase extends Database<Cow, Integer> {

    private RecordStorage recordStorage;
    private Map<Integer, Integer> byId;

    public CowDatabase(){
        recordStorage = new RecordStorage(48);
        byId = new HashMap<>();
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
    public boolean delete(Cow cow) {
        Integer recordId = byId.get(cow.getId());

        if(recordId == null)
            return false;

        recordStorage.disposeRecord(recordId);

        return true;
    }

    @Override
    public boolean update(Cow lastCow, Cow newCow) {
        Integer recordId = byId.get(lastCow.getId());

        if(recordId == null)
            return false;

        byte bytes[] = serialize(newCow);

        if(bytes == null)
            return false;

        recordStorage.updateRecord(recordId, bytes);

        return true;
    }

    @Override
    public String toString() {
        return recordStorage.toString();
    }
}
