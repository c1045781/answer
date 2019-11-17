package top.qiyoung.answer.utils;

import java.io.File;

public class DeleteFile {
    public String delFile(String filepath) {
        String resultInfo;
        File file = new File(filepath);
        if (file.exists()) {
            if (file.delete()) {
                resultInfo =  "1-删除成功";
            } else {
                resultInfo =  "0-删除失败";
            }
        } else {
            resultInfo = "文件不存在！";
        }
        return resultInfo;
    }
}
