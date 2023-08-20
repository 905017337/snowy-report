
package vip.xiaonuo.report.modular.datasource.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.report.modular.dataset.controller.dto.DataSetDto;
import vip.xiaonuo.report.modular.datasource.controller.dto.DataSourceDto;
import vip.xiaonuo.report.modular.datasource.controller.param.ConnectionParam;
import vip.xiaonuo.report.modular.datasource.controller.param.DataSourceParam;
import vip.xiaonuo.report.modular.datasource.mapper.entity.DataSource;

import java.util.List;

/**
* @desc DataSource 数据集服务接口
* @author Raod
* @date 2021-03-18 12:09:57.728203200
**/
public interface DataSourceService extends IService<DataSource>  {

    /**
     * 获取所有数据源
     * @return
     */
    List<DataSource> queryAllDataSource();

    /**
     * 测试 连接
     * @param connectionParam
     * @return
     */
    Boolean testConnection(ConnectionParam connectionParam);

    /**
     * 执行sql
     * @param dto
     * @return
     */
    List<JSONObject> execute(DataSourceDto dto);

    /**
     * 执行sql,统计数据total
     * @param dataSourceDto
     * @param dto
     * @return
     */
    long total(DataSourceDto dataSourceDto, DataSetDto dto);

    IPage<DataSource>  pageList( DataSourceParam param);
}
