package com.application.seguridad.unir.dto;

import com.application.seguridad.unir.model.StateBooking;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingDto {
    private Integer idBooking;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate reservationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime reservationTime;
    private String description;
    private Integer userId;
    private String name;
    private String lastName;
    private String email;
    private Integer stateBookingId;
    private String stateBookingName;


    public BookingDto(Integer idBooking, LocalDate reservationDate, LocalTime reservationTime, String description, Integer userId, String name, String lastName, String email, Integer stateBookingId, String stateBookingName) {
        this.idBooking = idBooking;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.description = description;
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.stateBookingId = stateBookingId;
        this.stateBookingName = stateBookingName;
    }

    public Integer getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(Integer idBooking) {
        this.idBooking = idBooking;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStateBookingId() {
        return stateBookingId;
    }

    public void setStateBookingId(Integer stateBookingId) {
        this.stateBookingId = stateBookingId;
    }

    public String getStateBookingName() {
        return stateBookingName;
    }

    public void setStateBookingName(String stateBookingName) {
        this.stateBookingName = stateBookingName;
    }
}
