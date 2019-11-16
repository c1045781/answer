package top.qiyoung.answer.mapper;

import org.apache.ibatis.annotations.Param;

public interface MidMapper {

    int insert(@Param("exerciseSetId") Integer exerciseSetId,@Param("exerciseId") Integer exerciseId);
}
