package vip.xiaonuo.report.core.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * @author caozhenhao
 * @version 1.0
 * @date 2023/8/19 18:59
 */

public final class ResponseBean implements Serializable {
    private String code;
    private String message;
    private Object[] args;
    private Object ext;
    private Object data;

    public ResponseBean() {
    }

    private ResponseBean(ResponseBean.Builder builder) {
        this.code = builder.code;
        this.args = builder.args;
        this.message = builder.message;
        this.data = builder.data;
        this.ext = builder.ext;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("ResponseBean{");
        sb.append("code='").append(this.code).append('\'');
        sb.append(", message='").append(this.message).append('\'');
        sb.append(", args=").append(this.args == null ? "null" : Arrays.asList(this.args).toString());
        sb.append(", data=").append(this.data);
        sb.append('}');
        return sb.toString();
    }

    public ResponseBean thenAsync(Consumer<Object> consumer, Object param, Executor executor) {
        executor.execute(() -> {
            consumer.accept(param);
        });
        return this;
    }

    public ResponseBean then(Consumer<Object> consumer, Object param) {
        consumer.accept(param);
        return this;
    }

    public static ResponseBean.Builder builder() {
        return new ResponseBean.Builder();
    }

    public String getCode() {
        return this.code;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getExt() {
        return this.ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    public static class Builder {
        private String code;
        private Object data;
        private String message;
        private Object[] args;
        private Object ext;

        private Builder() {
            this.code = "200";
        }

        public ResponseBean.Builder code(String code) {
            this.code = code;
            return this;
        }

        public ResponseBean.Builder message(String message) {
            this.message = message;
            return this;
        }

        public ResponseBean.Builder args(Object[] args) {
            this.args = args;
            return this;
        }

        public ResponseBean.Builder data(Object data) {
            this.data = data;
            return this;
        }

        public ResponseBean.Builder ext(Object data) {
            this.ext = this.ext;
            return this;
        }

        public ResponseBean build() {
            return new ResponseBean(this);
        }
    }
}
