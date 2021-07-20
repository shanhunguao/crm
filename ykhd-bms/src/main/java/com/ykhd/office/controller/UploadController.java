package com.ykhd.office.controller;

import com.ykhd.office.component.aliyun.oss.OssService;
import com.ykhd.office.domain.entity.PictureLibrary;
import com.ykhd.office.domain.req.PictureLibraryCondition;
import com.ykhd.office.service.IPictureLibraryService;
import com.ykhd.office.util.dictionary.SystemEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author zhoufan
 * @Date 2020/12/18
 */
@RestController
@RequestMapping("/upload")
public class UploadController extends BaseController {

    @Autowired
    private OssService ossService;

    @Autowired
    private IPictureLibraryService pictureLibraryService;

    /**
     * 上传商品图片信息
     */
    @PostMapping("/images")
    public Object upload(MultipartFile file) {
        return success(ossService.uploadFile(SystemEnums.OSSFolder.SF.name(), file));
    }

    /**
     * 添加商家图片库
     */
    @PostMapping(value = "/save")
    public Object save(@RequestBody List<PictureLibrary> pictureLibrary) {
        pictureLibrary.forEach(e -> {
            e.setSupplierUserid(BaseController.getManagerInfo().getId());
        });
        return pictureLibraryService.saveBatch(pictureLibrary);
    }

    /**
     * 分页查看商家图片库
     */
    @GetMapping("/list")
    public Object list(PictureLibraryCondition pictureLibraryCondition) {
        return pictureLibraryService.list(pictureLibraryCondition);
    }

}
