package vip.xiaonuo.report.modular.file.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.report.core.base.ResponseBean;
import vip.xiaonuo.report.core.config.BaseResponse;
import vip.xiaonuo.report.modular.file.controller.param.GaeaFileParam;
import vip.xiaonuo.report.modular.file.entity.GaeaFile;
import vip.xiaonuo.report.modular.file.service.GaeaFileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * (GaeaFile)实体类
 *
 * @author peiyanni
 * @since 2021-02-18 14:48:33
 */
@RestController
@RequestMapping("/file")
@Api(value = "/file", tags = "文件控制器")
public class GaeaFileController extends BaseResponse  {
    @Autowired
    private GaeaFileService gaeaFileService;


    @ApiOperation("获取所有文件")
    @GetMapping("/pageList")
    public ResponseBean pageList(GaeaFileParam param){
        IPage<GaeaFile> iPage = gaeaFileService.pageList(param);
        List<GaeaFile> records = iPage.getRecords();
        Page<GaeaFile> pageDto = new Page();
        pageDto.setCurrent(iPage.getCurrent()).setRecords(records).setPages(iPage.getPages()).setTotal(iPage.getTotal()).setSize(iPage.getSize());
        return responseSuccessWithData(pageDto);

    }

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public ResponseBean upload(@RequestParam("file") MultipartFile file) {
        return ResponseBean.builder().message("success").data((gaeaFileService.upload(file))).build();
    }

    @ApiOperation("文件下载")
    @GetMapping(value = "/download/{fileId}")
    public ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileId") String fileId) {
        return gaeaFileService.download(request, response, fileId);
    }
}
