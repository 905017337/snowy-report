
package vip.xiaonuo.report.modular.datasource.controller;

import com.anji.plus.gaea.annotation.Permission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.xiaonuo.report.core.base.ResponseBean;
import vip.xiaonuo.report.core.config.BaseResponse;
import vip.xiaonuo.report.modular.datasource.controller.param.ConnectionParam;
import vip.xiaonuo.report.modular.datasource.controller.param.DataSourceParam;
import vip.xiaonuo.report.modular.datasource.mapper.entity.DataSource;
import vip.xiaonuo.report.modular.datasource.service.DataSourceService;

import java.io.Serializable;
import java.util.List;

/**
* @desc 数据源 controller
* @website https://gitee.com/anji-plus/gaea
* @author Raod
* @date 2021-03-18 12:09:57.728203200
**/
@RestController
@Api(tags = "数据源管理")
@Permission(code = "datasourceManage", name = "数据源管理")
@RequestMapping("/dataSource")
public class DataSourceController  extends BaseResponse {

    @Autowired
    private DataSourceService dataSourceService;




    @ApiOperation("获取所有数据源")
    @GetMapping("/pageLists")
    public ResponseBean pageList(DataSourceParam param){
        IPage<DataSource> iPage = dataSourceService.pageList(param);
        List<DataSource> records = iPage.getRecords();
        Page<DataSource> pageDto = new Page();
        pageDto.setCurrent(iPage.getCurrent()).setRecords(records).setPages(iPage.getPages()).setTotal(iPage.getTotal()).setSize(iPage.getSize());
        return  responseSuccessWithData(pageDto);
    }

    /**
     * 获取所有数据源
     * @return
     */
    @ApiOperation("获取所有数据源")
    @GetMapping("/queryAllDataSource")
    public ResponseBean queryAllDataSource() {
        return responseSuccessWithData(dataSourceService.queryAllDataSource());
    }

    /**
     * 测试 连接
     * @param connectionParam
     * @return
     */
    @ApiOperation("测试数据源")
    @Permission( code = "query", name = "测试数据源")
    @PostMapping("/testConnection")
    public ResponseBean testConnection(@Validated @RequestBody ConnectionParam connectionParam) {
        return responseSuccessWithData(dataSourceService.testConnection(connectionParam));
    }
    @ApiOperation("批量删除")
    @PostMapping({"/delete/batch"})
    public ResponseBean deleteBatchIds(@RequestBody List<Serializable> ids) {
        boolean deleteCount = dataSourceService.removeByIds(ids);
        ResponseBean responseBean = this.responseSuccessWithData(deleteCount);
        this.logger.info("批量删除服务结束，req:{},ret：{}", ids, responseBean);
        return responseBean;
    }
}
