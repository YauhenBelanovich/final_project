package com.gmail.yauhen2012.service.constant;

import com.gmail.yauhen2012.repository.model.OrderStatusEnum;

public interface OrderConstant {

    OrderStatusEnum DEFAULT_ORDER_STATUS = OrderStatusEnum.NEW;
    OrderStatusEnum SEND_CART_ORDER_STATUS = OrderStatusEnum.IN_PROGRESS;

}
