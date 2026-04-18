package com.tqwc.feastweb.config;

import com.tqwc.feastcommon.utils.Result;
import com.tqwc.feastcommon.utils.StatusCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 作用：
 * 1. 统一捕获项目运行过程中抛出的异常
 * 2. 统一返回 Result 格式的数据
 * 3. 避免前端拿到 Spring 默认报错页面或复杂报错信息
 * @author Tang
 * @data 2026/4/16 18:33
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 @RequestBody + @Valid 触发的参数校验异常
     *
     * 例如：
     * 前端传入 DTO 时，某个字段为空、长度超限等。
     *
     * @param e 参数校验异常
     * @return 统一失败结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValid(MethodArgumentNotValidException e) {

        // 获取第一个字段错误信息
        FieldError fieldError = e.getBindingResult().getFieldError();

        // 如果没有字段错误，则返回默认提示
        String message = fieldError == null ? "参数校验失败" : fieldError.getDefaultMessage();

        return new Result(StatusCode.ERROR, message);
    }

    /**
     * 处理方法参数约束校验异常
     *
     * 例如：
     * @RequestParam、@PathVariable 上使用校验注解时触发。
     *
     * @param e 约束校验异常
     * @return 统一失败结果
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolation(ConstraintViolationException e) {
        return new Result(StatusCode.ERROR, e.getMessage());
    }

    /**
     * 处理非法参数异常
     *
     * 例如：
     * 手动抛出 IllegalArgumentException("参数不合法")
     *
     * @param e 非法参数异常
     * @return 统一失败结果
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result handleIllegalArgument(IllegalArgumentException e) {
        return new Result(StatusCode.ERROR, e.getMessage());
    }

    /**
     * 处理业务运行时异常
     *
     * 说明：
     * 你在 service 层里大量手动抛出的 RuntimeException
     * 都会被这里统一捕获。
     *
     * @param e 运行时异常
     * @return 统一失败结果
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        return new Result(StatusCode.ERROR, e.getMessage());
    }

    /**
     * 处理所有其他未知异常
     *
     * 说明：
     * 作为兜底异常处理，防止系统异常直接暴露给前端。
     *
     * @param e 异常对象
     * @return 统一失败结果
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return new Result(StatusCode.ERROR, "系统异常，请稍后再试");
    }
}
