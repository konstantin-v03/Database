package DatabaseAPI;

public interface CowDatabase {

    boolean insert(CowModel cow);

    boolean delete(CowModel cow);

    boolean update(int recordId);

    CowModel find(int id);

    CowModel findBy(int age);

    CowModel findBy(String breed);

}
