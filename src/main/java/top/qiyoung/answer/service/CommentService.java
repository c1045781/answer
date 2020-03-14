package top.qiyoung.answer.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.DTO.CommentDTO;
import top.qiyoung.answer.DTO.PaginationDTO;
import top.qiyoung.answer.mapper.CommentMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.Comment;
import top.qiyoung.answer.model.Query;
import top.qiyoung.answer.model.MyUser;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private UserMapper userMapper;

    public PaginationDTO<Comment> getCommentList(Integer currentPage, Integer size, String type, String search, String order) {
        PaginationDTO<Comment> paginationDTO = new PaginationDTO<>(currentPage,size);
        Query query = new Query();
        query.setIndex((currentPage-1)*size);
        query.setSize(size);
        query.setSearch(search);
        query.setType(type);
        query.setOrder(order);
        List<Comment> commentList = commentMapper.getCommentList(query);
        paginationDTO.setDataList(commentList);

        query.setIndex(null);
        query.setSize(null);
        int count = commentMapper.countCommentList(query);
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int)Math.ceil((double) paginationDTO.getTotalSize()/(double)size));
        return paginationDTO;
    }

    public int deleteByCommentId(Integer commentId) {
        return commentMapper.deleteByCommentId(commentId);
    }

    public int countComment() {
        return commentMapper.countCommentList(new Query());
    }

    public List<CommentDTO> getCommentDTOListById(Integer exerciseId) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        List<Comment> commentList = commentMapper.getCommentListByExerciseId(exerciseId);
        for (Comment comment : commentList) {
            MyUser myUser = userMapper.getUserById(comment.getUserId());
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentId(comment.getId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCreateDate(comment.getCreateTime());
            commentDTO.setMyUser(myUser);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    public void addComment(Comment comment, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        comment.setUserId(myUser.getUserId());
        comment.setCreateTime(new Date());
        commentMapper.addComment(comment);
    }
}
