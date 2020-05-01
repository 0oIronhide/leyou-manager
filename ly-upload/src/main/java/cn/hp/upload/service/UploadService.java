package cn.hp.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Ironhide
 * @create 2020-04-29-22:07
 */
@Service
public class UploadService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    public String uploadImage(MultipartFile file) {
        String url = null;
        //获取文件的名称
        String originalFilename = file.getOriginalFilename();
        //获取文件名的后缀
        String ext = StringUtils.substringAfterLast(originalFilename, ".");
        try {
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
            return "http://image.leyou.com/" + storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

}
