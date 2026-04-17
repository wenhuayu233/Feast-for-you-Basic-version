package com.tqwc.feastweb.controller;

import com.tqwc.feastcommon.entity.Dish;
import com.tqwc.feastcommon.utils.Result;
import com.tqwc.feastcommon.utils.StatusCode;
import com.tqwc.feastweb.dto.dish.DishCreateRequest;
import com.tqwc.feastweb.dto.dish.DishUpdateRequest;
import com.tqwc.feast.jwt.JwtUserPrincipal;
import com.tqwc.feastweb.service.DishService;
import com.tqwc.feastweb.utils.MinioUtil;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishService dishService;
    private final MinioUtil minioUtil;

    public DishController(DishService dishService, MinioUtil minioUtil) {
        this.dishService = dishService;
        this.minioUtil = minioUtil;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result create(@Valid @ModelAttribute DishCreateRequest request,
                         @RequestPart(value = "file", required = false) MultipartFile file,
                         Authentication authentication) {
        JwtUserPrincipal principal = currentPrincipal(authentication);
        if (Objects.isNull(principal)) {
            return new Result(StatusCode.ACCESSERROR, "未登录");
        }
        Result uploadError = uploadImageIfPresent(request, file);
        if (Objects.nonNull(uploadError)) {
            return uploadError;
        }
        Dish dish = dishService.create(request, principal.getUserId());
        return new Result(StatusCode.OK, "创建成功", dish);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute DishUpdateRequest request,
                         @RequestPart(value = "file", required = false) MultipartFile file,
                         Authentication authentication) {
        JwtUserPrincipal principal = currentPrincipal(authentication);
        if (Objects.isNull(principal)) {
            return new Result(StatusCode.ACCESSERROR, "未登录");
        }
        Result uploadError = uploadImageIfPresent(request, file);
        if (Objects.nonNull(uploadError)) {
            return uploadError;
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

    private Result uploadImageIfPresent(DishCreateRequest request, MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            return null;
        }
        try {
            String imageUrl = minioUtil.uploadFile(file);
            request.setImage(imageUrl);
            return null;
        } catch (Exception e) {
            return new Result(StatusCode.ERROR, "上传图片失败: " + e.getMessage());
        }
    }

    private Result uploadImageIfPresent(DishUpdateRequest request, MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            return null;
        }
        try {
            String imageUrl = minioUtil.uploadFile(file);
            request.setImage(imageUrl);
            return null;
        } catch (Exception e) {
            return new Result(StatusCode.ERROR, "上传图片失败: " + e.getMessage());
        }
    }
}
