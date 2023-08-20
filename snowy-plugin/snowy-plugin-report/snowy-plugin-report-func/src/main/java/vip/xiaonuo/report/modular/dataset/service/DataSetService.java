
package vip.xiaonuo.report.modular.dataset.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.report.modular.dataset.controller.dto.DataSetDto;
import vip.xiaonuo.report.modular.dataset.controller.dto.OriginalDataDto;
import vip.xiaonuo.report.modular.dataset.controller.param.DataSetParam;
import vip.xiaonuo.report.modular.dataset.mapper.entity.DataSet;

import java.util.List;

/**
* @desc DataSet 数据集服务接口
* @author Raod
* @date 2021-03-18 12:11:31.150755900
**/
public interface DataSetService  extends IService<DataSet> {

    /**
     * 单条详情
     * @param id
     * @return
     */
    DataSetDto detailSet(Long id);

    /**
     * 单条详情
     * @param setCode
     * @return
     */
    DataSetDto detailSet(String setCode);

    /**
     * 新增数据集、添加查询参数、数据转换
     * @param dto
     */
    DataSetDto insertSet(DataSetDto dto);

    /**
     * 更新数据集、添加查询参数、数据转换
     * @param dto
     */
    void updateSet(DataSetDto dto);

    /**
     * 删除数据集、添加查询参数、数据转换
     * @param id
     */
    void deleteSet(Long id);

    /**
     * 获取数据
     * @param dto
     * @return
     */
    OriginalDataDto getData(DataSetDto dto);

    /**
     *
     * @param dto
     * @return
     */
    OriginalDataDto testTransform(DataSetDto dto);

    /**
     * 获取所有数据集
     * @return
     */
    List<DataSet> queryAllDataSet();

    IPage<DataSet> pageList(DataSetParam param);
}
