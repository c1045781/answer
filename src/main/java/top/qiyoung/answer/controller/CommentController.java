package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.DTO.CommentDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.Comment;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.CommentService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    // 评论查询
    @RequestMapping("/check")
    public String check(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                        @RequestParam(value = "size", defaultValue = "2") Integer size,
                        @RequestParam(value = "type", required = false) String type,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "order", defaultValue = "comment_id asc") String order,
                        Model model){
        PaginationDTO<Comment> paginationDTO = commentService.getCommentList(currentPage,size,type,search,order);
        model.addAttribute("paginationDTO",paginationDTO);
        model.addAttribute("type",type);
        model.addAttribute("search",search);
        return "manage/comment/comment";
    }

    // 删除评论
    @RequestMapping("delete")
    @ResponseBody
    public String delete(Integer id){
        int result = commentService.deleteById(id);
        if(result<=0){
            return "failure";
        }
        return "success";
    }

    // 获取习题评论信息
    @RequestMapping("getCommentDTOList")
    @ResponseBody
    public List<CommentDTO> getComment( Integer id){
        List<CommentDTO> commentDTOList = commentService.getCommentDTOListById(id);
        return commentDTOList;
    }

    @RequestMapping("addComment")
    @ResponseBody
    public String addComment(Comment comment, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        comment.setUserId(user.getUserId());
        commentService.addComment(comment);
        return "success";
    }

}
