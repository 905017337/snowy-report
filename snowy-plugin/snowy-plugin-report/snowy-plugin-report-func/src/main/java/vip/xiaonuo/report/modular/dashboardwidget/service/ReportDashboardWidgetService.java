
package vip.xiaonuo.report.modular.dashboardwidget.service;


import com.anji.plus.gaea.curd.service.GaeaBaseService;
import vip.xiaonuo.report.modular.dashboardwidget.controller.param.ReportDashboardWidgetParam;
import vip.xiaonuo.report.modular.dashboardwidget.mapper.entity.ReportDashboardWidget;

/**
* @desc ReportDashboardWidget 大屏看板数据渲染服务接口
* @author Raod
* @date 2021-04-12 15:12:43.724
**/
public interface ReportDashboardWidgetService extends GaeaBaseService<ReportDashboardWidgetParam, ReportDashboardWidget> {

    /***
     * 查询详情
     *
     * @param id
     */
    ReportDashboardWidget getDetail(Long id);
}
