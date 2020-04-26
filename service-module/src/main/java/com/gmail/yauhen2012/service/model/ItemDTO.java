package com.gmail.yauhen2012.service.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemDTO {

    private Long itemId;
    private String itemName;
    private String uniqueNumber;
    private BigDecimal price;
    private String description;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemDTO itemDTO = (ItemDTO) o;
        return Objects.equals(itemId, itemDTO.itemId) &&
                Objects.equals(itemName, itemDTO.itemName) &&
                Objects.equals(uniqueNumber, itemDTO.uniqueNumber) &&
                Objects.equals(price, itemDTO.price) &&
                Objects.equals(description, itemDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, itemName, uniqueNumber, price, description);
    }

}
