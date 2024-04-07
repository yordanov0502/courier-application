package bg.tu_varna.sit.repository.interfaces;

import bg.tu_varna.sit.data.models.entities.Courier;

import java.util.List;

public interface CourierRepository<T> extends Repository<T> {
    Courier checkCredentials(String username, String password);
    List<Courier> getAllCouriers();
    boolean deleteById(Integer courierId);
    Courier getByOfficeAddress(String city,String officeName);
    Courier getById(Integer courierId);
}
