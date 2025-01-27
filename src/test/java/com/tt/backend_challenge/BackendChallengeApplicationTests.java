package com.tt.backend_challenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BackendChallengeApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;
	@Test
	void contextLoads() {
        assertThat(applicationContext).isNotNull();
        assertThat(applicationContext.containsBean("employeeController")).isTrue();
        assertThat(applicationContext.containsBean("employeeServiceImpl")).isTrue();
	}

}
