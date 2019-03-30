package DatabaseAPI;

import DatabaseCore.RecordStorage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class SlowCowDatabase implements CowDatabase {

    private RecordStorage recordStorage;

    public SlowCowDatabase(){
        recordStorage = new RecordStorage(48);
    }

    @Override
    public boolean insert(CowModel cow) {
        throw new NotImplementedException();
    }

    @Override
    public boolean delete(CowModel cow) {
        throw new NotImplementedException();
    }

    @Override
    public boolean update(int recordId) {
        throw new NotImplementedException();
    }

    @Override
    public CowModel find(int id) {
        throw new NotImplementedException();
    }

    @Override
    public CowModel findBy(int age) {
        return null;
    }

    @Override
    public CowModel findBy(String breed) {
        return null;
    }

}
