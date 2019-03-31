package DatabaseAPI;

public interface Database<T, K> {

    boolean insert(T item);

    T extract(K key);

    boolean delete(K key);

    boolean update(K key, T item1);

    boolean save();
}
