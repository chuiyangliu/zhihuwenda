package com.willow.advancednc;

import com.willow.advancednc.model.Question;
import com.willow.advancednc.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdvancedncApplicationTests {

	@Autowired
	SearchService searchService;

	@Test
	public void contextLoads() throws Exception {



	}

}
