package top.qiyoung.answer.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.model.Comment;
import top.qiyoung.answer.service.CommentService;

import javax.annotation.Resource;

@Controller
@RequestMapping("/manager/comment")
public class ManagerCommentController {

    @Resource
    private CommentService commentService;

    // 评论查询
    @RequestMapping("/check")
    public String check(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                        @RequestParam(value = "size", defaultValue = "10") Integer size,
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
    public String delete(Integer commentId){
        int result = commentService.deleteByCommentId(commentId);
        if(result<=0){
            return "failure";
        }
        return "success";
    }
}
