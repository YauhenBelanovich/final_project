package com.gmail.yauhen2012.springbootmodule.runner;

import java.lang.invoke.MethodHandles;

import com.gmail.yauhen2012.service.ReviewService;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.AddReviewDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ReviewService reviewService;
    private final UserService userService;


    public MyApplicationRunner(ReviewService reviewService, UserService userService) {this.reviewService = reviewService;
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        AddUserDTO addUserDTO = new AddUserDTO();
//        addUserDTO.setFirstName("Yauh");
//        addUserDTO.setLastName("Bel");
//        addUserDTO.setEmail("yahen2012@frg");
//        addUserDTO.setPatronymic("Genadz");
//        addUserDTO.setRole(RoleEnum.ADMINISTRATOR);
//        userService.add(addUserDTO);

//        AddUserDTO addUserDTO2 = new AddUserDTO();
//        addUserDTO2.setFirstName("Yauhen");
//        addUserDTO2.setLastName("Belaniv");
//        addUserDTO2.setEmail("yauhen2012@gmail.com");
//        addUserDTO2.setPatronymic("Henadz");
//        addUserDTO2.setRole(RoleEnum.ADMINISTRATOR);
//        userService.add(addUserDTO2);

//        userService.changeRole(2L, RoleEnum.CUSTOMER_USER);
//        userService.deleteUserById(1L);

        AddReviewDTO addReviewDTO = new AddReviewDTO();
        addReviewDTO.setText("cdscdscdscdnfmv nfmv");
        addReviewDTO.setStatus(false);
        addReviewDTO.setUserId(1L);
        reviewService.add(addReviewDTO);

        AddReviewDTO addReviewDTO2 = new AddReviewDTO();
        addReviewDTO2.setText("cdscdscdscdnfmv nfmv");
        addReviewDTO2.setStatus(true);
        addReviewDTO2.setUserId(1L);
        reviewService.add(addReviewDTO2);

        AddReviewDTO addReviewDTO3 = new AddReviewDTO();
        addReviewDTO3.setText("cdscdscdscdnfmv nfmv");
        addReviewDTO3.setStatus(true);
        addReviewDTO3.setUserId(1L);
        reviewService.add(addReviewDTO3);

//        List<ReviewDTO> reviewDTOList = reviewService.getReviewByPage("1");
//        List<UserDTO> userDTOS = reviewDTOList.stream()
//                .map(ReviewDTO::getUserId)
//                .collect(Collectors.toSet())
//                .stream()
//                .map(userService::findUserById)
//                .collect(Collectors.toList());
//        System.out.println(userDTOS.size());



        //        List<ReviewDTO> reviewDTOS = reviewService.getReviewByPage("1");
//        reviewDTOS.stream()
//                .map(ReviewDTO::getCreatedBy)
//                .forEach(System.out::println);
       // reviewService.deleteReviewById(1L);
       // System.out.println(reviewService.findReviewById(2L).getCreatedBy());

    }

}
