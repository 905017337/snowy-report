
package vip.xiaonuo.report.modular.datasetparam.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.report.core.base.ResponseBean;
import vip.xiaonuo.report.core.config.BaseResponse;
import vip.xiaonuo.report.modular.datasetparam.controller.dto.DataSetParamDto;
import vip.xiaonuo.report.modular.datasetparam.controller.param.DataSetParamValidationParam;
import vip.xiaonuo.report.modular.datasetparam.service.DataSetParamService;

/**
* @desc 数据集动态参数 controller
* @website https://gitee.com/anji-plus/gaea
* @author Raod
* @date 2021-03-18 12:12:33.108033200
**/
@RestController
@Api(tags = "数据集动态参数管理")
@RequestMapping("/dataSetParam")
public class DataSetParamController extends BaseResponse {

    @Autowired
    private DataSetParamService dataSetParamService;


    /**
     * 测试 查询参数是否正确
     * @param param
     * @return
     */
    @ApiOperation("测试 查询参数是否正确")
    @PostMapping("/verification")
    public ResponseBean verification(@Validated @RequestBody DataSetParamValidationParam param) {
        DataSetParamDto dto = new DataSetParamDto();
        dto.setSampleItem(param.getSampleItem());
        dto.setValidationRules(param.getValidationRules());
        return responseSuccessWithData(dataSetParamService.verification(dto));
    }
}
