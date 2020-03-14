package top.qiyoung.answer.controller.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.DTO.CommentDTO;
import top.qiyoung.answer.model.Comment;
import top.qiyoung.answer.model.MyUser;
import top.qiyoung.answer.service.CommentService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user/comment")
public class UserCommentController {

    @Resource
    private CommentService commentService;

    // 获取习题评论信息
    @RequestMapping("/getCommentDTOList")
    @ResponseBody
    public List<CommentDTO> getComment( Integer exerciseId){
        List<CommentDTO> commentDTOList = commentService.getCommentDTOListById(exerciseId);
        return commentDTOList;
    }

    @RequestMapping("addComment")
    @ResponseBody
    public String addComment(Comment comment, HttpServletRequest request){
//        MyUser myUser = (MyUser) request.getSession().getAttribute("myUser");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        comment.setUserId(myUser.getUserId());
        commentService.addComment(comment, userDetails);
        return "success";
    }

}
