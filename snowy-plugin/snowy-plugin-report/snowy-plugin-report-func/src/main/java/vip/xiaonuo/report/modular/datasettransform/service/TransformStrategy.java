package vip.xiaonuo.report.modular.datasettransform.service;

import com.alibaba.fastjson.JSONObject;
import vip.xiaonuo.report.modular.datasettransform.controller.dto.DataSetTransformDto;

import java.util.List;

/**
 * Created by raodeming on 2021/3/23.
 */
public interface TransformStrategy {
    /**
     * 数据清洗转换 类型
     * @return
     */
    String type();

    /***
     * 清洗转换算法接口
     * @param def
     * @param data
     * @return
     */
    List<JSONObject> transform(DataSetTransformDto def, List<JSONObject> data);
}
