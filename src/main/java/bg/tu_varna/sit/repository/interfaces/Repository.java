package bg.tu_varna.sit.repository.interfaces;

public interface Repository<T>{
    boolean save(T obj);
    boolean update(T obj);
    boolean delete(T obj);
}
