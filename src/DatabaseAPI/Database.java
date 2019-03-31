package DatabaseAPI;

public abstract class Database<T> {

    public abstract boolean insert(T item);

    public abstract boolean delete(T item);

    public abstract boolean update(T item, T item1);

    public abstract T find(int id);

}
