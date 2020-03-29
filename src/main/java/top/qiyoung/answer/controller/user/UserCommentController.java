package top.qiyoung.answer.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.dto.CommentDTO;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.model.Comment;
import top.qiyoung.answer.model.Exercise;
import top.qiyoung.answer.service.CommentService;
import top.qiyoung.answer.service.ExerciseService;
import top.qiyoung.answer.service.NotificationService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/user/comment")
public class UserCommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private NotificationService notificationService;

    // 获取习题评论信息
    @RequestMapping("/getCommentDTOList")
    @ResponseBody
    public PaginationDTO<CommentDTO> getComment( Integer parentId,Integer currentPage){
        PaginationDTO<CommentDTO> paginationDTO= commentService.getCommentDTOListById(parentId,currentPage);
        return paginationDTO;
    }

    @RequestMapping("/addComment")
    @ResponseBody
    public ResultDTO addComment(@RequestBody Comment comment){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultDTO resultDTO = commentService.addComment(comment, userDetails);
        return resultDTO;
    }

    @RequestMapping("/addLike")
    @ResponseBody
    public ResultDTO<Comment> addLike(Integer commentId){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultDTO<Comment> resultDTO = commentService.addLike(commentId,userDetails);
        Comment comment = commentService.getCommentByCommentId(commentId);
        resultDTO.setData(comment);
        return resultDTO;
    }

    @RequestMapping("/delLike")
    @ResponseBody
    public Comment delLike(Integer commentId){
        commentService.delLike(commentId);
        Comment comment = commentService.getCommentByCommentId(commentId);
        return comment;
    }
}
