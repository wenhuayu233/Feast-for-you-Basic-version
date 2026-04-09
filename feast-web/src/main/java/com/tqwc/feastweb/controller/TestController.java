package com.tqwc.feastweb.controller;

import com.tqwc.feastcommon.entity.Dish;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/admin/test")
    public String test(@RequestBody Dish dish) {
        return dish.getName();
    }
}
