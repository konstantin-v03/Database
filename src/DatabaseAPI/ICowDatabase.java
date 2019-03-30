package DatabaseAPI;

public interface ICowDatabase {

    void insert(CowModel cow);

    void delete(CowModel cow);

    void update(int id);

    CowModel find(int id);

    CowModel findBy(String breed, int age);

}
