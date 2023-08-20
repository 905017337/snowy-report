
package vip.xiaonuo.report.modular.dataset.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.anji.plus.gaea.constant.Enabled;
import com.anji.plus.gaea.exception.BusinessExceptionBuilder;
import com.anji.plus.gaea.utils.GaeaBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.report.core.code.ResponseCode;
import vip.xiaonuo.report.core.enums.SetTypeEnum;
import vip.xiaonuo.report.modular.dataset.controller.dto.DataSetDto;
import vip.xiaonuo.report.modular.dataset.controller.dto.OriginalDataDto;
import vip.xiaonuo.report.modular.dataset.controller.param.DataSetParam;
import vip.xiaonuo.report.modular.dataset.mapper.DataSetMapper;
import vip.xiaonuo.report.modular.dataset.mapper.entity.DataSet;
import vip.xiaonuo.report.modular.dataset.service.DataSetService;
import vip.xiaonuo.report.modular.datasetparam.controller.dto.DataSetParamDto;
import vip.xiaonuo.report.modular.datasetparam.service.DataSetParamService;
import vip.xiaonuo.report.modular.datasettransform.controller.dto.DataSetTransformDto;
import vip.xiaonuo.report.modular.datasettransform.mapper.entity.DataSetTransform;
import vip.xiaonuo.report.modular.datasettransform.service.DataSetTransformService;
import vip.xiaonuo.report.modular.datasource.controller.dto.DataSourceDto;
import vip.xiaonuo.report.modular.datasource.mapper.entity.DataSource;
import vip.xiaonuo.report.modular.datasource.service.DataSourceService;
import vip.xiaonuo.report.util.JdbcConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Raod
 * @desc DataSet 数据集服务实现
 * @date 2021-03-18 12:11:31.150755900
 **/
@Service
//@RequiredArgsConstructor
@Slf4j
public class DataSetServiceImpl  extends ServiceImpl<DataSetMapper, DataSet> implements DataSetService {

    @Autowired
    private DataSetMapper dataSetMapper;

    @Autowired
    private DataSetParamService dataSetParamService;

    @Autowired
    private DataSetTransformService dataSetTransformService;

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public IPage<DataSet> pageList(DataSetParam param) {
        QueryWrapper<DataSet> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotEmpty(param.getOrder())) {
            if("ASC".equals(param.getOrder())){
                queryWrapper.lambda().orderByAsc(DataSet::getUpdateTime);
            }
            if("DESC".equals(param.getOrder())){
                queryWrapper.lambda().orderByDesc(DataSet::getUpdateTime);
            }
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    /**
     * 单条详情
     *
     * @param id
     * @return
     */
    @Override
    public DataSetDto detailSet(Long id) {
        DataSetDto dto = new DataSetDto();
        DataSet result = this.getById(id);
        String setCode = result.getSetCode();
        GaeaBeanUtils.copyAndFormatter(result, dto);
        return getDetailSet(dto, setCode);
    }

    /**
     * 单条详情
     *
     * @param setCode
     * @return
     */
    @Override
    public DataSetDto detailSet(String setCode) {
        DataSetDto dto = new DataSetDto();
        QueryWrapper<DataSet> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DataSet::getSetCode,setCode);
        DataSet result = this.getOne(wrapper);
        GaeaBeanUtils.copyAndFormatter(result, dto);
        return getDetailSet(dto, setCode);
    }

    public DataSetDto getDetailSet(DataSetDto dto, String setCode) {
        //查询参数
        final QueryWrapper<vip.xiaonuo.report.modular.datasetparam.mapper.entity.DataSetParam> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(vip.xiaonuo.report.modular.datasetparam.mapper.entity.DataSetParam::getSetCode, setCode);
        List<vip.xiaonuo.report.modular.datasetparam.mapper.entity.DataSetParam> dataSetParamList = dataSetParamService.list(wrapper);
        List<DataSetParamDto> dataSetParamDtoList = new ArrayList<>();
        dataSetParamList.forEach(dataSetParam -> {
            DataSetParamDto dataSetParamDto = new DataSetParamDto();
            GaeaBeanUtils.copyAndFormatter(dataSetParam, dataSetParamDto);
            dataSetParamDtoList.add(dataSetParamDto);
        });
        dto.setDataSetParamDtoList(dataSetParamDtoList);

        //数据转换

        List<DataSetTransform> dataSetTransformList = dataSetTransformService.list(
                new QueryWrapper<DataSetTransform>()
                        .lambda()
                        .eq(DataSetTransform::getSetCode, setCode)
                        .orderByAsc(DataSetTransform::getOrderNum)
        );
        List<DataSetTransformDto> dataSetTransformDtoList = new ArrayList<>();
        dataSetTransformList.forEach(dataSetTransform -> {
            DataSetTransformDto dataSetTransformDto = new DataSetTransformDto();
            GaeaBeanUtils.copyAndFormatter(dataSetTransform, dataSetTransformDto);
            dataSetTransformDtoList.add(dataSetTransformDto);
        });
        dto.setDataSetTransformDtoList(dataSetTransformDtoList);

        if (StringUtils.isNotBlank(dto.getCaseResult())) {
            try {
                JSONArray jsonArray = JSONArray.parseArray(dto.getCaseResult());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                dto.setSetParamList(jsonObject.keySet());
            } catch (Exception e) {
                log.error("error", e);
            }
        }
        return dto;
    }


    /**
     * 新增数据集、添加查询参数、数据转换
     *
     * @param dto
     */
    @Override
    @Transactional
    public DataSetDto insertSet(DataSetDto dto) {
        List<DataSetParamDto> dataSetParamDtoList = dto.getDataSetParamDtoList();
        List<DataSetTransformDto> dataSetTransformDtoList = dto.getDataSetTransformDtoList();

        //1.新增数据集
        DataSet dataSet = new DataSet();
        BeanUtils.copyProperties(dto, dataSet);
        if (StringUtils.isNotBlank(dataSet.getCaseResult())) {
            try {
                JSONArray objects = JSONObject.parseArray(dataSet.getCaseResult());
                if (objects.size() > 1) {
                    Object o = objects.get(0);
                    objects = new JSONArray();
                    objects.add(o);
                    dataSet.setCaseResult(JSON.toJSONString(objects, SerializerFeature.WriteMapNullValue));
                }
            } catch (Exception e) {
                log.info("结果集只保留一行数据失败...{}", e.getMessage());
            }
        }
        this.dataSetMapper.insert(dataSet);
        //2.更新查询参数
        dataSetParamBatch(dataSetParamDtoList, dto.getSetCode());

        //3.更新数据转换
        dataSetTransformBatch(dataSetTransformDtoList, dto.getSetCode());
        return dto;
    }

    /**
     * 更新数据集、添加查询参数、数据转换
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSet(DataSetDto dto) {
        List<DataSetParamDto> dataSetParamDtoList = dto.getDataSetParamDtoList();
        List<DataSetTransformDto> dataSetTransformDtoList = dto.getDataSetTransformDtoList();
        //1.更新数据集
        DataSet dataSet = new DataSet();
        BeanUtils.copyProperties(dto, dataSet);
        if (StringUtils.isNotBlank(dataSet.getCaseResult())) {
            try {
                JSONArray objects = JSONObject.parseArray(dataSet.getCaseResult());
                if (objects.size() > 1) {
                    Object o = objects.get(0);
                    objects = new JSONArray();
                    objects.add(o);
                    dataSet.setCaseResult(JSON.toJSONString(objects, SerializerFeature.WriteMapNullValue));
                }
            } catch (Exception e) {
                log.info("结果集只保留一行数据失败...{}", e.getMessage());
            }
        }
        this.dataSetMapper.updateById(dataSet);

        //2.更新查询参数
        dataSetParamBatch(dataSetParamDtoList, dto.getSetCode());

        //3.更新数据转换
        dataSetTransformBatch(dataSetTransformDtoList, dto.getSetCode());
    }


    /**
     * 删除数据集、添加查询参数、数据转换
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSet(Long id) {
        DataSet dataSet = getById(id);
        String setCode = dataSet.getSetCode();
        //1.删除数据集
        this.dataSetMapper.deleteById(id);

        //2.删除查询参数
        dataSetParamService.remove(
                new QueryWrapper<vip.xiaonuo.report.modular.datasetparam.mapper.entity.DataSetParam>()
                        .lambda()
                        .eq(vip.xiaonuo.report.modular.datasetparam.mapper.entity.DataSetParam::getSetCode, setCode)
        );

        //3.删除数据转换
        dataSetTransformService.remove(
                new QueryWrapper<DataSetTransform>()
                        .lambda()
                        .eq(DataSetTransform::getSetCode, setCode)
        );
    }

    /**
     * 获取数据
     *
     * @param dto
     * @return
     */
    @Override
    public OriginalDataDto getData(DataSetDto dto) {

        OriginalDataDto originalDataDto = new OriginalDataDto();
        String setCode = dto.getSetCode();
        if (StringUtils.isBlank(setCode)) {
            return new OriginalDataDto(new ArrayList<>());
        }
        //1.获取数据集、参数替换、数据转换
        DataSetDto dataSetDto = detailSet(setCode);
        String dynSentence = dataSetDto.getDynSentence();
        //2.获取数据源
        DataSource dataSource;
        if (StringUtils.isNotBlank(dataSetDto.getSetType())
                && dataSetDto.getSetType().equals(SetTypeEnum.HTTP.getCodeValue())) {
            //http不需要数据源，兼容已有的逻辑，将http所需要的数据塞进DataSource
            dataSource = new DataSource();
            dataSource.setSourceConfig(dynSentence);
            dataSource.setSourceType(JdbcConstants.HTTP);
            String body = JSONObject.parseObject(dynSentence).getString("body");
            if (StringUtils.isNotBlank(body)) {
                dynSentence = body;
            } else {
                dynSentence = "{}";
            }

        } else {
            final QueryWrapper<DataSource> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(DataSource::getSourceCode,dataSetDto.getSourceCode());
            dataSource = dataSourceService.getOne(wrapper);
        }

        //3.参数替换
        //3.1参数校验
        log.debug("参数校验替换前：{}", dto.getContextData());
        boolean verification = dataSetParamService.verification(dataSetDto.getDataSetParamDtoList(), dto.getContextData());
        if (!verification) {
            throw BusinessExceptionBuilder.build(ResponseCode.RULE_FIELDS_CHECK_ERROR);
        }
        dynSentence = dataSetParamService.transform(dto.getContextData(), dynSentence);
        log.debug("参数校验替换后：{}", dto.getContextData());
        //4.获取数据
        DataSourceDto dataSourceDto = new DataSourceDto();
        BeanUtils.copyProperties(dataSource, dataSourceDto);
        dataSourceDto.setDynSentence(dynSentence);
        dataSourceDto.setContextData(dto.getContextData());
        //获取total,判断contextData中是否传入分页参数
        if (null != dto.getContextData()
                && dto.getContextData().containsKey("pageNumber")
                && dto.getContextData().containsKey("pageSize")) {
            long total = dataSourceService.total(dataSourceDto, dto);
            originalDataDto.setTotal(total);
        }
        List<JSONObject> data = dataSourceService.execute(dataSourceDto);
        //5.数据转换
        List<JSONObject> transform = dataSetTransformService.transform(dataSetDto.getDataSetTransformDtoList(), data);
        originalDataDto.setData(transform);
        return originalDataDto;
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public OriginalDataDto testTransform(DataSetDto dto) {
        String dynSentence = dto.getDynSentence();

        OriginalDataDto originalDataDto = new OriginalDataDto();
        String sourceCode = dto.getSourceCode();
        //1.获取数据源
        DataSource dataSource;
        if (dto.getSetType().equals(SetTypeEnum.HTTP.getCodeValue())) {
            //http不需要数据源，兼容已有的逻辑，将http所需要的数据塞进DataSource
            dataSource = new DataSource();
            dataSource.setSourceConfig(dynSentence);
            dataSource.setSourceType(JdbcConstants.HTTP);
            String body = JSONObject.parseObject(dynSentence).getString("body");
            if (StringUtils.isNotBlank(body)) {
                dynSentence = body;
            } else {
                dynSentence = "{}";
            }

        } else {
            final QueryWrapper<DataSource> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(DataSource::getSourceCode,sourceCode);
            dataSource = dataSourceService.getOne(queryWrapper);
        }

        //3.参数替换
        //3.1参数校验
        boolean verification = dataSetParamService.verification(dto.getDataSetParamDtoList(), null);
        if (!verification) {
            throw BusinessExceptionBuilder.build(ResponseCode.RULE_FIELDS_CHECK_ERROR);
        }

        dynSentence = dataSetParamService.transform(dto.getDataSetParamDtoList(), dynSentence);
        //4.获取数据
        DataSourceDto dataSourceDto = new DataSourceDto();
        BeanUtils.copyProperties(dataSource, dataSourceDto);
        dataSourceDto.setDynSentence(dynSentence);
        dataSourceDto.setContextData(setContextData(dto.getDataSetParamDtoList()));

        //获取total,判断DataSetParamDtoList中是否传入分页参数
        Map<String, Object> collect = dto.getDataSetParamDtoList().stream().collect(Collectors.toMap(DataSetParamDto::getParamName, DataSetParamDto::getSampleItem));
        if (collect.containsKey("pageNumber") && collect.containsKey("pageSize")) {
            dto.setContextData(collect);
            long total = dataSourceService.total(dataSourceDto, dto);
            originalDataDto.setTotal(total);
        }

        List<JSONObject> data = dataSourceService.execute(dataSourceDto);
        //5.数据转换
        List<JSONObject> transform = dataSetTransformService.transform(dto.getDataSetTransformDtoList(), data);
        originalDataDto.setData(transform);
        return originalDataDto;
    }


    /**
     * 获取所有数据集
     *
     * @return
     */
    @Override
    public List<DataSet> queryAllDataSet() {
        LambdaQueryWrapper<DataSet> wrapper = Wrappers.lambdaQuery();
        wrapper.select(DataSet::getSetCode, DataSet::getSetName, DataSet::getSetDesc, DataSet::getId)
                .eq(DataSet::getEnableFlag, Enabled.YES.getValue());
        wrapper.orderByDesc(DataSet::getUpdateTime);
        return dataSetMapper.selectList(wrapper);
    }

    public void dataSetParamBatch(List<DataSetParamDto> dataSetParamDtoList, String setCode) {
        dataSetParamService.remove(
                new QueryWrapper<vip.xiaonuo.report.modular.datasetparam.mapper.entity.DataSetParam>()
                        .lambda()
                        .eq(vip.xiaonuo.report.modular.datasetparam.mapper.entity.DataSetParam::getSetCode, setCode)
        );
        if (null == dataSetParamDtoList || dataSetParamDtoList.size() <= 0) {
            return;
        }
//        List<DataSetParam> dataSetParamList = new ArrayList<>();
        dataSetParamDtoList.forEach(dataSetParamDto -> {
            vip.xiaonuo.report.modular.datasetparam.mapper.entity.DataSetParam dataSetParam =
                    new vip.xiaonuo.report.modular.datasetparam.mapper.entity.DataSetParam();
            BeanUtils.copyProperties(dataSetParamDto, dataSetParam);
            dataSetParam.setSetCode(setCode);
            //不采用批量
            dataSetParamService.save(dataSetParam);
//            dataSetParamList.add(dataSetParam);
        });
//        dataSetParamService.insertBatch(dataSetParamList);

    }

    public void dataSetTransformBatch(List<DataSetTransformDto> dataSetTransformDtoList, String setCode) {
        dataSetTransformService.remove(
                new QueryWrapper<DataSetTransform>()
                        .lambda()
                        .eq(DataSetTransform::getSetCode, setCode)
        );
        if (null == dataSetTransformDtoList || dataSetTransformDtoList.size() <= 0) {
            return;
        }
//        List<DataSetTransform> dataSetTransformList = new ArrayList<>();
        for (int i = 0; i < dataSetTransformDtoList.size(); i++) {
            DataSetTransform dataSetTransform = new DataSetTransform();
            BeanUtils.copyProperties(dataSetTransformDtoList.get(i), dataSetTransform);
            dataSetTransform.setOrderNum(i + 1);
            dataSetTransform.setSetCode(setCode);
            //不采用批量
            dataSetTransformService.save(dataSetTransform);
//            dataSetTransformList.add(dataSetTransform);
        }
//        dataSetTransformService.insertBatch(dataSetTransformList);
    }

    /**
     * dataSetParamDtoList转map
     *
     * @param dataSetParamDtoList
     * @return
     */
    public Map<String, Object> setContextData(List<DataSetParamDto> dataSetParamDtoList) {
        Map<String, Object> map = new HashMap<>();
        if (null != dataSetParamDtoList && dataSetParamDtoList.size() > 0) {
            dataSetParamDtoList.forEach(dataSetParamDto -> map.put(dataSetParamDto.getParamName(), dataSetParamDto.getSampleItem()));
        }
        return map;
    }

}
