package bg.tu_varna.sit.service;

import bg.tu_varna.sit.data.models.entities.Courier;
import bg.tu_varna.sit.repository.implementations.CourierRepositoryImpl;
import org.apache.log4j.Logger;

import javax.swing.plaf.PanelUI;
import java.util.List;

public class CourierService {
    private static final Logger log = Logger.getLogger(CourierService.class);
    private final CourierService courierService = CourierService.getInstance();
    private final CourierRepositoryImpl courierRepository = CourierRepositoryImpl.getInstance();

    //lazy-loaded singleton pattern
    public static CourierService getInstance() {
        return CourierService.CourierServiceHolder.INSTANCE;
    }

    private static class CourierServiceHolder {
        public static final CourierService INSTANCE = new CourierService();
    }

    public Courier login(String username, String password){
        return courierRepository.checkCredentials(username,password);
    }

    public boolean addNewCourier(Courier courier){return  courierRepository.save(courier);}

    public boolean deleteCourierById(Integer courierId){return courierRepository.deleteById(courierId);}

    public boolean updateCourier(Courier courier){return courierRepository.update(courier);}

    public List<Courier> getAllCouriers(){return courierRepository.getAllCouriers();}

    public Courier getCourierByOfficeAddress(String city,String officeName){return courierRepository.getByOfficeAddress(city,officeName);}

    public Courier getCourierById(Integer courierId) {return courierRepository.getById(courierId);}

    public List<Courier> getAllCouriersByCity(String city) {return courierRepository.getAllCouriersByCity(city);}
}
