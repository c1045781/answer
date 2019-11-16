package top.qiyoung.answer.mapper;

import top.qiyoung.answer.model.ExerciseContent;

public interface ExerciseContentMapper {
    void insert(ExerciseContent exerciseContent);

    ExerciseContent getExerciseContentById(Integer exerciseContentId);

    void deleteById(Integer exerciseContentId);

    void update(ExerciseContent exerciseContent);
}
