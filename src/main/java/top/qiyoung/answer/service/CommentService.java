package top.qiyoung.answer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.dto.CommentDTO;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.exception.CustomizeErrorCode;
import top.qiyoung.answer.exception.CustomizeException;
import top.qiyoung.answer.mapper.CommentMapper;
import top.qiyoung.answer.mapper.NotificationMapper;
import top.qiyoung.answer.mapper.UserMapper;
import top.qiyoung.answer.model.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private NotificationMapper notificationMapper;

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

    public PaginationDTO<CommentDTO> getCommentDTOListById(Integer parentId,Integer currentPage) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        if (parentId == null ){
            throw new CustomizeException(CustomizeErrorCode.EXERCISE_NOT_FOUND);
        }
        Exercise exercise = exerciseService.getExerciseByExerciseId(parentId);
        if (exercise == null){
            throw new CustomizeException(CustomizeErrorCode.EXERCISE_NOT_FOUND);
        }
        List<Comment> commentList = commentMapper.getCommentListByParentIdType1(parentId,(currentPage-1),1);
        int count1 = commentMapper.CountCommentListByParentIdType1(parentId);
        for (Comment comment : commentList) {
            List<Comment> comments = commentMapper.getCommentListByParentIdType2(comment.getId(),0,1);
            int count2 = commentMapper.CountCommentListByParentIdType2(comment.getId());
            List<CommentDTO> commentDTOS = new ArrayList<>();
            for (Comment co : comments) {
                MyUser myUser = userMapper.getUserById(co.getUserId());
                MyUser receiver = userMapper.getUserById(co.getReceiverId());
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setExercise(exercise);
                commentDTO.setCommentId(co.getId());
                commentDTO.setLikeCount(co.getLikeCount());
                commentDTO.setContent(co.getContent());
                commentDTO.setReceiver(receiver);
                commentDTO.setMyUser(myUser);
                commentDTO.setCreateDate(co.getCreateTime());
                commentDTOS.add(commentDTO);
            }
            MyUser myUser = userMapper.getUserById(comment.getUserId());
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentId(comment.getId());
            commentDTO.setExercise(exercise);
//            commentDTO.setCommentDTOs(commentDTOS);
            commentDTO.setPaginationDTO(new PaginationDTO<>(1,1,(int) Math.ceil((double) count2 / (double) 1),count2,null,null,commentDTOS));
            commentDTO.setLikeCount(comment.getLikeCount());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCreateDate(comment.getCreateTime());
            commentDTO.setMyUser(myUser);
            commentDTOList.add(commentDTO);
        }
        PaginationDTO<CommentDTO> paginationDTO = new PaginationDTO<>(currentPage,1,(int) Math.ceil((double) count1 / (double) 1),count1,null,null,commentDTOList);
        return paginationDTO;
    }

    public ResultDTO addComment(Comment comment, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        comment.setUserId(myUser.getUserId());
        comment.setCreateTime(new Date());
        comment.setLikeCount(0);

        if (comment.getType() != null ){ // 二级评论
            Comment dbComment = commentMapper.getCommentByCommentId(comment.getParentId());
            MyUser user = userMapper.getUserById(dbComment.getUserId());
            if (dbComment == null){
                return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            comment.setReceiverId(dbComment.getUserId());
            while (dbComment.getType() != 1){
                dbComment = commentMapper.getCommentByCommentId(dbComment.getParentId());
            }
            comment.setParentId(dbComment.getId());
            Notification notification = new Notification(null,myUser.getUserId(),user.getUserId(),dbComment.getParentId(),2,new Date(),0,comment.getContent());
            notificationMapper.addNotification(notification);
        }else { // 一级评论
            comment.setType(1);
            Exercise exercise = exerciseService.getExerciseByExerciseId(comment.getParentId());
            if (exercise != null){
                comment.setParentId(exercise.getExerciseId());
                comment.setReceiverId(-1);
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_NOT_FOUND);
            }
        }
        commentMapper.addComment(comment);
        return ResultDTO.okOf();
    }

    public Comment getCommentByCommentId(Integer commentId) {
        Comment comment = commentMapper.getCommentByCommentId(commentId);
        return comment;
    }

    public ResultDTO addLike(Integer commentId,UserDetails userDetails) {
        MyUser nofiter = userMapper.findUserByAccount(userDetails.getUsername());
        Comment comment = commentMapper.getCommentByCommentId(commentId);
        MyUser receiver = userMapper.getUserById(comment.getUserId());
        Comment dbComment = comment;
        while (dbComment.getType() != 1){
            dbComment = commentMapper.getCommentByCommentId(dbComment.getParentId());
        }
        if (comment != null){
            commentMapper.addLike(commentId);
            Notification notification = new Notification(null,nofiter.getUserId(),receiver.getUserId(),dbComment.getParentId(),1,new Date(),0,comment.getContent());
            notificationMapper.addNotification(notification);
        }else {
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_NOT_FOUND);
        }
        return ResultDTO.okOf();
    }

    public void delLike(Integer commentId) {
        Comment comment = commentMapper.getCommentByCommentId(commentId);
        if (comment != null && comment.getLikeCount() > 0){
            commentMapper.delLike(commentId);
        }

    }
}
