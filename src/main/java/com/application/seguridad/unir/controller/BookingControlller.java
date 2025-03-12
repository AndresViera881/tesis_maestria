package com.application.seguridad.unir.controller;

import com.application.seguridad.unir.dto.BookingDto;
import com.application.seguridad.unir.exception.ModeloNotFoundException;
import com.application.seguridad.unir.model.Booking;
import com.application.seguridad.unir.model.StateBooking;
import com.application.seguridad.unir.model.User;
import com.application.seguridad.unir.repositories.IBookingRepository;
import com.application.seguridad.unir.repositories.IStateBookingRepository;
import com.application.seguridad.unir.repositories.IUserRepository;
import com.application.seguridad.unir.services.IBookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservas")
public class BookingControlller {
    private IBookingService _bookingService;
    private IStateBookingRepository _stateBookingRepository;
    private IBookingRepository _bookingRepository;
    private IUserRepository _userRepository;
    @Autowired
    private ModelMapper _mapper;

    public BookingControlller(IBookingService bookingService,
                              IBookingRepository bookingRepository,
                              IUserRepository userRepository,
                              IStateBookingRepository stateBookingRepository,
                              ModelMapper mapper) {
        this._bookingService = bookingService;
        this._bookingRepository = bookingRepository;
        this._userRepository = userRepository;
        this._stateBookingRepository = stateBookingRepository;
        this._mapper = mapper;
    }

    @GetMapping("listarReservas")
    public ResponseEntity<List<BookingDto>> listarReservas() throws Exception {
        List<BookingDto> lista = _bookingRepository.findAllWithUserAndStateBooking().stream()
                .map(p -> _mapper.map(p, BookingDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PostMapping("registrarReserva")
    public ResponseEntity<Void> registrarReserva(@RequestBody BookingDto dtoRequest) throws Exception {
        Booking reserva = _mapper.map(dtoRequest, Booking.class);
        User user = _userRepository.findByUserId(dtoRequest.getUserId());
        if (user == null) {
            throw new ModeloNotFoundException("USER NOT FOUND: " + dtoRequest.getUserId());
        }
        reserva.setUser(user);
        Optional<StateBooking> stateBooking = _stateBookingRepository.findById(dtoRequest.getStateBookingId());
        stateBooking.ifPresentOrElse(
                reserva::setStateBooking,
                () -> { throw new ModeloNotFoundException("STATE BOOKING NOT FOUND: " + dtoRequest.getStateBookingId()); }
        );

        Booking reservaGuardada = _bookingService.Register(reserva);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservaGuardada.getIdBooking())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("modificarReserva/{id}")
    public ResponseEntity<Void> modificarReserva(@PathVariable Integer id, @RequestBody BookingDto dtoRequest) throws Exception {
        Booking reserva = _bookingService.ListById(id);
        _mapper.getConfiguration().setSkipNullEnabled(true);
        _mapper.map(dtoRequest, reserva);
        // Verificar y asignar usuario
        if (dtoRequest.getUserId() != null) {
            User user = _userRepository.findByUserId(dtoRequest.getUserId());
            if (user == null) {
                throw new ModeloNotFoundException("USER NOT FOUND: " + dtoRequest.getUserId());
            }
            reserva.setUser(user);
        }
        // Verificar y asignar estado de reserva
        if (dtoRequest.getStateBookingId() != null) {
            Optional<StateBooking> stateBooking = _stateBookingRepository.findById(dtoRequest.getStateBookingId());
            stateBooking.ifPresentOrElse(
                    reserva::setStateBooking,
                    () -> { throw new ModeloNotFoundException("STATE BOOKING NOT FOUND: " + dtoRequest.getStateBookingId()); }
            );
        }
        // Guardar cambios
        _bookingService.Update(reserva);
        return ResponseEntity.noContent().build();
    }





}
