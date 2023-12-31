package vip.xiaonuo.report.modular.file.controller.param;


import com.anji.plus.gaea.annotation.Query;
import com.anji.plus.gaea.constant.QueryEnum;
import com.anji.plus.gaea.curd.params.PageParam;
import lombok.Data;

import java.io.Serializable;

/**
 * (GaeaFile)param
 *
 * @author peiyanni
 * @since 2021-02-18 14:48:29
 */
@Data
public class GaeaFileParam extends PageParam implements Serializable {

    /** 模糊查询 */
    @Query(value = QueryEnum.LIKE)
    private String filePath;

    /** 模糊查询 */
    @Query(value = QueryEnum.EQ)
    private String fileType;
}
