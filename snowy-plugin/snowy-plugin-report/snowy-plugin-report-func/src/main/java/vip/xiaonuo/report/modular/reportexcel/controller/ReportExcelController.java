package vip.xiaonuo.report.modular.reportexcel.controller;

import com.anji.plus.gaea.annotation.Permission;
import com.anji.plus.gaea.annotation.log.GaeaAuditLog;
import com.anji.plus.gaea.bean.ResponseBean;
import com.anji.plus.gaea.code.ResponseCode;
import com.anji.plus.gaea.curd.controller.GaeaBaseController;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.xiaonuo.report.modular.reportexcel.controller.dto.ReportExcelDto;
import vip.xiaonuo.report.modular.reportexcel.controller.param.ReportExcelParam;
import vip.xiaonuo.report.modular.reportexcel.mapper.entity.ReportExcel;
import vip.xiaonuo.report.modular.reportexcel.service.ReportExcelService;
import vip.xiaonuo.report.modular.reportshare.controller.dto.ReportShareDto;
import vip.xiaonuo.report.modular.reportshare.service.ReportShareService;

/**
 * @author chenkening
 * @date 2021/4/13 15:12
 */
@RestController
@Api(tags = "报表表格管理")
@Permission(code = "excelManage", name = "报表管理")
@RequestMapping("/reportExcel")
public class ReportExcelController extends GaeaBaseController<ReportExcelParam, ReportExcel, ReportExcelDto> {

    @Autowired
    private ReportExcelService reportExcelService;

    @Autowired
    private ReportShareService reportShareService;

    @Override
    public GaeaBaseService<ReportExcelParam, ReportExcel> getService() {
        return reportExcelService;
    }

    @Override
    public ReportExcel getEntity() {
        return new ReportExcel();
    }

    @Override
    public ReportExcelDto getDTO() {
        return new ReportExcelDto();
    }

    @ApiOperation("详情")
    @GetMapping("/detailByReportCode/{reportCode}")
    @Permission(code = "query", name = "详情")
    @GaeaAuditLog(pageTitle = "详情")
    public ResponseBean detailByReportCode(@PathVariable String reportCode) {
        ReportExcelDto reportExcelDto = reportExcelService.detailByReportCode(reportCode);
        return ResponseBean.builder().data(reportExcelDto).build();
    }

    @ApiOperation("预览")
    @PostMapping("/preview")
    @Permission(code = "view", name = "预览")
    @GaeaAuditLog(pageTitle = "预览")
    public ResponseBean preview(@RequestBody ReportExcelDto reportExcelDto) {
        ReportExcelDto result = reportExcelService.preview(reportExcelDto);
        return ResponseBean.builder().data(result).build();
    }


    @ApiOperation("报表导出")
    @PostMapping("/exportExcel")
    @Permission(code = "export", name = "导出")
    @GaeaAuditLog(pageTitle = "报表导出")
    public ResponseBean exportExcel(@RequestBody ReportExcelDto reportExcelDto) {

        return ResponseBean.builder().code(ResponseCode.SUCCESS_CODE)
                .data(reportExcelService.exportExcel(reportExcelDto))
                .message("导出成功，请稍后在文件管理中查看").build();
    }

//    @PostMapping("/exportPdf")
//    public ResponseBean exportPdf(@RequestBody ReportExcelDto reportExcelDto) {
//        reportExcelService.exportPdf(reportExcelDto);
//        return ResponseBean.builder().code(ResponseCode.SUCCESS_CODE)
//                .build();
//    }

    @ApiOperation("分享报表")
    @PostMapping("/share")
    @GaeaAuditLog(pageTitle = "excel分享")
    @Permission(code = "share", name = "分享报表")
    public ResponseBean share(@Validated @RequestBody ReportShareDto dto) {
        return ResponseBean.builder().data(reportShareService.insertShare(dto)).build();
    }
}
