package com.tqwc.feastweb.controller;

import com.tqwc.feastcommon.entity.LoveDiary;
import com.tqwc.feastcommon.utils.Result;
import com.tqwc.feastcommon.utils.StatusCode;
import com.tqwc.feastweb.dto.diary.LoveDiaryCreateDTO;
import com.tqwc.feastweb.dto.diary.LoveDiaryUpdateDTO;
import com.tqwc.feastweb.service.LoveDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 恋爱日记表 前端控制器
 * </p>
 *
 * 说明：
 * 该控制器负责恋爱日记模块的接口，
 * 当前第一阶段主要提供：
 * 1. 创建日记
 * 2. 查询日记列表
 * 3. 查询日记详情
 * 4. 修改日记
 * 5. 删除日记
 * @author Tang
 * @data 2026/4/19 16:56
 */
@RestController
@RequestMapping("/diary")
public class LoveDiaryController {

    /**
     * 注入恋爱日记业务服务
     */
    @Autowired
    private LoveDiaryService loveDiaryService;

    /**
     * 创建恋爱日记
     *
     * 请求方式：
     * POST /diary/create
     *
     * @param dto 创建日记请求参数
     * @return 统一返回结果
     */
    @PostMapping("/create")
    public Result createDiary(@RequestBody LoveDiaryCreateDTO dto) {

        // 调用业务层创建日记
        loveDiaryService.createDiary(dto);

        // 返回成功结果
        return new Result(StatusCode.OK, "日记创建成功");
    }

    /**
     * 查询当前用户所属关系下的日记列表
     *
     * 请求方式：
     * GET /diary/myList?currentUserId=1
     *
     * @param currentUserId 当前登录用户ID
     * @return 统一返回结果，data 中为日记列表
     */
    @GetMapping("/myList")
    public Result getMyDiaryList(@RequestParam Long currentUserId) {

        // 调用业务层查询日记列表
        List<LoveDiary> loveDiaryList = loveDiaryService.getMyDiaryList(currentUserId);

        // 返回成功结果
        return new Result(StatusCode.OK, "查询成功", loveDiaryList);
    }

    /**
     * 查询日记详情
     *
     * 请求方式：
     * GET /diary/detail/{diaryId}?currentUserId=1
     *
     * @param diaryId 日记ID
     * @param currentUserId 当前登录用户ID
     * @return 统一返回结果，data 中为日记详情
     */
    @GetMapping("/detail/{diaryId}")
    public Result getDiaryDetail(@PathVariable Long diaryId,
                                 @RequestParam Long currentUserId) {

        // 调用业务层查询日记详情
        LoveDiary loveDiary = loveDiaryService.getDiaryDetail(diaryId, currentUserId);

        // 返回成功结果
        return new Result(StatusCode.OK, "查询成功", loveDiary);
    }

    /**
     * 修改恋爱日记
     *
     * 请求方式：
     * PUT /diary/update
     *
     * @param dto 修改日记请求参数
     * @return 统一返回结果
     */
    @PutMapping("/update")
    public Result updateDiary(@RequestBody LoveDiaryUpdateDTO dto) {

        // 调用业务层修改日记
        loveDiaryService.updateDiary(dto);

        // 返回成功结果
        return new Result(StatusCode.OK, "日记修改成功");
    }

    /**
     * 删除恋爱日记
     *
     * 请求方式：
     * DELETE /diary/delete/{diaryId}?currentUserId=1
     *
     * @param diaryId 日记ID
     * @param currentUserId 当前登录用户ID
     * @return 统一返回结果
     */
    @DeleteMapping("/delete/{diaryId}")
    public Result deleteDiary(@PathVariable Long diaryId,
                              @RequestParam Long currentUserId) {

        // 调用业务层删除日记
        loveDiaryService.deleteDiary(diaryId, currentUserId);

        // 返回成功结果
        return new Result(StatusCode.OK, "日记删除成功");
    }

}
