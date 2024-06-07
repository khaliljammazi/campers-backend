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


@SpringBootTest
class CampingCenterServiceImplTest {



    @Test
    void add() {
    }
}
