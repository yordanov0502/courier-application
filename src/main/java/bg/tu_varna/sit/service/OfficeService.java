package bg.tu_varna.sit.service;

import bg.tu_varna.sit.data.models.entities.Customer;
import bg.tu_varna.sit.data.models.entities.Office;
import bg.tu_varna.sit.repository.implementations.CustomerRepositoryImpl;
import bg.tu_varna.sit.repository.implementations.OfficeRepositoryImpl;
import org.apache.log4j.Logger;

import java.util.List;

public class OfficeService {
    private static final Logger log = Logger.getLogger(OfficeService.class);
    private final OfficeService officeService = OfficeService.getInstance();
    private final OfficeRepositoryImpl officeRepository = OfficeRepositoryImpl.getInstance();

    //lazy-loaded singleton pattern
    public static OfficeService getInstance() {
        return OfficeService.OfficeServiceHolder.INSTANCE;
    }

    private static class OfficeServiceHolder {
        public static final OfficeService INSTANCE = new OfficeService();
    }

    public Office getOfficeByCityAndName(String city,String name){return officeRepository.getByCityAndName(city,name);}

    public List<Office> getAllOffices(){return officeRepository.getAll();}

    public List<String> getAllDistinctCities(){return officeRepository.getAllDistinctCities();}

    public boolean cityHasOffice(String city) {return officeRepository.cityHasOffice(city);}

}
