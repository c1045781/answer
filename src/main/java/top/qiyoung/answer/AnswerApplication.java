package top.qiyoung.answer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.qiyoung.Answer.mapper")
public class AnswerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnswerApplication.class, args);
    }

}
