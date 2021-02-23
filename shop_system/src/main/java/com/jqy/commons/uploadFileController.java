package com.jqy.commons;

import com.jqy.oss.OssUtil;
import com.jqy.result.CommonsReturn;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
/**
 * @ClassName uploadFileController
 * @Deacription TODO
 * @Author 阳
 * @Date 2020/12/3 11:56
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@RequestMapping("/uploadPhoto")
public class uploadFileController {

    @PostMapping
    public CommonsReturn uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String url = OssUtil.uploadFile("images", file);
        return CommonsReturn.success(url);
    }
}
