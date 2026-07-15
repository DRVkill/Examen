package com.project.order_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderRequest(
        @NotNull Integer userId,
        @NotEmpty List<ItemOrderRequest> items
) {}

