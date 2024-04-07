package bg.tu_varna.sit.repository.interfaces;

import bg.tu_varna.sit.data.models.entities.Office;

import java.util.List;

public interface OfficeRepository<T> extends Repository<T> {
    Office getByCityAndName(String office, String name);
    List<Office> getAll();
}
