package com.willow.advancednc.async.handler;


import com.willow.advancednc.async.EventHandler;
import com.willow.advancednc.async.EventModel;
import com.willow.advancednc.async.EventType;
import com.willow.advancednc.model.EntityType;
import com.willow.advancednc.model.Message;
import com.willow.advancednc.model.User;
import com.willow.advancednc.service.MessageService;
import com.willow.advancednc.service.UserService;
import com.willow.advancednc.utils.ZhiHuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class FollowHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        // 给关注者发私信
        int fromId= ZhiHuUtil.SYSTEM_USERID;
        int toId = model.getEntityOwnerId();
        Message message = new Message();
        message.setFromId(fromId);
        message.setToId(toId);
        message.setCreatedDate(new Date());
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
        User user = userService.getUser(model.getActorId());
        if (model.getEntityType() == EntityType.ENTITY_QUESTION) {
            message.setContent("用户'" + user.getName() + "'关注了你的问题,http://127.0.0.1:8080/question/" + model.getEntityId());
        } else if (model.getEntityType() == EntityType.ENTITY_USER) {
            message.setContent("用户'" + user.getName() + "'关注了你,http://127.0.0.1:8080/user/" + model.getActorId());
        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}