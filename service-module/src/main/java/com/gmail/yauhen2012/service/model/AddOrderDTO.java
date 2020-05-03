package com.gmail.yauhen2012.service.model;

import java.util.List;
import java.util.Objects;

public class AddOrderDTO {

    private Long userId;
    private List<Long> itemsId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getItemsId() {
        return itemsId;
    }

    public void setItemsId(List<Long> itemsId) {
        this.itemsId = itemsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddOrderDTO that = (AddOrderDTO) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(itemsId, that.itemsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, itemsId);
    }

}
