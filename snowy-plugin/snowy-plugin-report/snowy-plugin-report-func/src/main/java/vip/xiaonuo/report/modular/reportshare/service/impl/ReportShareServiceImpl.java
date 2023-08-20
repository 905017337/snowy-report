
package vip.xiaonuo.report.modular.reportshare.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.anji.plus.gaea.exception.BusinessExceptionBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.report.core.code.ResponseCode;
import vip.xiaonuo.report.core.enums.EnableFlagEnum;
import vip.xiaonuo.report.modular.reportshare.controller.dto.ReportShareDto;
import vip.xiaonuo.report.modular.reportshare.controller.param.ReportShareParam;
import vip.xiaonuo.report.modular.reportshare.mapper.ReportShareMapper;
import vip.xiaonuo.report.modular.reportshare.mapper.entity.ReportShare;
import vip.xiaonuo.report.modular.reportshare.service.ReportShareService;
import vip.xiaonuo.report.util.DateUtil;
import vip.xiaonuo.report.util.JwtUtil;
import vip.xiaonuo.report.util.MD5Util;
import vip.xiaonuo.report.util.UuidUtil;

/**
* @desc ReportShare 报表分享服务实现
* @author Raod
* @date 2021-08-18 13:37:26.663
**/
@Service
public class ReportShareServiceImpl extends ServiceImpl<ReportShareMapper, ReportShare> implements ReportShareService {
    private static final String SHARE_AJFLAG = "#/aj/";
    private static final String SHARE_ELFLAG = "#/el/";

    private static final String REPORT = "report_screen";
    private static final String EXCEL = "report_excel";
    /**
     * 默认跳转路由为aj的页面
     */
    private static final String SHARE_FLAG = "#/aj/";

    private static final String SHARE_URL = "#";

    @Autowired
    private ReportShareMapper reportShareMapper;

    @Override
    public ReportShare getDetail(Long id) {
        ReportShare reportShare = this.getById(id);
        return reportShare;
    }

    @Override
    public ReportShareDto insertShare(ReportShareDto dto) {
        //设置分享码
        if (dto.isSharePasswordFlag()) {
            dto.setSharePassword(UuidUtil.getRandomPwd(4));
        }

        ReportShareDto reportShareDto = new ReportShareDto();
        ReportShare entity = new ReportShare();
        BeanUtils.copyProperties(dto, entity);
        save(entity);
        //将分享链接返回
        reportShareDto.setShareUrl(entity.getShareUrl());
        reportShareDto.setSharePassword(dto.getSharePassword());
        return reportShareDto;
    }

    @Override
    public ReportShare detailByCode(String shareCode) {
        LambdaQueryWrapper<ReportShare> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ReportShare::getShareCode, shareCode);
        wrapper.eq(ReportShare::getEnableFlag, EnableFlagEnum.ENABLE.getCodeValue());
        ReportShare reportShare = getOne(wrapper);
        if (null == reportShare) {
            throw BusinessExceptionBuilder.build(ResponseCode.REPORT_SHARE_LINK_INVALID);
        }
        //解析jwt token，获取密码
        String password = JwtUtil.getPassword(reportShare.getShareToken());
        if (StringUtils.isNotBlank(password)) {
            //md5加密返回
            reportShare.setSharePassword(MD5Util.encrypt(password));
        }
        return reportShare;
    }

    /**
     * 延期过期时间
     *
     * @param dto
     */
    @Override
    public void shareDelay(ReportShareDto dto) {
        Integer shareValidType = dto.getShareValidType();
        if (null == dto.getId() || null == shareValidType) {
            throw BusinessExceptionBuilder.build("入参不完整");
        }
        ReportShare entity = getById(dto.getId());
        entity.setShareValidTime(DateUtil.getFutureDateTmdHmsByTime(entity.getShareValidTime(), shareValidType));
        entity.setShareToken(JwtUtil.createToken(entity.getReportCode(), entity.getShareCode(), entity.getSharePassword(), entity.getShareValidTime()));
        updateById(entity);
    }

    @Override
    public IPage<ReportShare> pageList(ReportShareParam param) {
        QueryWrapper<ReportShare> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotEmpty(param.getOrder())) {
            if("ASC".equals(param.getOrder())){
                queryWrapper.lambda().orderByAsc(ReportShare::getUpdateTime);
            }
            if("DESC".equals(param.getOrder())){
                queryWrapper.lambda().orderByDesc(ReportShare::getUpdateTime);
            }
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    /**
     * 新增初始化
     * @param entity
     */
    private void init(ReportShare entity) {
        //前端地址  window.location.href https://report.anji-plus.com/index.html#/report/bigscreen
        //截取#之前的内容
        //http://localhost:9528/#/bigscreen/viewer?reportCode=bigScreen2
        //http://127.0.0.1:9095/reportDashboard/getData
        String shareCode = UuidUtil.generateShortUuid();
        entity.setShareCode(shareCode);

//        if (entity.getShareUrl().contains(SHARE_URL)) {
//            String prefix = entity.getShareUrl().substring(0, entity.getShareUrl().indexOf("#"));
//            entity.setShareUrl(prefix + SHARE_FLAG + shareCode);
//        } else {
//            entity.setShareUrl(entity.getShareUrl() + SHARE_FLAG + shareCode);
//        }


        if (REPORT.equals(entity.getReportType())) {
            if (entity.getShareUrl().contains(SHARE_URL)) {
                String prefix = entity.getShareUrl().substring(0, entity.getShareUrl().indexOf("#"));
                entity.setShareUrl(prefix + SHARE_AJFLAG + shareCode);
            }else {
                entity.setShareUrl(entity.getShareUrl() + SHARE_AJFLAG + shareCode);
            }
        }else if (EXCEL.equals(entity.getReportType())) {
            if (entity.getShareUrl().contains(SHARE_URL)) {
                String prefix = entity.getShareUrl().substring(0, entity.getShareUrl().indexOf("#"));
                entity.setShareUrl(prefix + SHARE_ELFLAG + shareCode);
            }else {
                entity.setShareUrl(entity.getShareUrl() + SHARE_ELFLAG + shareCode);
            }
        }else {
            return;
        }

        entity.setShareValidTime(DateUtil.getFutureDateTmdHms(entity.getShareValidType()));
        entity.setShareToken(JwtUtil.createToken(entity.getReportCode(), shareCode, entity.getSharePassword(), entity.getShareValidTime()));
    }
}
