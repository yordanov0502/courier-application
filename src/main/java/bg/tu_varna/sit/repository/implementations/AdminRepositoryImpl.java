package bg.tu_varna.sit.repository.implementations;

import bg.tu_varna.sit.common.Hasher;
import bg.tu_varna.sit.data.access.Connection;
import bg.tu_varna.sit.data.models.entities.Admin;
import bg.tu_varna.sit.repository.interfaces.AdminRepository;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AdminRepositoryImpl implements AdminRepository<Admin> {

    private static final Logger log = Logger.getLogger(AdminRepositoryImpl.class);

    //lazy-loaded singleton pattern
    public static AdminRepositoryImpl getInstance() {return AdminRepositoryImpl.AdminRepositoryHolder.INSTANCE;}

    private static class AdminRepositoryHolder {
        public static final AdminRepositoryImpl INSTANCE = new AdminRepositoryImpl();
    }

    @Override
    public boolean save(Admin obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(obj); //insert object into table
            transaction.commit();//commit changes to the database
            log.info("Admin added successfully.");
            return true;
        } catch (Exception e) {
            log.error("Admin add error: " + e);
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Admin obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(obj);
            transaction.commit();
            log.info("Admin updated successfully.");
            return true;
        } catch (Exception e) {
            log.error("Admin update error: " + e);
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Admin obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(obj);
            transaction.commit();
            log.info("Admin deleted successfully.");
            return true;
        }  catch (Exception e) {
            log.info("Admin delete error: " + e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Admin checkCredentials(String username, String passwordInput) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        Admin admin = null;

        try{
            String jpql = "SELECT a FROM Admin a WHERE username = '" + username + "' AND password = '" + Hasher.SHA512.hash(passwordInput) + "'";
            admin = (Admin) session.createQuery(jpql).getSingleResult();
            transaction.commit();
            if(admin != null) return admin;
            log.info("Got admin by username & password successfully.");
        } catch (Exception e) {
            log.error("Get admin by username & password error: " + e.getMessage());
            return null;
        } finally {
            session.close();
        }
        return null;
    }
}
