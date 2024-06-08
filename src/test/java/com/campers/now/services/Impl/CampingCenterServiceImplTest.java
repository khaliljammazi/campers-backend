package com.campers.now.services.Impl;

import com.campers.now.models.Activity;
import com.campers.now.models.CampingCenter;
import com.campers.now.models.FeedBack;
import com.campers.now.repositories.CampingCenterRepository;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CampingCenterServiceImplTest {

    @MockBean
    private CampingCenterRepository campingCenterRepository;



    @BeforeEach
    void setUp() {
        // No need for MockitoAnnotations.openMocks(this); with @ExtendWith(MockitoExtension.class)
    }

    @Test
    void add() {
        // creation activity
        Activity activity = Activity.builder().label("activity 1")
                .description("description activity 1")
                .price(100).build();
        FeedBack feedBack = FeedBack.builder().label("comment 1").rating(5).build();

        // creation camping center
        CampingCenter campingCenter = CampingCenter.builder().label("camping center 1")
                .description("description 1")
                .capacity(100)
                .activities(Collections.singletonList(activity))
                .feedBacks(Collections.singletonList(feedBack)).build();

        // Mocking the save method
        when(campingCenterRepository.save(any(CampingCenter.class))).thenAnswer(invocation -> {
            CampingCenter cc = invocation.getArgument(0);
            cc.setId(1); // setting a mock id
            return cc;
        });

        // add the camping center
        CampingCenter campingCenterAdded = campingCenterRepository.save(campingCenter);

        // verify that the camping center was added
        assertNotNull(campingCenterAdded.getId());
        //delete the camping center
        campingCenterRepository.delete(campingCenterAdded);
    }
}
