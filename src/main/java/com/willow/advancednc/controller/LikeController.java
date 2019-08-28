package com.willow.advancednc.controller;

import com.willow.advancednc.async.EventModel;
import com.willow.advancednc.async.EventProducer;
import com.willow.advancednc.async.EventType;
import com.willow.advancednc.model.Comment;
import com.willow.advancednc.model.EntityType;
import com.willow.advancednc.model.HostHolder;
import com.willow.advancednc.service.CommentService;
import com.willow.advancednc.service.LikeService;
import com.willow.advancednc.utils.ZhiHuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"},method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if(hostHolder.get()==null){
            return ZhiHuUtil.getJSONString(999);
        }
        Comment comment =commentService.getCommentById(commentId);

        eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.get().getId()).setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
                .setExts("questionId",String.valueOf(comment.getEntityId())));

        long likeCount=likeService.like(hostHolder.get().getId(), EntityType.ENTITY_COMMENT,commentId);
        return ZhiHuUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"},method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId){
        if(hostHolder.get()==null){
            return ZhiHuUtil.getJSONString(999);
        }
        Comment comment =commentService.getCommentById(commentId);

        long likeCount=likeService.disLike(hostHolder.get().getId(), EntityType.ENTITY_COMMENT,commentId);
        return ZhiHuUtil.getJSONString(0,String.valueOf(likeCount));
    }

}
