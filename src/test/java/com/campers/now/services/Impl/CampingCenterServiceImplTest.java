package com.campers.now.services.Impl;

import com.campers.now.models.Activity;
import com.campers.now.models.CampingCenter;
import com.campers.now.models.FeedBack;
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

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CampingCenterServiceImplTest {

    @MockBean
    private CampingCenterRepository campingCenterRepository;

    @MockBean
    private ActivityRepository activityRepository;

    @MockBean
    private FeedBackRepository feedBackRepository;

    @InjectMocks
    private CampingCenterServiceImpl campingCenterService;

    @BeforeEach
    void setUp() {
        // No need for MockitoAnnotations.openMocks(this); with @ExtendWith(MockitoExtension.class)
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

}
