package top.qiyoung.answer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@SpringBootTest(classes=AnswerApplication.class)
@RunWith(SpringRunner.class)
class AnswerApplicationTests {

    @Test
    void contextLoads() throws IOException {
        /*String hashpw = BCrypt.hashpw("123", BCrypt.gensalt());
        System.out.println(hashpw);*/
        /*String path = "c:/software/";
        String fileName = "test";
        String fileType = "xlsx";
        //创建工作文档对象
        Workbook wb = null;
        if (fileType.equals("xls")) {
            wb = new HSSFWorkbook();
        }
        else if(fileType.equals("xlsx"))
        {
            wb = new XSSFWorkbook();
        }
        else
        {
            System.out.println("您的文档格式不正确！");
        }
        //创建sheet对象
        Sheet sheet1 = (Sheet) wb.createSheet("sheet1");
        //循环写入行数据
        for (int i = 0; i < 5; i++) {
            Row row = (Row) sheet1.createRow(i);
            //循环写入列数据
            for (int j = 0; j < 8; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue("测试"+j);
            }
        }
        //创建文件流
        OutputStream stream = new FileOutputStream(path+fileName+"."+fileType);
        //写入数据
        wb.write(stream);
        //关闭文件流
        stream.close();*/
    }

}
