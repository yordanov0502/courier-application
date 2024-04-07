package bg.tu_varna.sit.service;

import bg.tu_varna.sit.data.models.entities.Admin;
import bg.tu_varna.sit.repository.implementations.AdminRepositoryImpl;
import org.apache.log4j.Logger;

public class AdminService {
    private static final Logger log = Logger.getLogger(AdminService.class);
    private final AdminService adminService = AdminService.getInstance();
    private final AdminRepositoryImpl adminRepository = AdminRepositoryImpl.getInstance();

    //lazy-loaded singleton pattern
    public static AdminService getInstance() {
        return AdminServiceHolder.INSTANCE;
    }

    private static class AdminServiceHolder {
        public static final AdminService INSTANCE = new AdminService();
    }

    public Admin login(String username, String password){
        return adminRepository.checkCredentials(username,password);
    }

}
