
package vip.xiaonuo.report.modular.datasettransform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import vip.xiaonuo.report.modular.datasettransform.controller.dto.DataSetTransformDto;
import vip.xiaonuo.report.modular.datasettransform.mapper.DataSetTransformMapper;
import vip.xiaonuo.report.modular.datasettransform.mapper.entity.DataSetTransform;
import vip.xiaonuo.report.modular.datasettransform.service.DataSetTransformService;
import vip.xiaonuo.report.modular.datasettransform.service.TransformStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @desc DataSetTransform 数据集数据转换服务实现
* @author Raod
* @date 2021-03-18 12:13:15.591309400
**/
@Service
//@RequiredArgsConstructor
public class DataSetTransformServiceImpl extends ServiceImpl<DataSetTransformMapper, DataSetTransform> implements DataSetTransformService  {

    private final Map<String, TransformStrategy> queryServiceImplMap = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    private DataSetTransformMapper dataSetTransformMapper;

    public TransformStrategy getTarget(String type) {
        return queryServiceImplMap.get(type);
    }



    @Override
    public List<JSONObject> transform(List<DataSetTransformDto> dataSetTransformDtoList, List<JSONObject> data) {
        if (dataSetTransformDtoList == null || dataSetTransformDtoList.size() <= 0) {
            return data;
        }

        for (DataSetTransformDto dataSetTransformDto : dataSetTransformDtoList) {
            data = getTarget(dataSetTransformDto.getTransformType()).transform(dataSetTransformDto, data);
        }
        return data;
    }
}
