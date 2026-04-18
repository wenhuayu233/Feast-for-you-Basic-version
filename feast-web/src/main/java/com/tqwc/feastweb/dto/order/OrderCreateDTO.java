package com.tqwc.feastweb.dto.order;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Tang
 * @data 2026/4/14 15:43
 */
public class OrderCreateDTO implements Serializable {
    /**
     * 当前登录用户ID
     * 说明：测试阶段可由前端传入，后续可改为从登录态获取
     */
    private Long currentUserId;

    /**
     * 订单日期 / 做饭日期
     */
    private LocalDate orderDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 图片地址
     */
    private String image;

    /**
     * 菜品明细列表
     */
    private List<OrderItemAddDTO> itemList;

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<OrderItemAddDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItemAddDTO> itemList) {
        this.itemList = itemList;
    }

}
