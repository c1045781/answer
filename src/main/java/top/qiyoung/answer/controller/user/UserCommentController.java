package top.qiyoung.answer.controller.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.dto.CommentDTO;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.model.Comment;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.service.CommentService;
import top.qiyoung.answer.service.ExerciseService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/user/comment")
public class UserCommentController {

    @Resource
    private CommentService commentService;
    @Resource
    private ExerciseService exerciseService;

    // 获取习题评论信息
    @RequestMapping("/getCommentDTOList")
    @ResponseBody
    public List<CommentDTO> getComment( Integer exerciseId){
        List<CommentDTO> commentDTOList = commentService.getCommentDTOListById(exerciseId);
        return commentDTOList;
    }

    @RequestMapping("/addComment")
    @ResponseBody
    public ResultDTO addComment(Comment comment){
        if (comment.getExerciseId() != null ){
            Exercise exercise = exerciseService.getExerciseByExerciseId(comment.getExerciseId());
            if (exercise != null){
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                comment.setExerciseId(exercise.getExerciseId());
                commentService.addComment(comment, userDetails);
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_NOT_FOUND);
            }
        }else {
            return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_NOT_FOUND);
        }
        return ResultDTO.okOf();
    }

}
