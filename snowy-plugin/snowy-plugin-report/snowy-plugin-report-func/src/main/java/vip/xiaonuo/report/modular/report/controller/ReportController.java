package vip.xiaonuo.report.modular.report.controller;

import com.anji.plus.gaea.holder.UserContentHolder;
import com.anji.plus.gaea.utils.GaeaBeanUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.xiaonuo.report.core.base.ResponseBean;
import vip.xiaonuo.report.core.config.BaseResponse;
import vip.xiaonuo.report.modular.report.controller.dto.ReportDto;
import vip.xiaonuo.report.modular.report.controller.param.ReportParam;
import vip.xiaonuo.report.modular.report.mapper.entity.Report;
import vip.xiaonuo.report.modular.report.service.ReportService;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author chenkening
 * @date 2021/3/26 10:19
 */
@RestController
@Api(tags = "报表数据管理")
@RequestMapping("/report")
public class ReportController extends BaseResponse {

    @Autowired
    private ReportService reportService;


    @ApiOperation("获取所有报表数据管理")
    @GetMapping("/pageList")
    public ResponseBean pageList(ReportParam param){
        IPage<Report> iPage = reportService.pageList(param);
        List<Report> records = iPage.getRecords();
        Page<Report> pageDto = new Page();
        pageDto.setCurrent(iPage.getCurrent()).setRecords(records).setPages(iPage.getPages()).setTotal(iPage.getTotal()).setSize(iPage.getSize());
        return responseSuccessWithData(pageDto);

    }
    @ApiOperation("新增")
    @PostMapping
    public ResponseBean insert(@RequestBody ReportDto dto) {
        ResponseBean responseBean = this.responseSuccess();
        Report entity = new Report();
        BeanUtils.copyProperties(dto, entity);
        reportService.save(entity);
        this.logger.info("新增服务结束，req：{},res:{}", dto, responseBean);
        return responseBean;
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public ResponseBean update(@RequestBody ReportDto dto) {
        String username = UserContentHolder.getContext().getUsername();
        Report entity = new Report();
        BeanUtils.copyProperties(dto, entity);
        reportService.updateById(entity);
        this.logger.info("更新服务结束，user:{},req:{},res:{}", new Object[]{username, dto, entity});
        return this.responseSuccess();
    }

    @ApiOperation("删除")
    @DeleteMapping({"/{id}"})
    public ResponseBean deleteById(@PathVariable("id") Long id) {
        reportService.removeById(id);
        this.logger.info("删除服务结束:{}", id);
        return this.responseSuccess();
    }

    @ApiOperation("详情")
    @GetMapping({"/{id}"})
    public ResponseBean detail(@PathVariable("id") Long id) {
        Report result = reportService.getById(id);
        ReportDto dto = new ReportDto();
        GaeaBeanUtils.copyAndFormatter(result, dto);
        ResponseBean responseBean = this.responseSuccessWithData(dto);
        this.logger.info("根据ID查询结束，req：{},res:{}", id, responseBean);
        return responseBean;
    }
    @ApiOperation("批量删除")
    @PostMapping({"/delete/batch"})
    public ResponseBean deleteBatchIds(@RequestBody List<Serializable> ids) {
        boolean deleteCount = reportService.removeByIds(ids);
        ResponseBean responseBean = this.responseSuccessWithData(deleteCount);
        this.logger.info("批量删除服务结束，req:{},ret：{}", ids, responseBean);
        return responseBean;
    }

    @ApiOperation("复制")
    @PostMapping("/copy")
    public ResponseBean copy(@RequestBody ReportDto dto) {
        reportService.copy(dto);
        return ResponseBean.builder().build();
    }


}
