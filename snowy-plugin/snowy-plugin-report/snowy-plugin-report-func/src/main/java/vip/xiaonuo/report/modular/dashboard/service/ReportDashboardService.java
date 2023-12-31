
package vip.xiaonuo.report.modular.dashboard.service;

import com.anji.plus.gaea.curd.service.GaeaBaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.report.modular.dashboard.controller.dto.ChartDto;
import vip.xiaonuo.report.modular.dashboard.controller.dto.ReportDashboardObjectDto;
import vip.xiaonuo.report.modular.dashboard.controller.param.ReportDashboardParam;
import vip.xiaonuo.report.modular.dashboard.mapper.entity.ReportDashboard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @desc ReportDashboard 大屏设计服务接口
* @author Raod
* @date 2021-04-12 14:52:21.761
**/
public interface ReportDashboardService extends GaeaBaseService<ReportDashboardParam, ReportDashboard> {

    /***
     * 查询详情
     *
     * @param reportCode
     */
    ReportDashboardObjectDto getDetail(String reportCode);

    /***
     * 保存大屏设计
     *
     * @param dto
     */
    void insertDashboard(ReportDashboardObjectDto dto);


    /**
     * 获取单个图表数据
     * @param dto
     * @return
     */
    Object getChartData(ChartDto dto);


    /**
     * 导出大屏，zip文件
     * @param request
     * @param response
     * @param reportCode
     * @return
     */
    ResponseEntity<byte[]> exportDashboard(HttpServletRequest request, HttpServletResponse response, String reportCode, Integer showDataSet);

    /**
     * 导入大屏zip
     * @param file
     * @param reportCode
     * @return
     */
    void importDashboard(MultipartFile file, String reportCode);
}
