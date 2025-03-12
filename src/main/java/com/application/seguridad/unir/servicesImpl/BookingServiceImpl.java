package com.application.seguridad.unir.servicesImpl;

import com.application.seguridad.unir.model.Booking;
import com.application.seguridad.unir.model.User;
import com.application.seguridad.unir.repositories.IBookingRepository;
import com.application.seguridad.unir.repositories.IGenericRepo;
import com.application.seguridad.unir.services.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl extends CRUDImpl<Booking, Integer> implements IBookingService {

    @Autowired
    private IBookingRepository repo;
    @Override
    protected IGenericRepo<Booking, Integer> getRepo() {
        return repo;
    }
}
