package com.gmail.yauhen2012.service.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.gmail.yauhen2012.repository.model.OrderStatusEnum;

public class OrderDTO {

    private Long orderId;
    private String date;
    private OrderStatusEnum status;
    private String userEmail;
    private String userTel;
    private BigDecimal totalPrice;
    private List<OrderDetailsDTO> orderDetailsDTOList;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderDetailsDTO> getOrderDetailsDTOList() {
        return orderDetailsDTOList;
    }

    public void setOrderDetailsDTOList(List<OrderDetailsDTO> orderDetailsDTOList) {
        this.orderDetailsDTOList = orderDetailsDTOList;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId=" + orderId +
                ", date='" + date + '\'' +
                ", status=" + status +
                ", userEmail='" + userEmail + '\'' +
                ", userTel='" + userTel + '\'' +
                ", totalPrice=" + totalPrice +
                ", orderDetailsDTOList=" + orderDetailsDTOList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderDTO orderDTO = (OrderDTO) o;
        return Objects.equals(orderId, orderDTO.orderId) &&
                Objects.equals(date, orderDTO.date) &&
                status == orderDTO.status &&
                Objects.equals(userEmail, orderDTO.userEmail) &&
                Objects.equals(userTel, orderDTO.userTel) &&
                Objects.equals(totalPrice, orderDTO.totalPrice) &&
                Objects.equals(orderDetailsDTOList, orderDTO.orderDetailsDTOList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, date, status, userEmail, userTel, totalPrice, orderDetailsDTOList);
    }

}
