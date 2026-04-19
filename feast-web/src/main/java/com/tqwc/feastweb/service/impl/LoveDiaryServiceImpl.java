package com.tqwc.feastweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tqwc.feastcommon.entity.LoveDiary;
import com.tqwc.feastcommon.entity.OrderRecord;
import com.tqwc.feastcommon.entity.Relationship;
import com.tqwc.feastweb.dto.diary.LoveDiaryCreateDTO;
import com.tqwc.feastweb.dto.diary.LoveDiaryUpdateDTO;
import com.tqwc.feastweb.mapper.LoveDiaryMapper;
import com.tqwc.feastweb.service.LoveDiaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tqwc.feastweb.service.OrderRecordService;
import com.tqwc.feastweb.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 恋爱日记表 服务实现类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Service
public class LoveDiaryServiceImpl extends ServiceImpl<LoveDiaryMapper, LoveDiary> implements LoveDiaryService {
    /**
     * 注入情侣关系业务服务
     */
    @Autowired
    private RelationshipService relationshipService;

    /**
     * 注入订单业务服务
     */
    @Autowired
    private OrderRecordService orderRecordService;

    /**
     * 创建恋爱日记
     *
     * @param dto 创建日记请求参数
     */
    @Override
    public void createDiary(LoveDiaryCreateDTO dto) {

        // 1. 判断参数是否为空
        if (dto == null) {
            throw new RuntimeException("创建日记参数不能为空");
        }

        // 2. 判断当前用户ID是否为空
        if (dto.getCurrentUserId() == null) {
            throw new RuntimeException("当前用户ID不能为空");
        }

        // 3. 判断日记日期是否为空
        if (dto.getDiaryDate() == null) {
            throw new RuntimeException("日记日期不能为空");
        }

        // 4. 查询当前用户是否存在有效情侣关系
        Relationship relationship = relationshipService.getActiveRelationshipByUserId(dto.getCurrentUserId());
        if (relationship == null) {
            throw new RuntimeException("当前用户尚未绑定情侣关系，无法创建日记");
        }

        // 5. 如果传了订单ID，则校验订单是否存在，且是否属于当前情侣关系
        if (dto.getOrderId() != null) {
            OrderRecord orderRecord = orderRecordService.getById(dto.getOrderId());
            if (orderRecord == null) {
                throw new RuntimeException("关联订单不存在");
            }

            if (!orderRecord.getRelationshipId().equals(relationship.getId())) {
                throw new RuntimeException("该订单不属于当前情侣关系，不能关联");
            }
        }

        // 6. 创建恋爱日记对象
        LoveDiary loveDiary = new LoveDiary();
        loveDiary.setRelationshipId(relationship.getId());
        loveDiary.setOrderId(dto.getOrderId());
        loveDiary.setDiaryDate(dto.getDiaryDate());
        loveDiary.setTitle(dto.getTitle());
        loveDiary.setContent(dto.getContent());
        loveDiary.setImage(dto.getImage());
        loveDiary.setStyleTag(dto.getStyleTag());
        loveDiary.setCreatedBy(dto.getCurrentUserId());
        loveDiary.setCreatedTime(LocalDateTime.now());

        // 7. 保存日记
        this.save(loveDiary);
    }

    /**
     * 查询当前用户所属关系下的日记列表
     *
     * @param currentUserId 当前登录用户ID
     * @return 日记列表
     */
    @Override
    public List<LoveDiary> getMyDiaryList(Long currentUserId) {

        // 1. 判断当前用户ID是否为空
        if (currentUserId == null) {
            throw new RuntimeException("当前用户ID不能为空");
        }

        // 2. 查询当前用户是否存在有效情侣关系
        Relationship relationship = relationshipService.getActiveRelationshipByUserId(currentUserId);
        if (relationship == null) {
            throw new RuntimeException("当前用户尚未绑定情侣关系");
        }

        // 3. 构造查询条件
        LambdaQueryWrapper<LoveDiary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LoveDiary::getRelationshipId, relationship.getId())
                .orderByDesc(LoveDiary::getDiaryDate)
                .orderByDesc(LoveDiary::getCreatedTime);

        // 4. 返回日记列表
        return this.list(wrapper);
    }

    /**
     * 查询日记详情
     *
     * @param diaryId 日记ID
     * @param currentUserId 当前登录用户ID
     * @return 日记详情
     */
    @Override
    public LoveDiary getDiaryDetail(Long diaryId, Long currentUserId) {

        // 1. 判断日记ID是否为空
        if (diaryId == null) {
            throw new RuntimeException("日记ID不能为空");
        }

        // 2. 判断当前用户ID是否为空
        if (currentUserId == null) {
            throw new RuntimeException("当前用户ID不能为空");
        }

        // 3. 查询当前用户是否存在有效情侣关系
        Relationship relationship = relationshipService.getActiveRelationshipByUserId(currentUserId);
        if (relationship == null) {
            throw new RuntimeException("当前用户尚未绑定情侣关系");
        }

        // 4. 查询日记是否存在
        LoveDiary loveDiary = this.getById(diaryId);
        if (loveDiary == null) {
            throw new RuntimeException("日记不存在");
        }

        // 5. 判断该日记是否属于当前用户所在情侣关系
        if (!loveDiary.getRelationshipId().equals(relationship.getId())) {
            throw new RuntimeException("无权查看该日记");
        }

        // 6. 返回日记详情
        return loveDiary;
    }

    /**
     * 修改恋爱日记
     *
     * @param dto 修改日记请求参数
     */
    @Override
    public void updateDiary(LoveDiaryUpdateDTO dto) {
        // 1. 判断参数是否为空
        if (dto == null) {
            throw new RuntimeException("修改日记参数不能为空");
        }

        // 2. 判断日记ID是否为空
        if (dto.getId() == null) {
            throw new RuntimeException("日记ID不能为空");
        }

        // 3. 判断当前用户ID是否为空
        if (dto.getCurrentUserId() == null) {
            throw new RuntimeException("当前用户ID不能为空");
        }

        // 4. 判断日记日期是否为空
        if (dto.getDiaryDate() == null) {
            throw new RuntimeException("日记日期不能为空");
        }

        // 5. 查询日记是否存在
        LoveDiary loveDiary = this.getById(dto.getId());
        if (loveDiary == null) {
            throw new RuntimeException("日记不存在");
        }

        // 6. 判断当前用户是否为创建人
        if (!loveDiary.getCreatedBy().equals(dto.getCurrentUserId())) {
            throw new RuntimeException("无权修改该日记");
        }

        // 7. 查询当前用户是否存在有效情侣关系
        Relationship relationship = relationshipService.getActiveRelationshipByUserId(dto.getCurrentUserId());
        if (relationship == null) {
            throw new RuntimeException("当前用户尚未绑定情侣关系");
        }

        // 8. 如果传了订单ID，则校验订单是否存在，且是否属于当前情侣关系
        if (dto.getOrderId() != null) {
            OrderRecord orderRecord = orderRecordService.getById(dto.getOrderId());
            if (orderRecord == null) {
                throw new RuntimeException("关联订单不存在");
            }

            if (!orderRecord.getRelationshipId().equals(relationship.getId())) {
                throw new RuntimeException("该订单不属于当前情侣关系，不能关联");
            }
        }

        // 9. 更新可修改字段
        loveDiary.setOrderId(dto.getOrderId());
        loveDiary.setDiaryDate(dto.getDiaryDate());
        loveDiary.setTitle(dto.getTitle());
        loveDiary.setContent(dto.getContent());
        loveDiary.setImage(dto.getImage());
        loveDiary.setStyleTag(dto.getStyleTag());
        loveDiary.setUpdatedTime(LocalDateTime.now());

        // 10. 更新数据库
        this.updateById(loveDiary);
    }

    /**
     * 删除恋爱日记
     *
     * @param diaryId 日记ID
     * @param currentUserId 当前登录用户ID
     */
    @Override
    public void deleteDiary(Long diaryId, Long currentUserId) {
        // 1. 判断日记ID是否为空
        if (diaryId == null) {
            throw new RuntimeException("日记ID不能为空");
        }

        // 2. 判断当前用户ID是否为空
        if (currentUserId == null) {
            throw new RuntimeException("当前用户ID不能为空");
        }

        // 3. 查询日记是否存在
        LoveDiary loveDiary = this.getById(diaryId);
        if (loveDiary == null) {
            throw new RuntimeException("日记不存在");
        }

        // 4. 判断当前用户是否为创建人
        if (!loveDiary.getCreatedBy().equals(currentUserId)) {
            throw new RuntimeException("无权删除该日记");
        }

        // 5. 删除日记
        this.removeById(diaryId);
    }


}
