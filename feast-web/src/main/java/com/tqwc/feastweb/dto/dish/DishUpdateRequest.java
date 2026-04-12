package com.tqwc.feastweb.dto.dish;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DishUpdateRequest {

    @NotBlank(message = "菜品名称不能为空")
    @Size(max = 100, message = "菜品名称长度不能超过100")
    private String name;

    @Size(max = 50, message = "口味长度不能超过50")
    private String flavor;

    @Size(max = 255, message = "描述长度不能超过255")
    private String description;

    @Size(max = 255, message = "图片地址长度不能超过255")
    private String image;
}
