package com.willow.advancednc.controller;


import com.willow.advancednc.model.HostHolder;
import com.willow.advancednc.model.Message;
import com.willow.advancednc.model.User;
import com.willow.advancednc.model.ViewObject;
import com.willow.advancednc.service.MessageService;
import com.willow.advancednc.service.UserService;
import com.willow.advancednc.utils.ZhiHuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    private static final Logger logger= LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try{
            if(hostHolder.get()==null){
                return ZhiHuUtil.getJSONString(999,"用户未登陆");
            }

            User user=userService.selectByName(toName);
            if(user==null){
                return ZhiHuUtil.getJSONString(1,"用户不存在");
            }
            Message message=new Message();
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.get().getId());
            message.setToId(user.getId());
            messageService.addMessage(message);
            return ZhiHuUtil.getJSONString(0);

        }catch (Exception e){
            logger.error("发送消息失败"+e.getMessage());
            return ZhiHuUtil.getJSONString(1,"发送失败");
        }
    }

    @RequestMapping(path = {"/msg/list"},method = {RequestMethod.GET})
    public String getConversationList(Model model){
        if(hostHolder.get()==null){
            return "redirect:/reglogin";
        }
        int localId=hostHolder.get().getId();
        List<Message> conversationList=messageService.getConversationList(localId,0,10);
        List<ViewObject> converations=new ArrayList<ViewObject>();
        for(Message message:conversationList){
            ViewObject vo=new ViewObject();
            vo.set("message",message);
            int targetId=message.getFromId()==localId?message.getToId():message.getFromId();
            vo.set("user",userService.getUser(targetId));
            vo.set("unread",messageService.getConversationUnreadCount(localId,message.getConversationId()));
            converations.add(vo);
        }
        model.addAttribute("conversations",converations);
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"},method = {RequestMethod.GET})
    public String getConversationDetail(Model model,@RequestParam("conversationId") String conversationId){
        try{
            List<Message> messageList=messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages=new ArrayList<ViewObject>();
            for(Message message:messageList){
                ViewObject vo=new ViewObject();
                vo.set("message",message);
                vo.set("user",userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            logger.error("获取详情失败"+e.getMessage());
        }
        return "letterDetail";
    }

}
