package vip.xiaonuo.report.modular.report.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.report.modular.report.controller.dto.ReportDto;
import vip.xiaonuo.report.modular.report.controller.param.ReportParam;
import vip.xiaonuo.report.modular.report.mapper.entity.Report;

/**
 *
 * @author chenkening
 * @date 2021/3/26 10:35
 */
public interface ReportService extends IService<Report> {


    /**
     * 下载次数+1
     * @param reportCode
     */
    void downloadStatistics(String reportCode);

    /**
     * 复制大屏
     * @param dto
     */
    void copy(ReportDto dto);

    IPage<Report> pageList(ReportParam param);
}
