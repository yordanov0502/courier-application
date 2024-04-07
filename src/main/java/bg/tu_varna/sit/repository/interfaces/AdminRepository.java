package bg.tu_varna.sit.repository.interfaces;

import bg.tu_varna.sit.data.models.entities.Admin;

public interface AdminRepository<T> extends Repository<T> {
    Admin checkCredentials(String username, String password);
}
