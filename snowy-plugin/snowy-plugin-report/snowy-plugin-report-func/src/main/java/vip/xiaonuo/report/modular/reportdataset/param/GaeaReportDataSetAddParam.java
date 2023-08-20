/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.report.modular.reportdataset.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 数据集管理添加参数
 *
 * @author czh
 * @date  2023/08/20 16:14
 **/
@Getter
@Setter
public class GaeaReportDataSetAddParam {

    /** 数据集编码 */
    @ApiModelProperty(value = "数据集编码", position = 2)
    private String setCode;

    /** 数据集名称 */
    @ApiModelProperty(value = "数据集名称", position = 3)
    private String setName;

    /** 数据集描述 */
    @ApiModelProperty(value = "数据集描述", position = 4)
    private String setDesc;

    /** 数据源编码 */
    @ApiModelProperty(value = "数据源编码", position = 5)
    private String sourceCode;

    /** 动态查询sql或者接口中的请求体 */
    @ApiModelProperty(value = "动态查询sql或者接口中的请求体", position = 6)
    private String dynSentence;

    /** 结果案例 */
    @ApiModelProperty(value = "结果案例", position = 7)
    private String caseResult;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG */
    @ApiModelProperty(value = "0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG", position = 8)
    private Integer enableFlag;

    /** 创建人 */
    @ApiModelProperty(value = "创建人", position = 10)
    private String createBy;

    /** 更新人 */
    @ApiModelProperty(value = "更新人", position = 12)
    private String updateBy;

    /** VERSION */
    @ApiModelProperty(value = "VERSION", position = 14)
    private Integer version;

    /** SET_TYPE */
    @ApiModelProperty(value = "SET_TYPE", position = 15)
    private String setType;

}
