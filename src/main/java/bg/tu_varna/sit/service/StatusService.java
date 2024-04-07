package bg.tu_varna.sit.service;

import bg.tu_varna.sit.data.models.entities.Status;
import bg.tu_varna.sit.repository.implementations.StatusRepositoryImpl;
import org.apache.log4j.Logger;

public class StatusService {
    private static final Logger log = Logger.getLogger(StatusService.class);
    private final StatusService statusService = StatusService.getInstance();
    private final StatusRepositoryImpl statusRepository = StatusRepositoryImpl.getInstance();

    //lazy-loaded singleton pattern
    public static StatusService getInstance() {
        return StatusService.StatusServiceHolder.INSTANCE;
    }

    private static class StatusServiceHolder {
        public static final StatusService INSTANCE = new StatusService();
    }

    //? Used by admin when adding/creating new order
    public boolean createNewStatus(Status status){return statusRepository.save(status);}

    //? Used by customers when adding/creating new order
    public Status addNewStatus(Status status){return statusRepository.addStatus(status);}

}
