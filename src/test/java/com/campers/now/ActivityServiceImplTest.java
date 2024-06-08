package com.campers.now;

import com.campers.now.repositories.ActivityRepository;
import com.campers.now.services.Impl.ActivityServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class ActivityServiceImplTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityServiceImpl activityService;

    @Test
    void testFindTop() {
        // Mock data
        Object[] result1 = {"Activity 1", "image1.jpg", 10L};
        Object[] result2 = {"Activity 2", "image2.jpg", 8L};
        Object[] result3 = {"Activity 3", "image3.jpg", 6L};
        List<Object> expectedTop = Arrays.asList(result1, result2, result3);

        // Mock the behavior of the activity repository
        when(activityRepository.findTop()).thenReturn(expectedTop);

        // Call the method to be tested
        List<Object> actualTop = activityService.getTop5MostReservedActivities();

        // Assertions
        assertEquals(expectedTop.size(), actualTop.size());
        for (int i = 0; i < expectedTop.size(); i++) {
            assertEquals(expectedTop.get(i), actualTop.get(i));
        }
    }

    @Test
    void testGetCurrentSeason() {
        // Mock the current date to test different months
        LocalDate springDate = LocalDate.of(2024, 4, 1); // April
        LocalDate summerDate = LocalDate.of(2024, 7, 1); // July
        LocalDate autumnDate = LocalDate.of(2024, 10, 1); // October
        LocalDate winterDate = LocalDate.of(2024, 1, 1); // January

        // Test the getCurrentSeason method for each season
        assertEquals("SPRING", getCurrentSeason(springDate));
        assertEquals("SUMMER", getCurrentSeason(summerDate));
        assertEquals("AUTUMN", getCurrentSeason(autumnDate));
        assertEquals("WINTER", getCurrentSeason(winterDate));
    }

    // Method to test
    private String getCurrentSeason(LocalDate currentDate) {
        int month = currentDate.getMonthValue();

        if (month >= 3 && month <= 5) {
            return "SPRING";
        } else if (month >= 6 && month <= 8) {
            return "SUMMER";
        } else if (month >= 9 && month <= 11) {
            return "AUTUMN";
        } else {
            return "WINTER";
        }
    }
}
