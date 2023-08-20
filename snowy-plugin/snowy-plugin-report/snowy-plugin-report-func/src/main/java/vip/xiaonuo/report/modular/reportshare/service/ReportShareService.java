
package vip.xiaonuo.report.modular.reportshare.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.report.modular.reportshare.controller.dto.ReportShareDto;
import vip.xiaonuo.report.modular.reportshare.controller.param.ReportShareParam;
import vip.xiaonuo.report.modular.reportshare.mapper.entity.ReportShare;

/**
* @desc ReportShare 报表分享服务接口
* @author Raod
* @date 2021-08-18 13:37:26.663
**/
public interface ReportShareService extends IService<ReportShare>   {

    /***
     * 查询详情
     *
     * @param id
     * @return
     */
    ReportShare getDetail(Long id);

    ReportShareDto insertShare(ReportShareDto dto);

    ReportShare detailByCode(String shareCode);

    /**
     * 延期过期时间
     * @param dto
     */
    void shareDelay(ReportShareDto dto);

    IPage<ReportShare> pageList(ReportShareParam param);
}
