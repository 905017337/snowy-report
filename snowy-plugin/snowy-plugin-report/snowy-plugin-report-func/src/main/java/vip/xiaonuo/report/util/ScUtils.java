package vip.xiaonuo.report.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author caozhenhao
 * @version 1.0
 * @date 2023/8/20 11:18
 */

public abstract class ScUtils {
    private static String SLAT = "354816d26912441ab280f08831c38453";
    public static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    public static Pattern pattern = Pattern.compile("\\$\\{(\\w+)}");


    public static boolean matchPath(List<String> list, String path) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        } else if (!list.contains(path)) {
            Optional<String> any = list.stream().filter((s) -> {
                return antPathMatcher.match(s, path);
            }).findAny();
            return any.isPresent();
        } else {
            return true;
        }
    }

    public static String camelToUnderline(String source) {
        return StringUtils.camelToUnderline(source);
    }

    public static String underlineToCamel(String source) {
        return StringUtils.underlineToCamel(source);
    }

    public static String getPassKey(long id) {
        String tokenTmp = DigestUtils.md5DigestAsHex((SLAT + "_" + id).getBytes(Charset.forName("UTF-8")));
        return tokenTmp;
    }

    public static String UUID() {
        return UUID.randomUUID().toString().replaceAll("\\-", "");
    }

    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    public static String formatDate(Date date, String pattern) {
        return (new SimpleDateFormat(pattern)).format(date);
    }

    public static String toJSONString(Object object) {
        return JSONObject.toJSONString(object);
    }

    public static String replaceFormatString(String source, Map<String, Object> map) {
        Matcher matcher = pattern.matcher(source);
        StringBuffer sb = new StringBuffer();

        while(matcher.find()) {
            String variable = matcher.group(1);
            Object value = map.get(variable);
            if (value != null) {
                matcher.appendReplacement(sb, String.valueOf(value));
            }
        }

        return sb.toString();
    }

    public static String replaceFormatString(String source, Object param) {
        if (param instanceof String) {
            return replaceFormatString(source, param + "", "");
        } else if (!(param instanceof Integer) && !(param instanceof Long)) {
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(param));
            return replaceFormatString(source, (Map)jsonObject);
        } else {
            return replaceFormatString(source, param + "", "");
        }
    }

    public static String replaceFormatString(String source, String... args) {
        if (args != null && args.length != 0) {
            Matcher matcher = pattern.matcher(source);
            StringBuffer sb = new StringBuffer();

            for(int index = 0; matcher.find(); ++index) {
                String variable = matcher.group(1);
                String value = null;
                if (index <= args.length - 1) {
                    value = args[index];
                }

                if (value != null) {
                    matcher.appendReplacement(sb, value);
                }
            }

            return sb.toString();
        } else {
            return replaceFormatString(source, (Map)(new HashMap()));
        }
    }
}