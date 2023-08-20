package vip.xiaonuo.report.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.xiaonuo.report.core.base.ResponseBean;

/**
 * @author caozhenhao
 * @version 1.0
 * @date 2023/8/19 18:59
 */

public  abstract class BaseResponse {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public BaseResponse() {
    }

    public ResponseBean responseSuccess() {
        return ResponseBean.builder().build();
    }

    public ResponseBean responseSuccess(String code, Object... args) {
        return ResponseBean.builder().code(code).args(args).build();
    }

    public ResponseBean responseSuccessWithData(Object data) {
        return ResponseBean.builder().data(data).build();
    }

    public ResponseBean responseSuccessWithData(String code, Object data, Object... args) {
        return ResponseBean.builder().code(code).data(data).args(args).build();
    }

    public ResponseBean successWithData(String code, Object data, Object... args) {
        return this.responseSuccessWithData(code, data, args);
    }

    public ResponseBean successWithData(Object data) {
        return ResponseBean.builder().data(data).build();
    }

    public ResponseBean failure() {
        return ResponseBean.builder().code("500").build();
    }

    public ResponseBean failure(String code, Object... args) {
        return ResponseBean.builder().code(code).args(args).build();
    }

    public ResponseBean failureWithData(String code, Object data, Object... args) {
        return ResponseBean.builder().code(code).args(args).data(data).build();
    }
}