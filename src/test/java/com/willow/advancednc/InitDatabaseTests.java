package com.willow.advancednc;


import com.willow.advancednc.dao.QuestionDAO;
import com.willow.advancednc.dao.UserDAO;
import com.willow.advancednc.model.Question;
import com.willow.advancednc.model.User;
import com.willow.advancednc.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {
	@Autowired
	UserDAO userDAO;
	@Autowired
	QuestionDAO questionDAO;
	@Autowired
	FollowService followService;

	@Test
	public void initDatabase() {
		Random random = new Random();

		for(int i=0; i<20; ++i){
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d",i+1));
			user.setPassword("XX");
			user.setSalt("");
			userDAO.addUser(user);



			Question question = new Question();
			question.setCommentCount(i+1);
			Date date = new Date();
			date.setTime(date.getTime() + 60*60*1000*i);
			question.setCreatedDate(date);
			question.setUserId(i+1);
			question.setTitle(String.format("Title%d",i+1));
			question.setContent(String.format("Content %d",i+1));
			questionDAO.addQuestion(question);
		}
	}
}