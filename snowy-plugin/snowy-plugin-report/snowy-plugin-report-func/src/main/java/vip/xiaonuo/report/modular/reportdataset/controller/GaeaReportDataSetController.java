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
package vip.xiaonuo.report.modular.reportdataset.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.report.modular.reportdataset.entity.GaeaReportDataSet;
import vip.xiaonuo.report.modular.reportdataset.param.GaeaReportDataSetAddParam;
import vip.xiaonuo.report.modular.reportdataset.param.GaeaReportDataSetEditParam;
import vip.xiaonuo.report.modular.reportdataset.param.GaeaReportDataSetIdParam;
import vip.xiaonuo.report.modular.reportdataset.param.GaeaReportDataSetPageParam;
import vip.xiaonuo.report.modular.reportdataset.service.GaeaReportDataSetService;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * 数据集管理控制器
 *
 * @author czh
 * @date  2023/08/20 16:14
 */
@Api(tags = "数据集管理控制器")
@ApiSupport(author = "SNOWY_TEAM", order = 1)
@RestController
@Validated
public class GaeaReportDataSetController {

    @Resource
    private GaeaReportDataSetService gaeaReportDataSetService;

    /**
     * 获取数据集管理分页
     *
     * @author czh
     * @date  2023/08/20 16:14
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取数据集管理分页")
    @GetMapping("/report/reportdataset/page")
    public CommonResult<Page<GaeaReportDataSet>> page(GaeaReportDataSetPageParam gaeaReportDataSetPageParam) {
        return CommonResult.data(gaeaReportDataSetService.page(gaeaReportDataSetPageParam));
    }

    /**
     * 添加数据集管理
     *
     * @author czh
     * @date  2023/08/20 16:14
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("添加数据集管理")
    @CommonLog("添加数据集管理")
    @PostMapping("/report/reportdataset/add")
    public CommonResult<String> add(@RequestBody @Valid GaeaReportDataSetAddParam gaeaReportDataSetAddParam) {
        gaeaReportDataSetService.add(gaeaReportDataSetAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑数据集管理
     *
     * @author czh
     * @date  2023/08/20 16:14
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("编辑数据集管理")
    @CommonLog("编辑数据集管理")
    @PostMapping("/report/reportdataset/edit")
    public CommonResult<String> edit(@RequestBody @Valid GaeaReportDataSetEditParam gaeaReportDataSetEditParam) {
        gaeaReportDataSetService.edit(gaeaReportDataSetEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除数据集管理
     *
     * @author czh
     * @date  2023/08/20 16:14
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("删除数据集管理")
    @CommonLog("删除数据集管理")
    @PostMapping("/report/reportdataset/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<GaeaReportDataSetIdParam> gaeaReportDataSetIdParamList) {
        gaeaReportDataSetService.delete(gaeaReportDataSetIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取数据集管理详情
     *
     * @author czh
     * @date  2023/08/20 16:14
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取数据集管理详情")
    @GetMapping("/report/reportdataset/detail")
    public CommonResult<GaeaReportDataSet> detail(@Valid GaeaReportDataSetIdParam gaeaReportDataSetIdParam) {
        return CommonResult.data(gaeaReportDataSetService.detail(gaeaReportDataSetIdParam));
    }
}
