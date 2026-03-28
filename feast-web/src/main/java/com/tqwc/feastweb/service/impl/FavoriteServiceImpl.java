package com.tqwc.feastweb.service.impl;

import com.tqwc.feastcommon.entity.Favorite;
import com.tqwc.feastweb.mapper.FavoriteMapper;
import com.tqwc.feastweb.service.FavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author Tang
 * @since 2026-03-28
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

}
