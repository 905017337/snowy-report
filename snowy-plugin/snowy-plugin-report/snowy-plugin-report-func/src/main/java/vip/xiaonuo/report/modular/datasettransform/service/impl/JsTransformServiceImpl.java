package vip.xiaonuo.report.modular.datasettransform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.anji.plus.gaea.exception.BusinessExceptionBuilder;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.report.core.code.ResponseCode;
import vip.xiaonuo.report.modular.datasettransform.controller.dto.DataSetTransformDto;
import vip.xiaonuo.report.modular.datasettransform.service.TransformStrategy;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by raodeming on 2021/3/23.
 */
@Component
@Slf4j
public class JsTransformServiceImpl implements TransformStrategy {

    private ScriptEngine engine;
    {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
    }

    /**
     * 数据清洗转换 类型
     *
     * @return
     */
    @Override
    public String type() {
        return "js";
    }

    /***
     * 清洗转换算法接口
     * @param def
     * @param data
     * @return
     */
    @Override
    public List<JSONObject> transform(DataSetTransformDto def, List<JSONObject> data) {
        return getValueFromJs(def,data);
    }

    private List<JSONObject> getValueFromJs(DataSetTransformDto def, List<JSONObject> data) {
        String js = def.getTransformScript();
        try {
            engine.eval(js);
            if(engine instanceof Invocable){
                Invocable invocable = (Invocable) engine;
                Object dataTransform = invocable.invokeFunction("dataTransform", data);
                if (dataTransform instanceof List) {
                    return (List<JSONObject>) dataTransform;
                }
                //前端js自定义的数组[{"aa":"bb"}]解析后变成{"0":{"aa":"bb"}}
                ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) dataTransform;
                List<JSONObject> result = new ArrayList<>();
                scriptObjectMirror.forEach((key, value) -> {
                    ScriptObjectMirror valueObject = (ScriptObjectMirror) value;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putAll(valueObject);
                    result.add(jsonObject);
                });
                return result;
            }

        } catch (Exception ex) {
            log.info("执行js异常", ex);
            throw BusinessExceptionBuilder.build(ResponseCode.EXECUTE_JS_ERROR, ex.getMessage());
        }
        return null;
    }
}
