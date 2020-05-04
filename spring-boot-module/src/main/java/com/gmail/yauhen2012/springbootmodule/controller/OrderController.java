package com.gmail.yauhen2012.springbootmodule.controller;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhen2012.repository.model.OrderStatusEnum;
import com.gmail.yauhen2012.service.OrderService;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.AddOrderDTO;
import com.gmail.yauhen2012.service.model.AppUser;
import com.gmail.yauhen2012.service.model.OrderDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/new")
    public String addOrder(@RequestParam(value = "itemId", required = false) Long itemId,
            @RequestParam(value = "quantity", required = false) Long quantity,
            RedirectAttributes redirectAttributes) {

        Long userId = getCurrentUserId();

        if (quantity != null) {
            List<Long> itemIdsList = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                itemIdsList.add(itemId);
            }
            OrderDTO orderDTO = orderService.findOrderByUserIdWithStatusNEW(userId);
            if (orderDTO == null) {
                AddOrderDTO addOrderDTO = new AddOrderDTO();
                addOrderDTO.setUserId(userId);
                addOrderDTO.setItemsId(itemIdsList);
                orderService.add(addOrderDTO);
                logger.debug("Get addOrder method");
            } else {
                Long orderId = orderDTO.getOrderId();
                orderService.addItemsToOrder(orderId, itemIdsList);
                logger.debug("Get addItemsToOrder method");
            }
        } else {
            redirectAttributes.addFlashAttribute("noItemsSelectedMessage", "No items selected");
        }
        return "redirect:/items";
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable Long id, Model model) {
        OrderDTO orderDTO = orderService.findById(id);
        if (orderDTO != null) {
            model.addAttribute("order", orderDTO);
            logger.debug("Get orderById method");
            return "order";
        }
        logger.error("Get orderById method! No items found!!!");
        return "redirect:/?error";
    }

    @GetMapping("/{id}/newStatus")
    public String setNewStatus(@PathVariable Long id, @RequestParam(name = "status") OrderStatusEnum status) {
        if (orderService.changeStatus(id, status)) {
            logger.debug("Get new status method");
            return "redirect:/orders?success";
        }
        logger.error("Get newStatus method! No orders found!!!");
        return "redirect:/orders?error";
    }

    @GetMapping("/cart")
    public String getCart(Model model) {
        Long userId = getCurrentUserId();
        OrderDTO orderDTO = orderService.findOrderByUserIdWithStatusNEW(userId);
        if (orderDTO != null) {
            model.addAttribute("order", orderDTO);
            logger.debug("Get cart method");
            return "cart";
        }
        logger.debug("Get cart method. Cart is empty");
        return "redirect:/items?cartIsEmpty";
    }

    @GetMapping("/cart/item/delete")
    public String deleteItemFromOrder(@RequestParam(value = "itemId") Long itemId,
            @RequestParam(value = "orderId") Long orderId) {

        if (orderService.deleteItemByIdFromOrder(orderId, itemId)) {
            logger.debug("Get deleteItemByIdFromOrder method");
            return "redirect:/orders/cart";
        }
        logger.debug("Get deleteItemFromCart method. Something went wrong");
        return "redirect:/orders/cart?error";
    }

    @GetMapping("/cart/editQuantity")
    public String editItemQuantity(@RequestParam(value = "itemId") Long itemId,
            @RequestParam(value = "orderId") Long orderId,
            @RequestParam(value = "quantity") Long quantity) {

        if (quantity != null) {
            List<Long> itemIdsList = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                itemIdsList.add(itemId);
            }
            if (orderService.deleteItemByIdFromOrder(orderId, itemId)) {
                orderService.addItemsToOrder(orderId, itemIdsList);
                logger.debug("Get editItemQuantity method");
                return "redirect:/orders/cart";
            }
        }
        logger.debug("Get deleteItemFromCart method. Something went wrong");
        return "redirect:/orders/cart?error";
    }

    @GetMapping("/cart/send")
    public String sendCart(@RequestParam(value = "orderId") Long orderId) {
        if (orderService.sendCart(orderId)) {
            logger.debug("Get sendCart method");
            return "redirect:/orders";
        }
        logger.debug("Get deleteItemFromCart method. Something went wrong");
        return "redirect:/orders/cart?error";
    }

    @GetMapping
    public String getOrdersList(@RequestParam(value = "page", defaultValue = "1") String page, Model model) {

        List<OrderDTO> orderDTOList = orderService.getSortedOrdersByPage(page);
        model.addAttribute("orders", orderDTOList);
        logger.debug("Get ordersList method");
        return "orders";
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        return userService.loadUserByEmail(user.getUsername()).getId();
    }

}
