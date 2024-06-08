package com.campers.now.services.Impl;

import com.campers.now.models.Activity;
import com.campers.now.models.CampingCenter;
import com.campers.now.models.FeedBack;
import com.campers.now.models.Reservation;
import com.campers.now.repositories.ActivityRepository;
import com.campers.now.repositories.CampingCenterRepository;
import com.campers.now.repositories.FeedBackRepository;
import com.campers.now.services.CampingCenterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CampingCenterServiceImplTest {

    @MockBean
    private CampingCenterRepository campingCenterRepository;

    @MockBean
    private ReservationServiceImpl reservationService;

    @InjectMocks
    private CampingCenterServiceImpl campingCenterService;

    private CampingCenter campingCenter1;
    private CampingCenter campingCenter2;

    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation reservation3;

    @BeforeEach
    void setUp() {
        // Set up mock data for reservations
        Calendar calendar = Calendar.getInstance();

        calendar.set(2023, Calendar.JUNE, 1);
        Date startDate1 = calendar.getTime();
        calendar.set(2023, Calendar.JUNE, 10);
        Date endDate1 = calendar.getTime();

        calendar.set(2023, Calendar.JULY, 1);
        Date startDate2 = calendar.getTime();
        calendar.set(2023, Calendar.JULY, 15);
        Date endDate2 = calendar.getTime();

        reservation1 = Reservation.builder()
                .numberReserved(10)
                .isActive(true)
                .totalAmount(1000)
                .dateStart(startDate1)
                .dateEnd(endDate1)
                .build();

        reservation2 = Reservation.builder()
                .numberReserved(5)
                .isActive(true)
                .totalAmount(1500)
                .dateStart(startDate2)
                .dateEnd(endDate2)
                .build();

        campingCenter1 = CampingCenter.builder()
                .capacity(100)
                .reservations(Arrays.asList(reservation1, reservation2))
                .build();

        calendar.set(2023, Calendar.AUGUST, 1);
        Date startDate3 = calendar.getTime();
        calendar.set(2023, Calendar.AUGUST, 20);
        Date endDate3 = calendar.getTime();

        reservation3 = Reservation.builder()
                .numberReserved(20)
                .isActive(true)
                .totalAmount(3000)
                .dateStart(startDate3)
                .dateEnd(endDate3)
                .build();

        campingCenter2 = CampingCenter.builder()
                .capacity(200)
                .reservations(Collections.singletonList(reservation3))
                .build();
    }

    @Test
    void calculateOccupancyRate() {
        // Mock the repository method
        when(campingCenterRepository.findAll()).thenReturn(Arrays.asList(campingCenter1, campingCenter2));

        // Calculate occupancy rate
        double[] occupancyRates = campingCenterService.calculateOccupancyRate();

        // Expected values
        double totalCapacity = 300;
        double totalOccupiedSpaces = 35;
        double occupancyRate = totalOccupiedSpaces / totalCapacity * 100;
        double unoccupiedSpacesPercent = 100 - occupancyRate;

        double[] expectedRates = {
                Math.round(unoccupiedSpacesPercent * 100.0) / 100.0,
                Math.round(occupancyRate * 100.0) / 100.0
        };

        // Verify the results
        assertArrayEquals(expectedRates, occupancyRates);
    }

    @Test
    void add() {
        // Create activity
        Activity activity = Activity.builder().label("activity 1")
                .description("description activity 1")
                .price(100).build();

        // Create feedback
        FeedBack feedBack = FeedBack.builder().label("comment 1").rating(5).build();

        // Create camping center
        CampingCenter campingCenter = CampingCenter.builder().label("camping center 1")
                .description("description 1")
                .capacity(100)
                .activities(Collections.singletonList(activity))
                .feedBacks(Collections.singletonList(feedBack)).build();

        // Mock the save method for CampingCenterRepository
        when(campingCenterRepository.save(any(CampingCenter.class))).thenAnswer(invocation -> {
            CampingCenter cc = invocation.getArgument(0);
            cc.setId(1); // Mock ID setting
            return cc;
        });

        // Add the camping center
        CampingCenter campingCenterAdded = campingCenterService.add(campingCenter);

        // Verify that the camping center was added with ID set
        assertNotNull(campingCenterAdded.getId());
        assertEquals(1, campingCenterAdded.getId());
        assertEquals("camping center 1", campingCenterAdded.getLabel());
        assertEquals("description 1", campingCenterAdded.getDescription());
        assertEquals(100, campingCenterAdded.getCapacity());

        // Verify associated activity
        assertNotNull(campingCenterAdded.getActivities());
        assertEquals(1, campingCenterAdded.getActivities().size());
        assertEquals("activity 1", campingCenterAdded.getActivities().get(0).getLabel());

        // Verify associated feedback
        assertNotNull(campingCenterAdded.getFeedBacks());
        assertEquals(1, campingCenterAdded.getFeedBacks().size());
        assertEquals("comment 1", campingCenterAdded.getFeedBacks().get(0).getLabel());
        assertEquals(5, campingCenterAdded.getFeedBacks().get(0).getRating());

        // Verify the relationships are set correctly
        assertEquals(campingCenterAdded, campingCenterAdded.getActivities().get(0).getCampingCenter());
        //assertEquals(campingCenterAdded, campingCenterAdded.getFeedBacks().get(0).getCampingCenter());

        // Mock the delete method for CampingCenterRepository
        campingCenterRepository.delete(campingCenterAdded);
    }

    @Test
    void calculateRevenuePerOccupiedSpace() {
        // Mock the reservation service method
        when(reservationService.getAll()).thenReturn(Arrays.asList(reservation1, reservation2, reservation3));

        // Calculate revenue per occupied space
        double revenuePerOccupiedSpace = campingCenterService.calculateRevenuePerOccupiedSpace();

        // Expected values
        double totalRevenue = 1000 + 1500 + 3000;
        int totalOccupiedSpaces = 10 * 10 + 5 * 15 + 20 * 20; // numberReserved * campingPeriodInDays

        double expectedRevenuePerOccupiedSpace = totalRevenue / totalOccupiedSpaces;
    }

}
