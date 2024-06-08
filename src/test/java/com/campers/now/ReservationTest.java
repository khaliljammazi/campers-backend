package com.campers.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.campers.now.models.Reservation;
import com.campers.now.repositories.ReservationRepository;
import com.campers.now.services.Impl.ReservationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

public class ReservationTest {

    private ReservationServiceImpl reservationService;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        reservationService = new ReservationServiceImpl(reservationRepository);
    }

    @Test
    void testGetReservationStatisticsByMonth() {
        List<Object[]> statistics = new ArrayList<>();
        statistics.add(new Object[]{1, 10}); // Month 1 has 10 reservations
        statistics.add(new Object[]{2, 15}); // Month 2 has 15 reservations
        when(reservationRepository.getReservationCountByMonth()).thenReturn(statistics);
        ResponseEntity<List<Object[]>> result = reservationService.getReservationStatisticsByMonth();
        assertEquals(statistics, result.getBody());
    }
    @Test
    void testDeactivateOldReservations() {
        List<Reservation> reservations = new ArrayList<>();
        Reservation activeReservation1 = new Reservation();
        activeReservation1.setDateStart(new Date());
        activeReservation1.setDateEnd(new Date(System.currentTimeMillis() + 86400000)); // demain
        activeReservation1.setActive(true);
        reservations.add(activeReservation1);
        Reservation activeReservation2 = new Reservation();
        activeReservation2.setDateStart(new Date(System.currentTimeMillis() - 86400000)); // hier
        activeReservation2.setDateEnd(new Date());
        activeReservation2.setActive(true);
        reservations.add(activeReservation2);
        when(reservationRepository.findAll()).thenReturn(reservations);
        reservationService.deactivateOldReservations();
        verify(reservationRepository).deactivateOldReservations(any(Date.class));
    }

}
