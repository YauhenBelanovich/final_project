package com.gmail.yauhen2012.springbootmodule.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.yauhen2012.service.ReviewService;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.ReviewDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping
    public String getReviewList(@RequestParam(value = "page", defaultValue = "1") String page, Model model) {
        List<ReviewDTO> reviewDTOList = reviewService.getReviewByPage(page);
        List<UserDTO> userDTOList = reviewDTOList.stream()
                .map(ReviewDTO::getUserId)
                .collect(Collectors.toSet())
                .stream()
                .map(userService::findUserById)
                .collect(Collectors.toList());
        model.addAttribute("reviewList", reviewDTOList);
        model.addAttribute("userDTOList", userDTOList);
        logger.debug("Get reviewList method");
        return "reviews";
    }

    @GetMapping("/{id}/delete")
    public String deleteItemById(@PathVariable Long id) {
        reviewService.deleteReviewById(id);
        logger.debug("Get deleteReviewById method");
        return "redirect:/reviews";
    }

    @GetMapping("/{id}/newStatus")
    public String setNewStatus(@PathVariable Long id) {
        reviewService.changeStatus(id);
        logger.debug("Get new status method");
        return "redirect:/reviews";
    }

}
