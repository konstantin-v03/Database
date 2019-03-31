package DatabaseAPI;

public abstract class Database<T, K> {

    public abstract boolean insert(T item);

    public abstract T extract(K key);

    public abstract boolean delete(T item);

    public abstract boolean update(T item, T item1);
}
