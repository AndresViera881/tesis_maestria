package com.application.seguridad.unir.servicesImpl;

import com.application.seguridad.unir.model.Booking;
import com.application.seguridad.unir.model.StateBooking;
import com.application.seguridad.unir.repositories.IBookingRepository;
import com.application.seguridad.unir.repositories.IGenericRepo;
import com.application.seguridad.unir.repositories.IStateBookingRepository;
import com.application.seguridad.unir.services.IBookingService;
import com.application.seguridad.unir.services.IStateBookingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class StateBookingServiceImpl extends CRUDImpl<StateBooking, Integer> implements IStateBookingService {

    @Autowired
    private IStateBookingRepository repo;
    @Override
    protected IGenericRepo<StateBooking, Integer> getRepo() {
        return repo;
    }

    public Optional<StateBooking> findById(Integer id) {
        return repo.findById(id);
    }
}
