package com.tqwc.feastweb.controller;

import com.tqwc.feastcommon.entity.Dish;
import com.tqwc.feastcommon.utils.Result;
import com.tqwc.feastcommon.utils.StatusCode;
import com.tqwc.feastweb.dto.dish.DishCreateRequest;
import com.tqwc.feastweb.dto.dish.DishUpdateRequest;
import com.tqwc.feast.jwt.JwtUserPrincipal;
import com.tqwc.feastweb.service.DishService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    public Result create(@Valid @RequestBody DishCreateRequest request, Authentication authentication) {
        JwtUserPrincipal principal = currentPrincipal(authentication);
        if (Objects.isNull(principal)) {
            return new Result(StatusCode.ACCESSERROR, "未登录");
        }
        Dish dish = dishService.create(request, principal.getUserId());
        return new Result(StatusCode.OK, "创建成功", dish);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Long id,
                         @Valid @RequestBody DishUpdateRequest request,
                         Authentication authentication) {
        JwtUserPrincipal principal = currentPrincipal(authentication);
        if (Objects.isNull(principal)) {
            return new Result(StatusCode.ACCESSERROR, "未登录");
        }
        Dish dish = dishService.update(id, request, principal.getUserId());
        return new Result(StatusCode.OK, "更新成功", dish);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Long id, Authentication authentication) {
        JwtUserPrincipal principal = currentPrincipal(authentication);
        if (Objects.isNull(principal)) {
            return new Result(StatusCode.ACCESSERROR, "未登录");
        }
        dishService.softDelete(id, principal.getUserId());
        return new Result(StatusCode.OK, "删除成功");
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Long id) {
        Dish dish = dishService.getActiveById(id);
        if (Objects.isNull(dish)) {
            return new Result(StatusCode.ERROR, "菜品不存在或已下架");
        }
        return new Result(StatusCode.OK, "查询成功", dish);
    }

    @GetMapping
    public Result list() {
        List<Dish> list = dishService.listActive();
        return new Result(StatusCode.OK, "查询成功", list);
    }

    private static JwtUserPrincipal currentPrincipal(Authentication authentication) {
        if (Objects.isNull(authentication) || !(authentication.getPrincipal() instanceof JwtUserPrincipal principal)) {
            return null;
        }
        return principal;
    }
}
