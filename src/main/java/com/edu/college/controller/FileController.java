package com.edu.college.controller;

import com.edu.college.common.ret.Response;
import com.edu.college.pojo.dto.MicroAttachment;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("file")
public class FileController {
    @Resource
    private Environment environment;

    @PostMapping(value = "/upload")
    public Response add(@RequestParam(value = "file") MultipartFile multipartFile) throws IllegalStateException, IOException {//这里一定要写required=false 不然前端不传文件一定报错。到不了后台。
        return upload(multipartFile);
    }

    public Response upload(MultipartFile multipartFile) throws IOException {
        if (multipartFile.getContentType() == null) {
            return Response.fail("没有上传文件");
        }
        //获取环境中配置的自定义的系统外的路径--> [ /data/attachment/]
        String uploadDIYPath = environment.getProperty("upload.path.location");
        String pattern = "yyyy/MM/dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        //以日期当文件访问路径 及 系统路径 --- > 也是未来作为访问路径的一部分。
        String datePath = formatter.format(LocalDate.now());

        //定义上传目录//这里特地加一个file 字符串，为了让URL拦截器能识别，不拦截file下面得静态资源。
        File upload = new File(uploadDIYPath + "/file/" + datePath);

        //如果上传目录不存在，级联创建这些层级的目录。这里是只是创建了上传目录，只是目录。
        if (!upload.exists()) upload.mkdirs(); //把上传目录创建出来。

        String uploadAbsolutePath = upload.getAbsolutePath();

        //生成新的文件名。
        String fileName = multipartFile.getOriginalFilename();

        assert fileName != null;
        int index = fileName.lastIndexOf(".");

        String fileType = index != -1 ? fileName.substring(index) : "." + multipartFile.getContentType();

        final String uuid = UUID.randomUUID().toString();
        String newFileName = uuid + fileType;

        //这里一定要拿绝对路径来创建serverFile.
        File serverFile = new File(uploadAbsolutePath, newFileName);

        //core : 绝对路径创建的serverFile，springMVC 文件转移覆盖。
        multipartFile.transferTo(serverFile);
        //上传完文件之后要返回数据出去的。比如返回可以在本作用域中获取的文件大小，文件名，文件路径（网络+文件路径=浏览器上可访问的路径），
        final String s = "file/" + datePath + "/" + newFileName;
        //文件上传成功后，要把文件保存到数据库中,并查看数据库是否保存了记录。
        return Response.success(MicroAttachment.builder()
                .fileName(fileName)
                .filePath(s)
                .fileSize(multipartFile.getSize()) // 除以1024 则可以得到 KB
                .fileType(multipartFile.getContentType()).build());
    }
}
