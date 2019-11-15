package top.qiyoung.answer.mapper;

import top.qiyoung.answer.model.ExercisesContent;

public interface ExercisesContentMapper {
    void insert(ExercisesContent exercisesContent);

    ExercisesContent getExercisesContentById(Integer exercisesContentId);

    void deleteById(Integer exercisesContentId);

    void update(ExercisesContent exercisesContent);
}
