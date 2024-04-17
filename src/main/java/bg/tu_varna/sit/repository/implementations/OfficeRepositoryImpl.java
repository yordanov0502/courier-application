package bg.tu_varna.sit.repository.implementations;

import bg.tu_varna.sit.data.access.Connection;
import bg.tu_varna.sit.data.models.entities.Office;
import bg.tu_varna.sit.repository.interfaces.OfficeRepository;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class OfficeRepositoryImpl implements OfficeRepository<Office> {
    private static final Logger log = Logger.getLogger(OfficeRepositoryImpl.class);

    //lazy-loaded singleton pattern
    public static OfficeRepositoryImpl getInstance() {return OfficeRepositoryImpl.OfficeRepositoryHolder.INSTANCE;}

    private static class OfficeRepositoryHolder {
        public static final OfficeRepositoryImpl INSTANCE = new OfficeRepositoryImpl();
    }

    @Override
    public boolean save(Office obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(obj); //insert object into table
            transaction.commit();//commit changes to the database
            log.info("Office added successfully.");
            return true;
        } catch (Exception e) {
            log.error("Office add error: " + e);
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Office obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(obj);
            transaction.commit();
            log.info("Office updated successfully.");
            return true;
        } catch (Exception e) {
            log.error("Office update error: " + e);
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Office obj) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(obj);
            transaction.commit();
            log.info("Office deleted successfully.");
            return true;
        }  catch (Exception e) {
            log.info("Office delete error: " + e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Office getByCityAndName(String city, String name) {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        Office office = null;

        try {
            String jpql = "SELECT o FROM Office o WHERE city = '"+city+"' AND officeName = '" + name + "'";
            office = (Office) session.createQuery(jpql).getSingleResult();
            transaction.commit();
            log.info("Got office by city and name successfully.");
        } catch(Exception e) {
            log.error("Get office by city and name error: " + e.getMessage());
            return null;
        } finally {
            session.close();
        }
        return office;
    }

    @Override
    public List<Office> getAll() {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        List<Office> offices = new ArrayList<>();
        try{
            String jpql = "SELECT o FROM Office o";
            offices.addAll(session.createQuery(jpql, Office.class).getResultList());
            transaction.commit();
            log.info("Got all offices successfully.");
        } catch (Exception e) {
            log.error("Get all offices error: " + e.getMessage());
        } finally {
            session.close();
        }
        return offices;
    }

    @Override
    public List<String> getAllDistinctCities() {
        Session session = Connection.openSession();
        Transaction transaction = session.beginTransaction();
        List<String> offices = new ArrayList<>();
        try{
            String jpql = "SELECT DISTINCT(o.city) FROM Office o";
            offices.addAll(session.createQuery(jpql, String.class).getResultList());
            transaction.commit();
            log.info("Got all distinct offices successfully.");
        } catch (Exception e) {
            log.error("Get all distinct offices error: " + e.getMessage());
        } finally {
            session.close();
        }
        return offices;
    }

    //?????????????????????? with parameter set in jpql ???????????????????????????
    @Override
    public boolean cityHasOffice(String city) {
        Session session = Connection.openSession();

        try
        {
            String jpql = "SELECT COUNT(o) FROM Office o WHERE o.city = :city";
            Long count = (Long) session.createQuery(jpql)
                    .setParameter("city", city)
                    .getSingleResult();
            log.info("Checked for office by city successfully.");
            return count > 0;
        }
        catch(Exception e)
        {
            log.error("Check for office by city error: " + e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }
    //?????????????????????? with parameter set in jpql ???????????????????????????

}
