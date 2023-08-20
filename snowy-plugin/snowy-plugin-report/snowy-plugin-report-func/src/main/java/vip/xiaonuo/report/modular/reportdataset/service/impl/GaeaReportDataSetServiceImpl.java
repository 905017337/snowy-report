/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.report.modular.reportdataset.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.report.modular.reportdataset.entity.GaeaReportDataSet;
import vip.xiaonuo.report.modular.reportdataset.mapper.GaeaReportDataSetMapper;
import vip.xiaonuo.report.modular.reportdataset.param.GaeaReportDataSetAddParam;
import vip.xiaonuo.report.modular.reportdataset.param.GaeaReportDataSetEditParam;
import vip.xiaonuo.report.modular.reportdataset.param.GaeaReportDataSetIdParam;
import vip.xiaonuo.report.modular.reportdataset.param.GaeaReportDataSetPageParam;
import vip.xiaonuo.report.modular.reportdataset.service.GaeaReportDataSetService;

import java.util.List;

/**
 * 数据集管理Service接口实现类
 *
 * @author czh
 * @date  2023/08/20 16:14
 **/
@Service
public class GaeaReportDataSetServiceImpl extends ServiceImpl<GaeaReportDataSetMapper, GaeaReportDataSet> implements GaeaReportDataSetService {

    @Override
    public Page<GaeaReportDataSet> page(GaeaReportDataSetPageParam gaeaReportDataSetPageParam) {
        QueryWrapper<GaeaReportDataSet> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isAllNotEmpty(gaeaReportDataSetPageParam.getSortField(), gaeaReportDataSetPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(gaeaReportDataSetPageParam.getSortOrder());
            queryWrapper.orderBy(true, gaeaReportDataSetPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(gaeaReportDataSetPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(GaeaReportDataSet::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(GaeaReportDataSetAddParam gaeaReportDataSetAddParam) {
        GaeaReportDataSet gaeaReportDataSet = BeanUtil.toBean(gaeaReportDataSetAddParam, GaeaReportDataSet.class);
        this.save(gaeaReportDataSet);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(GaeaReportDataSetEditParam gaeaReportDataSetEditParam) {
        GaeaReportDataSet gaeaReportDataSet = this.queryEntity(gaeaReportDataSetEditParam.getId().toString());
        BeanUtil.copyProperties(gaeaReportDataSetEditParam, gaeaReportDataSet);
        this.updateById(gaeaReportDataSet);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<GaeaReportDataSetIdParam> gaeaReportDataSetIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(gaeaReportDataSetIdParamList, GaeaReportDataSetIdParam::getId));
    }

    @Override
    public GaeaReportDataSet detail(GaeaReportDataSetIdParam gaeaReportDataSetIdParam) {
        return this.queryEntity(gaeaReportDataSetIdParam.getId().toString());
    }

    @Override
    public GaeaReportDataSet queryEntity(String id) {
        GaeaReportDataSet gaeaReportDataSet = this.getById(id);
        if(ObjectUtil.isEmpty(gaeaReportDataSet)) {
            throw new CommonException("数据集管理不存在，id值为：{}", id);
        }
        return gaeaReportDataSet;
    }
}
