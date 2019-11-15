package top.qiyoung.answer.mapper;

import top.qiyoung.answer.model.Exercises;
import top.qiyoung.answer.model.Query;

import java.util.List;

public interface ExercisesMapper {
    void insert(Exercises exercises);

    List<Exercises> getExercisesList(Query query);

    int countExercisesList(Query query);

    void deleteById(Integer id);

    Exercises getExercisesById(Integer id);

    void update(Exercises exercises);
}
