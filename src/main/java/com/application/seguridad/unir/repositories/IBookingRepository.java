package com.application.seguridad.unir.repositories;

import com.application.seguridad.unir.dto.BookingDto;
import com.application.seguridad.unir.model.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IBookingRepository extends IGenericRepo<Booking, Integer>{

    @Query("SELECT new com.application.seguridad.unir.dto.BookingDto(" +
            "b.idBooking, b.reservationDate, b.reservationTime, b.description, " +
            "u.id, u.name, u.lastName, u.email, sb.id, sb.name) " +
            "FROM Booking b " +
            "LEFT JOIN b.user u " +
            "LEFT JOIN b.stateBooking sb")
    List<BookingDto> findAllWithUserAndStateBooking();

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.reservationDate = :reservationDate")
    Optional<Booking> findByUserAndReservationDate(@Param("userId") Integer userId, @Param("reservationDate") LocalDateTime reservationDate);
}
