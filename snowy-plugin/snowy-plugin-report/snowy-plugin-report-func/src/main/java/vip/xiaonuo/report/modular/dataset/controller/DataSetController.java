
package vip.xiaonuo.report.modular.dataset.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.xiaonuo.report.core.base.ResponseBean;
import vip.xiaonuo.report.core.config.BaseResponse;
import vip.xiaonuo.report.modular.dataset.controller.dto.DataSetDto;
import vip.xiaonuo.report.modular.dataset.controller.param.DataSetParam;
import vip.xiaonuo.report.modular.dataset.controller.param.DataSetTestTransformParam;
import vip.xiaonuo.report.modular.dataset.mapper.entity.DataSet;
import vip.xiaonuo.report.modular.dataset.service.DataSetService;

import java.io.Serializable;
import java.util.List;

/**
* @desc 数据集 controller
* @website https://gitee.com/anji-plus/gaea
* @author Raod
* @date 2021-03-18 12:11:31.150755900
**/
@RestController
@Api(tags = "数据集管理")
@RequestMapping("/dataSet")
public class DataSetController extends BaseResponse {

    @Autowired
    private DataSetService dataSetService;

    @ApiOperation("获取所有数据集")
    @GetMapping("/pageList")
    public ResponseBean pageList(DataSetParam param){
        IPage<DataSet> iPage = dataSetService.pageList(param);
        List<DataSet> records = iPage.getRecords();
        Page<DataSet> pageDto = new Page();
        pageDto.setCurrent(iPage.getCurrent()).setRecords(records).setPages(iPage.getPages()).setTotal(iPage.getTotal()).setSize(iPage.getSize());
        return  responseSuccessWithData(pageDto);
    }

    @ApiOperation("明细")
    @GetMapping("/detailBysetId/{id}")
    public ResponseBean detailBysetId(@PathVariable("id") Long id) {
        this.logger.info("{}根据ID查询服务开始，id为：{}", this.getClass().getSimpleName(), id);
        ResponseBean responseBean = this.responseSuccessWithData(dataSetService.detailSet(id));

        this.logger.info("{}根据ID查询结束，结果：{}", this.getClass().getSimpleName(), JSON.toJSONString(responseBean) );
        return responseBean;
    }

    @ApiOperation("明细")
    @GetMapping({"/detailBysetCode/{setCode}"})
    public ResponseBean detailBysetCode(@PathVariable("setCode") String setCode) {
        this.logger.info("{}根据setCode查询服务开始，setCode为：{}", this.getClass().getSimpleName(), setCode);
        ResponseBean responseBean = this.responseSuccessWithData(dataSetService.detailSet(setCode));
        this.logger.info("{}根据setCode查询结束，结果：{}", this.getClass().getSimpleName(), JSON.toJSONString(responseBean));
        return responseBean;
    }

    @ApiOperation("新增")
    @PostMapping
    public ResponseBean insert(@RequestBody DataSetDto dto) {
        this.logger.info("{}新增服务开始，参数：{}", this.getClass().getSimpleName(), JSON.toJSONString(dto));
        DataSetDto dataSetDto = dataSetService.insertSet(dto);
        this.logger.info("{}新增服务结束，结果：{}", this.getClass().getSimpleName(),JSON.toJSONString(dataSetDto));
        return ResponseBean.builder().data(dataSetDto).build();
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseBean update(@RequestBody DataSetDto dto) {
        this.logger.info("{}更新服务开始,更新人：{}，参数：{}", this.getClass().getSimpleName(),JSON.toJSONString(dto));
        ResponseBean responseBean = this.responseSuccess();
        dataSetService.updateSet(dto);
        this.logger.info("{}更新服务结束，结果：{}", this.getClass().getSimpleName(), JSON.toJSONString(responseBean));
        return this.responseSuccess();
    }

    @ApiOperation("删除")
    @DeleteMapping({"/{id}"})
    public ResponseBean deleteById(@PathVariable("id") Long id) {
        this.logger.info("{}删除服务开始，参数ID：{}", this.getClass().getSimpleName(), id);
        dataSetService.deleteSet(id);
        this.logger.info("{}删除服务结束", this.getClass().getSimpleName());
        return this.responseSuccess();
    }

    /**
     * 测试 数据转换是否正确
     * @param param
     * @return
     */
    @ApiOperation("明细")
    @PostMapping("/testTransform")
    public ResponseBean testTransform(@Validated @RequestBody DataSetTestTransformParam param) {
        DataSetDto dto = new DataSetDto();
        BeanUtils.copyProperties(param, dto);
        return responseSuccessWithData(dataSetService.testTransform(dto));
    }

    /**
     * 获取所有数据集
     * @return
     */
    @ApiOperation("获取所有数据集")
    @GetMapping("/queryAllDataSet")
    public ResponseBean queryAllDataSet() {
        return responseSuccessWithData(dataSetService.queryAllDataSet());
    }

    @ApiOperation("批量删除")
    @PostMapping({"/delete/batch"})
    public ResponseBean deleteBatchIds(@RequestBody List<Serializable> ids) {
        boolean deleteCount = dataSetService.removeByIds(ids);
        ResponseBean responseBean = this.responseSuccessWithData(deleteCount);
        this.logger.info("批量删除服务结束，req:{},ret：{}", ids, responseBean);
        return responseBean;
    }
}
