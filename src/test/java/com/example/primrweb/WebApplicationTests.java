package com.example.primrweb;

import static com.example.primrweb.TestUtils.SPRING_PROFILE_NAME;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_NAME)
public class WebApplicationTests {

	@Test
	public void contextLoads() {
	}

}
