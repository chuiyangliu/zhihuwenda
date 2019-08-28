package com.willow.advancednc.controller;

import com.willow.advancednc.async.EventModel;
import com.willow.advancednc.async.EventProducer;
import com.willow.advancednc.async.EventType;
import com.willow.advancednc.model.Comment;
import com.willow.advancednc.model.EntityType;
import com.willow.advancednc.model.HostHolder;
import com.willow.advancednc.service.CommentService;
import com.willow.advancednc.service.QuestionService;
import com.willow.advancednc.utils.ZhiHuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {

    private static final Logger logger= LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;


    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        try{
            Comment comment=new Comment();
            comment.setContent(content);
            if(hostHolder.get()!=null) {
                comment.setUserId(hostHolder.get().getId());
            }else{
                comment.setUserId(ZhiHuUtil.ANONYMOUS_USERID);
            }
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            commentService.addComment(comment);

            int count=commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(),count);

            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUserId())
                    .setEntityId(questionId));

        }catch (Exception e){
            logger.error("添加评论错误"+e.getMessage());
        }
        return "redirect:/question/"+questionId;
    }

}
