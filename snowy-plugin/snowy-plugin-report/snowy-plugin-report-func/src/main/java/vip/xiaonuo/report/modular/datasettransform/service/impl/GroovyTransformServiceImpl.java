package vip.xiaonuo.report.modular.datasettransform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.anji.plus.gaea.exception.BusinessExceptionBuilder;
import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.report.core.code.ResponseCode;
import vip.xiaonuo.report.modular.datasettransform.controller.dto.DataSetTransformDto;
import vip.xiaonuo.report.modular.datasettransform.service.IGroovyHandler;
import vip.xiaonuo.report.modular.datasettransform.service.TransformStrategy;

import java.util.List;

/**
 * Created by raodeming on 2021/3/23.
 */
@Component
@Slf4j
public class GroovyTransformServiceImpl implements TransformStrategy {

    private GroovyClassLoader groovyClassLoader = new GroovyClassLoader();

    /**
     * 数据清洗转换 类型
     *
     * @return
     */
    @Override
    public String type() {
        return "javaBean";
    }

    /***
     * 清洗转换算法接口
     * @param def
     * @param data
     * @return
     */
    @Override
    public List<JSONObject> transform(DataSetTransformDto def, List<JSONObject> data) {
        String transformScript = def.getTransformScript();
        Class<?> clazz = groovyClassLoader.parseClass(transformScript);
        if (clazz != null) {
            try {
                Object instance = clazz.newInstance();
                if (instance!=null) {
                    if (instance instanceof IGroovyHandler) {
                        IGroovyHandler handler = (IGroovyHandler) instance;
                        return handler.transform(data);
                    } else {
                        System.err.println("转换失败！");
                    }
                }
            } catch (Exception e) {
                log.info("执行javaBean异常", e);
                throw BusinessExceptionBuilder.build(ResponseCode.EXECUTE_GROOVY_ERROR, e.getMessage());
            }
        }
        return data;
    }
}
