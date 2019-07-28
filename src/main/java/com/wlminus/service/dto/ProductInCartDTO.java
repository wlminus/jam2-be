package com.wlminus.service.dto;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProductInCartDTO {
    private Long id;

    @NotNull
    @Min(value = 1L)
    @Max(value = 100000000L)
    private Long price;

    @NotNull
    @Min(value = 1L)
    @Max(value = 100000000L)
    private Long amount;
}
