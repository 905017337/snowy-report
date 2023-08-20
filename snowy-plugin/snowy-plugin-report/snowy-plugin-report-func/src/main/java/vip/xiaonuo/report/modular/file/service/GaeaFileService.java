package vip.xiaonuo.report.modular.file.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.report.modular.file.controller.param.GaeaFileParam;
import vip.xiaonuo.report.modular.file.entity.GaeaFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * (GaeaFile)Service
 *
 * @author peiyanni
 * @since 2021-02-18 14:48:25
 */
public interface GaeaFileService extends IService<GaeaFile> {

    /**
     * 文件上传
     *
     * @param multipartFile  文件
     * @return
     */
    GaeaFile upload(MultipartFile multipartFile);


    /**
     * 文件上传
     *
     * @param file 二选一
     * @return
     */
    GaeaFile upload(File file);
    /**
     * 根据fileId显示图片或者下载文件
     *
     * @param request
     * @param response
     * @param fileId
     * @return
     */
    ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response, String fileId);

    /**
     * 获取文件
     * @param fileId
     * @return
     */
    byte[] getFile(String fileId);

    IPage<GaeaFile> pageList(GaeaFileParam param);
}
