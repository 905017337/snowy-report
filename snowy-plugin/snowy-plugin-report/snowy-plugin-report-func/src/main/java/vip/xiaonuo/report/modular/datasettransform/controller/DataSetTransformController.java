
package vip.xiaonuo.report.modular.datasettransform.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.report.core.config.BaseResponse;
import vip.xiaonuo.report.modular.datasettransform.service.DataSetTransformService;

/**
* @desc 数据集数据转换 controller
* @website https://gitee.com/anji-plus/gaea
* @author Raod
* @date 2021-03-18 12:13:15.591309400
**/
@RestController
@Api(tags = "数据集数据转换管理")
@RequestMapping("/dataSetTransform")
public class DataSetTransformController extends BaseResponse {

    @Autowired
    private DataSetTransformService dataSetTransformService;



}
