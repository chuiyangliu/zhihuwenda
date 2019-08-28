package com.willow.advancednc.async.handler;


import com.willow.advancednc.async.EventHandler;
import com.willow.advancednc.async.EventModel;
import com.willow.advancednc.async.EventType;
import com.willow.advancednc.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AddQuestionHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(AddQuestionHandler.class);

    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EventModel model) {
        try {
            searchService.indexQuestion(model.getEntityId(),
                    model.getExts("title"), model.getExts("content"));
        } catch (Exception e) {
            logger.error("增加题目索引失败");
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
