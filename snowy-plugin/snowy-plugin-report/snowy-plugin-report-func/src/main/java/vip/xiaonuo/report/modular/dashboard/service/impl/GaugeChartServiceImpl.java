package vip.xiaonuo.report.modular.dashboard.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import vip.xiaonuo.report.modular.dashboard.controller.dto.ChartDto;
import vip.xiaonuo.report.modular.dashboard.service.ChartStrategy;

import java.util.List;

/**
 * 饼图或者空心饼图或者漏斗图
 * Created by raodeming on 2021/4/26.
 */
@Component
public class GaugeChartServiceImpl implements ChartStrategy {
    /**
     * 图表类型
     *
     * @return
     */
    @Override
    public String type() {
        return "widget-gauge";
    }

    /**
     * 针对每种图表类型做单独的数据转换解析
     *
     * @param dto
     * @param data
     * @return
     */
    @Override
    public Object transform(ChartDto dto, List<JSONObject> data) {

//        return "{\"value\": 50, \"name\": \"名称1\", \"unit\": \"%\"}";
        return data;
    }




}
