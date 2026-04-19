package com.tqwc.feastweb.service;

import com.tqwc.feastcommon.entity.LoveDiary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tqwc.feastweb.dto.diary.LoveDiaryCreateDTO;
import com.tqwc.feastweb.dto.diary.LoveDiaryUpdateDTO;

import java.util.List;

/**
 * <p>
 * 恋爱日记表 服务类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
public interface LoveDiaryService extends IService<LoveDiary> {
    /**
     * 创建恋爱日记
     *
     * @param dto 创建日记请求参数
     */
    void createDiary(LoveDiaryCreateDTO dto);

    /**
     * 查询当前用户所属关系下的日记列表
     *
     * @param currentUserId 当前登录用户ID
     * @return 日记列表
     */
    List<LoveDiary> getMyDiaryList(Long currentUserId);

    /**
     * 查询日记详情
     *
     * @param diaryId 日记ID
     * @param currentUserId 当前登录用户ID
     * @return 日记详情
     */
    LoveDiary getDiaryDetail(Long diaryId, Long currentUserId);

    /**
     * 修改恋爱日记
     *
     * @param dto 修改日记请求参数
     */
    void updateDiary(LoveDiaryUpdateDTO dto);

    /**
     * 删除恋爱日记
     *
     * @param diaryId 日记ID
     * @param currentUserId 当前登录用户ID
     */
    void deleteDiary(Long diaryId, Long currentUserId);
}
