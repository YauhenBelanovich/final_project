package com.gmail.yauhen2012.springbootmodule.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.gmail.yauhen2012.service.ReviewService;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.AddReviewDTO;
import com.gmail.yauhen2012.service.model.AppUser;
import com.gmail.yauhen2012.service.model.ReviewDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
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
        model.addAttribute("reviews", reviewDTOList);
        model.addAttribute("users", userDTOList);
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

    @GetMapping("/new")
    public String addReviewPage(Model model) {
        model.addAttribute("review", new AddReviewDTO());
        logger.debug("Get addReviewPage method");
        return "review_new";
    }

    @PostMapping("/new")
    public String addReivew(@Valid @ModelAttribute(name = "review") AddReviewDTO reviewDTO, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("review", reviewDTO);
            return "review_new";
        } else {

            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
                reviewDTO.setUserId(getCurrentUserId());
                reviewDTO.setStatus(false);
                reviewService.add(reviewDTO);
                logger.debug("Post addReview method");
                return "redirect:/articles";
            } else {
                return "redirect:/login?logout";
            }
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        return userService.loadUserByEmail(user.getUsername()).getId();
    }

}
