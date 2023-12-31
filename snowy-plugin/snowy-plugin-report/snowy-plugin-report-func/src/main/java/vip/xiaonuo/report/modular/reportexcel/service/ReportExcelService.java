package vip.xiaonuo.report.modular.reportexcel.service;


import com.anji.plus.gaea.curd.service.GaeaBaseService;
import vip.xiaonuo.report.modular.reportexcel.controller.dto.ReportExcelDto;
import vip.xiaonuo.report.modular.reportexcel.controller.param.ReportExcelParam;
import vip.xiaonuo.report.modular.reportexcel.mapper.entity.ReportExcel;

/**
 * TODO
 *
 * @author chenkening
 * @date 2021/4/13 15:14
 */
public interface ReportExcelService extends GaeaBaseService<ReportExcelParam, ReportExcel> {

    /**
     * 根据报表编码查询详情
     *
     * @param reportCode
     * @return
     */
    ReportExcelDto detailByReportCode(String reportCode);

    /**
     * 报表预览
     *
     * @param reportExcelDto
     * @return
     */
    ReportExcelDto preview(ReportExcelDto reportExcelDto);


    /**
     * 导出为excel
     *
     * @param reportExcelDto
     * @return
     */
    Boolean exportExcel(ReportExcelDto reportExcelDto);

//    Boolean exportPdf(ReportExcelDto reportExcelDto);
}
