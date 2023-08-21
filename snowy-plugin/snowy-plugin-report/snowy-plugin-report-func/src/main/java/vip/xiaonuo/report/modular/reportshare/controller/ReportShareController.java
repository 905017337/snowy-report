
package vip.xiaonuo.report.modular.reportshare.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.xiaonuo.report.core.base.ResponseBean;
import vip.xiaonuo.report.core.config.BaseResponse;
import vip.xiaonuo.report.modular.reportshare.controller.dto.ReportShareDto;
import vip.xiaonuo.report.modular.reportshare.controller.param.ReportShareParam;
import vip.xiaonuo.report.modular.reportshare.mapper.entity.ReportShare;
import vip.xiaonuo.report.modular.reportshare.service.ReportShareService;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raod
 * @desc 报表分享 controller
 * @date 2021-08-18 13:37:26.663
 **/
@RestController
@Api(tags = "报表分享管理")
@RequestMapping("/reportShare")
public class ReportShareController extends BaseResponse {

    @Autowired
    private ReportShareService reportShareService;

    @ApiOperation("获取所有数据集")
    @GetMapping("/pageList")
    public ResponseBean pageList(ReportShareParam param){
        IPage<ReportShare> iPage = reportShareService.pageList(param);
        List<ReportShare> records = iPage.getRecords();
        Page<ReportShare> pageDto = new Page();
        pageDto.setCurrent(iPage.getCurrent()).setRecords(records).setPages(iPage.getPages()).setTotal(iPage.getTotal()).setSize(iPage.getSize());
        return  responseSuccessWithData(pageDto);
    }


    @ApiOperation("明细")
    @GetMapping({"/{id}"})
    public ResponseBean detail(@PathVariable("id") Long id) {
        this.logger.info("{}根据ID查询服务开始，id为：{}", this.getClass().getSimpleName(), id);
        ReportShare result = reportShareService.getDetail(id);
        ReportShareDto dto = new ReportShareDto();
        BeanUtils.copyProperties(result, dto);
        ResponseBean responseBean = this.responseSuccessWithData(dto);
        this.logger.info("{}根据ID查询结束，结果：{}", this.getClass().getSimpleName(), JSON.toJSONString(responseBean));
        return responseBean;
    }

    @ApiOperation("明细")
    @GetMapping({"/detailByCode"})
    public ResponseBean detailByCode(@RequestParam("shareCode") String shareCode) {
        return ResponseBean.builder().data(reportShareService.detailByCode(shareCode)).build();
    }

    @ApiOperation("分享延期")
    @PostMapping({"/shareDelay"})
    public ResponseBean shareDelay(@RequestBody ReportShareDto dto) {
        reportShareService.shareDelay(dto);
        return ResponseBean.builder().build();
    }

    @ApiOperation("批量删除")
    @PostMapping({"/delete/batch"})
    public ResponseBean deleteBatchIds(@RequestBody List<Serializable> ids) {
        boolean deleteCount = reportShareService.removeByIds(ids);
        ResponseBean responseBean = this.responseSuccessWithData(deleteCount);
        this.logger.info("批量删除服务结束，req:{},ret：{}", ids, responseBean);
        return responseBean;
    }

}
