package DatabaseAPI;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CowDatabase implements ICowDatabase{

    @Override
    public void insert(CowModel cow) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(CowModel cow) {
        throw new NotImplementedException();
    }

    @Override
    public void update(int id) {
        throw new NotImplementedException();
    }

    @Override
    public CowModel find(int id) {
        throw new NotImplementedException();
    }

    @Override
    public CowModel findBy(String breed, int age) {
        throw new NotImplementedException();
    }

}
