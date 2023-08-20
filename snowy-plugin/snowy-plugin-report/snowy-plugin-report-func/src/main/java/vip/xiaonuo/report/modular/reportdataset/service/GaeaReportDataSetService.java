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
package vip.xiaonuo.report.modular.reportdataset.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.report.modular.reportdataset.entity.GaeaReportDataSet;
import vip.xiaonuo.report.modular.reportdataset.param.GaeaReportDataSetAddParam;
import vip.xiaonuo.report.modular.reportdataset.param.GaeaReportDataSetEditParam;
import vip.xiaonuo.report.modular.reportdataset.param.GaeaReportDataSetIdParam;
import vip.xiaonuo.report.modular.reportdataset.param.GaeaReportDataSetPageParam;

import java.util.List;

/**
 * 数据集管理Service接口
 *
 * @author czh
 * @date  2023/08/20 16:14
 **/
public interface GaeaReportDataSetService extends IService<GaeaReportDataSet> {

    /**
     * 获取数据集管理分页
     *
     * @author czh
     * @date  2023/08/20 16:14
     */
    Page<GaeaReportDataSet> page(GaeaReportDataSetPageParam gaeaReportDataSetPageParam);

    /**
     * 添加数据集管理
     *
     * @author czh
     * @date  2023/08/20 16:14
     */
    void add(GaeaReportDataSetAddParam gaeaReportDataSetAddParam);

    /**
     * 编辑数据集管理
     *
     * @author czh
     * @date  2023/08/20 16:14
     */
    void edit(GaeaReportDataSetEditParam gaeaReportDataSetEditParam);

    /**
     * 删除数据集管理
     *
     * @author czh
     * @date  2023/08/20 16:14
     */
    void delete(List<GaeaReportDataSetIdParam> gaeaReportDataSetIdParamList);

    /**
     * 获取数据集管理详情
     *
     * @author czh
     * @date  2023/08/20 16:14
     */
    GaeaReportDataSet detail(GaeaReportDataSetIdParam gaeaReportDataSetIdParam);

    /**
     * 获取数据集管理详情
     *
     * @author czh
     * @date  2023/08/20 16:14
     **/
    GaeaReportDataSet queryEntity(String id);
}
