package com.willow.advancednc.async.handler;


import com.willow.advancednc.async.EventHandler;
import com.willow.advancednc.async.EventModel;
import com.willow.advancednc.async.EventType;
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
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        // 给被赞的人发message
        int fromId = ZhiHuUtil.SYSTEM_USERID;
        int toId = model.getEntityOwnerId();
        Message message = new Message();
        message.setFromId(fromId);
        message.setToId(toId);
        message.setCreatedDate(new Date());
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
        User user = userService.getUser(model.getActorId());
        message.setContent("用户'" + user.getName() + "'赞了你的问题,http://127.0.0.1:8080/question/" + model.getExts("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE); // 只关注LIKE的事件
    }
}