package com.emotion_apiserver.community.communityController;

import com.emotion_apiserver.community.model.Community;
import com.emotion_apiserver.community.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    @Autowired
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    // 커뮤니티 게시물 목록 페이지
    @GetMapping
    public String listCommunity(Model model) {
        List<Community> communities = communityService.findAll();
        model.addAttribute("communities", communities);
        return "community";
    }

    // 커뮤니티 게시물 추가 폼
    @GetMapping("/new")
    public String newCommunityForm(Model model) {
        model.addAttribute("community", new Community());
        return "communityForm";
    }

    // 커뮤니티 게시물 저장
    @PostMapping
    public String saveCommunity(@ModelAttribute Community community) {
        communityService.save(community);
        return "redirect:/community";
    }

    // 커뮤니티 게시물 삭제
    @DeleteMapping("/{id}")
    public String deleteCommunity(@PathVariable Long id) {
        communityService.delete(id);
        return "redirect:/community";
    }
}