package com.gmail.yauhen2012.service.model;

import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class AddItemDTO {

    @NotEmpty(message = "is required")
    @Size(max = 40, message = "Must be max 40 characters long")
    private String itemName;
    @NotEmpty(message = "is required")
    @Size(max = 16, message = "Must be max 10 characters long")
    private String uniqueNumber;
    @NotNull(message = "is required")
    @Positive(message = "price should be positive")
    private BigDecimal price;
    @Size(max = 200, message = "Must be max 200 characters long")
    private String description;

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
        AddItemDTO that = (AddItemDTO) o;
        return Objects.equals(itemName, that.itemName) &&
                Objects.equals(uniqueNumber, that.uniqueNumber) &&
                Objects.equals(price, that.price) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, uniqueNumber, price, description);
    }

}
