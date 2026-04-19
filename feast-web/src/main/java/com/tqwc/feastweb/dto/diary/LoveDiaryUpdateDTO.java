package com.tqwc.feastweb.dto.diary;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 修改恋爱日记请求DTO
 *
 * @author Tang
 * @data 2026/4/19 16:18
 */
public class LoveDiaryUpdateDTO implements Serializable {
    /**
     * 日记ID
     */
    private Long id;

    /**
     * 当前登录用户ID
     * 说明：测试阶段可由前端传入，后续可改为从登录态获取
     */
    private Long currentUserId;

    /**
     * 关联订单ID
     * 说明：可以为空，为空表示普通日记；
     * 不为空表示该日记关联某一张订单
     */
    private Long orderId;

    /**
     * 日记日期
     */
    private LocalDate diaryDate;

    /**
     * 日记标题
     */
    private String title;

    /**
     * 日记内容
     */
    private String content;

    /**
     * 图片地址
     */
    private String image;

    /**
     * 样式标签，如爱心、餐具
     */
    private String styleTag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getDiaryDate() {
        return diaryDate;
    }

    public void setDiaryDate(LocalDate diaryDate) {
        this.diaryDate = diaryDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStyleTag() {
        return styleTag;
    }

    public void setStyleTag(String styleTag) {
        this.styleTag = styleTag;
    }
}
