package top.qiyoung.answer.mapper;

import top.qiyoung.answer.model.ExerciseSet;

import java.util.List;

public interface ExerciseSetMapper {
    int insert(ExerciseSet exerciseSet);

    List<ExerciseSet> getAll();
}
