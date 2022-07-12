package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datum.platform.mapper.domain.Publicity;
import com.datum.platform.mapper.PublicityMapper;
import com.datum.platform.manager.PublicityManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 公示信息 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
@Service
public class PublicityManagerImpl extends ServiceImpl<PublicityMapper, Publicity> implements PublicityManager {

    @Override
    public List<Publicity> listNeedSync() {
        LambdaQueryWrapper<Publicity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.isNull(Publicity::getImageUrl);
        return list(queryWrapper);
    }
}
