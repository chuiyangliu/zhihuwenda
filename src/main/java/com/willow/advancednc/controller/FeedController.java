package com.willow.advancednc.controller;


import com.willow.advancednc.model.EntityType;
import com.willow.advancednc.model.Feed;
import com.willow.advancednc.model.HostHolder;
import com.willow.advancednc.service.FeedService;

import com.willow.advancednc.service.FollowService;
import com.willow.advancednc.utils.JedisAdapter;
import com.willow.advancednc.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    @Autowired
    FeedService feedService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;



    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String getPushFeeds(Model model){

        int localUserId = hostHolder.get().getId();
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<Feed>();
        for (String feedId : feedIds) {
            Feed feed = feedService.getById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String getPullFeeds(Model model){
        int localUserId = hostHolder.get()!= null ? hostHolder.get().getId() : 0;
        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0) {
            // 关注的人
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

}
