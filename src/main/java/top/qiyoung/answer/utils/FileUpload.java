package top.qiyoung.answer.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUpload {
    public String upload(MultipartFile avatarImg) throws IOException {
        if (!avatarImg.isEmpty()) {
            String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\upload\\"; // System.getProperty("user.dir")获取项目路径
            String filename = avatarImg.getOriginalFilename();
            String[] split = filename.split("\\.");
            String suffix = split[split.length - 1];
            if (suffix.equals("jpg") || suffix.equals("png") || suffix.equals("jpeg") || suffix.equals("xls") || suffix.equals("xlsx")) {
                filename = UUID.randomUUID() + "." + suffix;
                File filepath = new File(path, filename);
                if (!filepath.getParentFile().exists()) {
                    filepath.getParentFile().mkdir();
                }
                avatarImg.transferTo(new File(path + File.separator + filename));
            } else {
                throw new IOException();
            }
            return "/upload/" + filename;
        } else {
            return "/uoload/default.jpg";
        }
    }
}
