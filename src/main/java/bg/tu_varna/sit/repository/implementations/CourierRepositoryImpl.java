package bg.tu_varna.sit.repository.implementations;

import bg.tu_varna.sit.common.Hasher;
import bg.tu_varna.sit.data.access.Connection;
import bg.tu_varna.sit.data.models.entities.Courier;
import bg.tu_varna.sit.repository.interfaces.CourierRepository;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CourierRepositoryImpl implements CourierRepository<Courier> {
    private static final Logger log = Logger.getLogger(CourierRepositoryImpl.class);

    //lazy-loaded singleton pattern
    public static CourierRepositoryImpl getInstance() {return CourierRepositoryImpl.CourierRepositoryHolder.INSTANCE;}

    private static class CourierRepositoryHolder {
        public static final CourierRepositoryImpl INSTANCE = new CourierRepositoryImpl();
    }

    @Override
    public boolean save(Courier obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(obj); //insert object into table
            transaction.commit();//commit changes to the database
            log.info("Courier added successfully.");
            return true;
        } catch (Exception e) {
            log.error("Courier add error: " + e);
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Courier obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(obj);
            transaction.commit();
            log.info("Courier updated successfully.");
            return true;
        } catch (Exception e) {
            log.error("Courier update error: " + e);
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Courier obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(obj);
            transaction.commit();
            log.info("Courier deleted successfully.");
            return true;
        }  catch (Exception e) {
            log.info("Courier delete error: " + e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Courier checkCredentials(String username, String passwordInput) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        Courier courier = null;

        try{
            String jpql = "SELECT co FROM Courier co WHERE username = '" + username + "' AND password = '" + Hasher.SHA512.hash(passwordInput) + "'";
            courier = (Courier) session.createQuery(jpql).getSingleResult();
            transaction.commit();
            if(courier != null) return courier;
            log.info("Got courier by username & password successfully.");
        } catch (Exception e) {
            log.error("Get courier by username & password error: " + e.getMessage());
            return null;
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public List<Courier> getAllCouriers() {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        List<Courier> couriers = new ArrayList<>();
        try{
            String jpql = "SELECT c FROM Courier c";
            couriers.addAll(session.createQuery(jpql, Courier.class).getResultList());
            transaction.commit();
            log.info("Got all couriers successfully.");
        } catch (Exception e) {
            log.error("Get all couriers error: " + e.getMessage());
        } finally {
            session.close();
        }
        return couriers;
    }

    @Override
    public boolean deleteById(Integer courierId) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Courier courier = session.get(Courier.class, courierId);
            if (courier != null) {
                session.delete(courier);
                transaction.commit();
                log.info("Courier with ID " + courierId + " deleted successfully.");
                return true;
            } else {
                log.info("No courier found with ID: " + courierId);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.info("Courier delete error: " + e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Courier getByOfficeAddress(String city, String officeName) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        Courier courier = null;

        try{
            String jpql = "SELECT co FROM Courier co WHERE co.office.city = '" + city + "' AND co.office.officeName = '" + officeName + "'";
            courier = (Courier) session.createQuery(jpql).getSingleResult();
            transaction.commit();
            if(courier != null) return courier;
            log.info("Got courier by city & address successfully.");
        } catch (Exception e) {
            log.error("Get courier by city & address error: " + e.getMessage());
            return null;
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public Courier getById(Integer courierId) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        Courier courier = null;

        try{
            String jpql = "SELECT co FROM Courier co WHERE co.id = '" + courierId + "'";
            courier = (Courier) session.createQuery(jpql).getSingleResult();
            transaction.commit();
            if(courier != null) return courier;
            log.info("Got courier by ID successfully.");
        } catch (Exception e) {
            log.error("Get courier by ID error: " + e.getMessage());
            return null;
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public List<Courier> getAllCouriersByCity(String city) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        List<Courier> couriers = new ArrayList<>();
        try{
            String jpql = "SELECT c FROM Courier c WHERE c.office.city = '"+city+"'";
            couriers.addAll(session.createQuery(jpql, Courier.class).getResultList());
            transaction.commit();
            log.info("Got all couriers by city successfully.");
        } catch (Exception e) {
            log.error("Get all couriers by city error: " + e.getMessage());
        } finally {
            session.close();
        }
        return couriers;
    }


}
