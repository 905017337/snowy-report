package vip.xiaonuo.report.modular.report.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.anji.plus.gaea.constant.BaseOperationEnum;
import com.anji.plus.gaea.exception.BusinessException;
import com.anji.plus.gaea.exception.BusinessExceptionBuilder;
import com.anji.plus.gaea.utils.GaeaBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.report.core.code.ResponseCode;
import vip.xiaonuo.report.core.enums.ReportTypeEnum;
import vip.xiaonuo.report.modular.dashboard.mapper.entity.ReportDashboard;
import vip.xiaonuo.report.modular.dashboard.service.ReportDashboardService;
import vip.xiaonuo.report.modular.dashboardwidget.mapper.entity.ReportDashboardWidget;
import vip.xiaonuo.report.modular.dashboardwidget.service.ReportDashboardWidgetService;
import vip.xiaonuo.report.modular.report.controller.dto.ReportDto;
import vip.xiaonuo.report.modular.report.controller.param.ReportParam;
import vip.xiaonuo.report.modular.report.mapper.ReportMapper;
import vip.xiaonuo.report.modular.report.mapper.entity.Report;
import vip.xiaonuo.report.modular.report.service.ReportService;
import vip.xiaonuo.report.modular.reportexcel.mapper.entity.ReportExcel;
import vip.xiaonuo.report.modular.reportexcel.service.ReportExcelService;
import vip.xiaonuo.report.modular.reportshare.mapper.entity.ReportShare;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author chenkening
 * @date 2021/3/26 10:35
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Resource
    private ReportMapper reportMapper;
    @Autowired
    private ReportDashboardService reportDashboardService;
    @Autowired
    private ReportDashboardWidgetService reportDashboardWidgetService;
    @Autowired
    private ReportExcelService reportExcelService;


    @Override
    public IPage<Report> pageList(ReportParam param) {
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotEmpty(param.getOrder())) {
            if("ASC".equals(param.getOrder())){
                queryWrapper.lambda().orderByAsc(Report::getUpdateTime);
            }
            if("DESC".equals(param.getOrder())){
                queryWrapper.lambda().orderByDesc(Report::getUpdateTime);
            }
        }
        if(ObjectUtil.isNotEmpty(param.getReportType())) {
            queryWrapper.lambda().eq(Report::getReportType,param.getReportType());
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }
    public void processBatchBeforeOperation(List<Report> entities, BaseOperationEnum operationEnum) throws BusinessException {
        processBatchAfterOperation(entities, operationEnum);
        switch (operationEnum) {
            case DELETE_BATCH:
                entities.forEach(report -> {
                    Long id = report.getId();
                    Report delReport = getById(id);
                    if (null == delReport) {
                        return;
                    }
                    String reportCode = delReport.getReportCode();
                    String reportType = delReport.getReportType();
                    switch (ReportTypeEnum.valueOf(reportType)) {
                        case report_screen:
                            LambdaQueryWrapper<ReportDashboard> reportDashboardLambdaQueryWrapper = Wrappers.lambdaQuery();
                            reportDashboardLambdaQueryWrapper.eq(ReportDashboard::getReportCode, reportCode);
                            reportDashboardService.delete(reportDashboardLambdaQueryWrapper);

                            LambdaQueryWrapper<ReportDashboardWidget> reportDashboardWidgetLambdaQueryWrapper = Wrappers.lambdaQuery();
                            reportDashboardWidgetLambdaQueryWrapper.eq(ReportDashboardWidget::getReportCode, reportCode);
                            reportDashboardWidgetService.delete(reportDashboardWidgetLambdaQueryWrapper);

                            break;
                        case report_excel:
                            LambdaQueryWrapper<ReportExcel> reportExcelLambdaQueryWrapper = Wrappers.lambdaQuery();
                            reportExcelLambdaQueryWrapper.eq(ReportExcel::getReportCode, reportCode);
                            reportExcelService.delete(reportExcelLambdaQueryWrapper);
                            break;
                        default:
                    }
                });
                break;
            default:

        }
    }

    private void processBatchAfterOperation(List<Report> entities, BaseOperationEnum operationEnum) {
//        if (CollectionUtils.isEmpty(entities)) {
//            return;
//        }
//
//        Map<String, Map<String, String>> dictItemMap = entities.stream()
//                .collect(Collectors.groupingBy(item -> item.getLocale() + GaeaConstant.REDIS_SPLIT +item.getDictCode(),
//                        Collectors.toMap(GaeaDictItem::getItemValue, GaeaDictItem::getItemName,(v1, v2)-> v2)));
//
//        switch (operationEnum) {
//            case DELETE_BATCH:
//                //遍历并保持到Redis中
//                dictItemMap.entrySet().stream().forEach(entry -> {
//                    String key = GaeaKeyConstant.DICT_PREFIX  + entry.getKey();
//                    Set<String> hashKeys = entry.getValue().keySet();
//                    cacheHelper.hashBatchDel(key, hashKeys);
//                });
//                break;
//            default:
//        }
    }

    /**
     * 下载次数+1
     *
     * @param reportCode
     */
    @Override
    public void downloadStatistics(String reportCode) {
        final QueryWrapper<Report> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Report::getReportCode,reportCode);
        Report report = getOne(wrapper);
        if (null != report) {
            Long downloadCount = report.getDownloadCount();
            if (null == downloadCount) {
                downloadCount = 0L;
            }else {
                downloadCount++;
            }
            report.setDownloadCount(downloadCount);
            updateById(report);
        }

    }

    @Override
    public void copy(ReportDto dto) {
        if (null == dto.getId()) {
            throw BusinessExceptionBuilder.build(ResponseCode.NOT_NULL, "id");
        }
        if (StringUtils.isBlank(dto.getReportCode())) {
            throw BusinessExceptionBuilder.build(ResponseCode.NOT_NULL, "报表编码");
        }
        Report report = getById(dto.getId());
        String reportCode = report.getReportCode();
        Report copyReport = copyReport(report, dto);
        //复制主表数据
        save(copyReport);
        String copyReportCode = copyReport.getReportCode();
        String reportType = report.getReportType();
        switch (ReportTypeEnum.valueOf(reportType)) {
            case report_screen:
                //查询看板
                ReportDashboard reportDashboard = reportDashboardService.selectOne("report_code", reportCode);
                if (null != reportDashboard) {
                    reportDashboard.setId(null);
                    reportDashboard.setReportCode(copyReportCode);
                    reportDashboardService.insert(reportDashboard);
                }

                //查询组件
                List<ReportDashboardWidget> reportDashboardWidgetList = reportDashboardWidgetService.list("report_code", reportCode);
                if (!CollectionUtils.isEmpty(reportDashboardWidgetList)) {
                    String finalCopyReportCode = copyReportCode;
                    reportDashboardWidgetList.forEach(reportDashboardWidget -> {
                        reportDashboardWidget.setId(null);
                        reportDashboardWidget.setReportCode(finalCopyReportCode);
                    });
                    reportDashboardWidgetService.insertBatch(reportDashboardWidgetList);
                }

                break;
            case report_excel:
                ReportExcel reportExcel = reportExcelService.selectOne("report_code", reportCode);
                if (null != reportExcel) {
                    reportExcel.setId(null);
                    reportExcel.setReportCode(copyReportCode);
                    reportExcelService.insert(reportExcel);
                }

                break;
            default:
        }
    }

    private Report copyReport(Report report, ReportDto dto){
        //复制主表数据
        Report copyReport = new Report();
        GaeaBeanUtils.copyAndFormatter(report, copyReport);
        copyReport.setReportCode(dto.getReportCode());
        copyReport.setReportName(dto.getReportName());
        return copyReport;
    }

}
