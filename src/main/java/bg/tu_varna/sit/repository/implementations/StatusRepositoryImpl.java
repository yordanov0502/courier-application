package bg.tu_varna.sit.repository.implementations;

import bg.tu_varna.sit.data.access.Connection;
import bg.tu_varna.sit.data.models.entities.Admin;
import bg.tu_varna.sit.data.models.entities.Status;
import bg.tu_varna.sit.repository.interfaces.StatusRepository;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class StatusRepositoryImpl implements StatusRepository<Status> {
    private static final Logger log = Logger.getLogger(StatusRepositoryImpl.class);

    //lazy-loaded singleton pattern
    public static StatusRepositoryImpl getInstance() {return StatusRepositoryImpl.StatusRepositoryHolder.INSTANCE;}

    private static class StatusRepositoryHolder {
        public static final StatusRepositoryImpl INSTANCE = new StatusRepositoryImpl();
    }

    @Override
    public boolean save(Status obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(obj); //insert object into table
            transaction.commit();//commit changes to the database
            log.info("Status added successfully.");
            return true;
        } catch (Exception e) {
            log.error("Status add error: " + e);
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Status addStatus(Status obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(obj); // Insert object into table
            transaction.commit(); // Commit changes to the database
            log.info("Status added successfully.");
            return obj; // Return the persisted entity with the generated ID and other details
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Roll back in case of an error
            }
            log.error("Status add error: " + e);
            return null; // Return null or throw an exception
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Status obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(obj);
            transaction.commit();
            log.info("Status updated successfully.");
            return true;
        } catch (Exception e) {
            log.error("Status update error: " + e);
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Status obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(obj);
            transaction.commit();
            log.info("Status deleted successfully.");
            return true;
        }  catch (Exception e) {
            log.info("Status delete error: " + e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }
}
