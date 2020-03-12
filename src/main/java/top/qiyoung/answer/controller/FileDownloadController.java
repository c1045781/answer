package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

// 文件上传
@Controller
public class FileDownloadController {
    @RequestMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        String fileName1 =  System.getProperty("user.dir") + "\\src\\main\\resources\\static\\upload\\template.xls";//被下载文件的名称
        String fileName2 =  System.getProperty("user.dir") + "\\src\\main\\resources\\static\\upload\\template.xlsx";//被下载文件的名称

        // 文件地址，真实环境是存放在数据库中的
        File file1 = new File(fileName1);
        File file2 = new File(fileName2);
        // 穿件输入对象
        if (file1.exists()){
            FileInputStream fis = new FileInputStream(file1);
            // 设置相关格式
            response.setContentType("application/force-download");
            // 设置下载后的文件名以及header
            response.addHeader("Content-disposition", "attachment;fileName=" + "template.xls");
            // 创建输出对象
            OutputStream os = response.getOutputStream();
            // 常规操作
            byte[] buf = new byte[1024];
            int len = 0;
            while((len = fis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            fis.close();
        }else if (file2.exists()){
            FileInputStream fis = new FileInputStream(file2);
            // 设置相关格式
            response.setContentType("application/force-download");
            // 设置下载后的文件名以及header
            response.addHeader("Content-disposition", "attachment;fileName=" + "template.xlsx");
            // 创建输出对象
            OutputStream os = response.getOutputStream();
            // 常规操作
            byte[] buf = new byte[1024];
            int len = 0;
            while((len = fis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            fis.close();
        }else {
            throw new IOException();
        }
    }
}
