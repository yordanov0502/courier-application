package bg.tu_varna.sit.repository.interfaces;

import bg.tu_varna.sit.data.models.entities.Status;

public interface StatusRepository<T> extends Repository<T>{
    Status addStatus(Status obj);
}
