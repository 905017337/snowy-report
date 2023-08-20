
package vip.xiaonuo.report.modular.datasettransform.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.report.modular.datasettransform.controller.dto.DataSetTransformDto;
import vip.xiaonuo.report.modular.datasettransform.mapper.entity.DataSetTransform;

import java.util.List;

/**
* @desc DataSetTransform 数据集数据转换服务接口
* @author Raod
* @date 2021-03-18 12:13:15.591309400
**/
public interface DataSetTransformService  extends IService<DataSetTransform>  {

    List<JSONObject> transform(List<DataSetTransformDto> dataSetTransformDtoList, List<JSONObject> data);

}
