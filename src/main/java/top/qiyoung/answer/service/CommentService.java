package top.qiyoung.answer.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.qiyoung.answer.dto.CommentDTO;
import top.qiyoung.answer.dto.PaginationDTO;
import top.qiyoung.answer.dto.ResultDTO;
import top.qiyoung.answer.enums.CommentTypeEnum;
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

    public PaginationDTO<Comment> getCommentList(Integer currentPage, Integer size, String exerciseId,String userId, String order) {
        PaginationDTO<Comment> paginationDTO = new PaginationDTO<>(currentPage,size);
        /*Query query = new Query();
        query.setIndex();
        query.setSize(size);
        query.setSearch(search);
        query.setOrder(order);*/
        List<Comment> commentList = commentMapper.getCommentList((currentPage-1)*size,size,exerciseId,userId,order);
        paginationDTO.setDataList(commentList);

        /*query.setIndex(null);
        query.setSize(null);*/
        int count = commentMapper.countCommentList(exerciseId,userId);
        paginationDTO.setTotalSize(count);
        paginationDTO.setTotalPage((int)Math.ceil((double) paginationDTO.getTotalSize()/(double)size));
        return paginationDTO;
    }

    public int deleteByCommentId(Integer commentId) {
        return commentMapper.deleteByCommentId(commentId);
    }

    public int countComment() {
        return commentMapper.countCommentList(null,null);
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
        List<Comment> commentList = commentMapper.getCommentListByParentId(parentId,(currentPage-1)*2,2, CommentTypeEnum.FIRST_COMMENT.getType(),"create_time desc");
        int count1 = commentMapper.countCommentListByParentId(parentId, CommentTypeEnum.FIRST_COMMENT.getType());
        for (Comment comment : commentList) {
            CommentDTO commentDTO = new CommentDTO();
            List<Comment> comments = commentMapper.getCommentListByParentId(comment.getCommentId(),0,2,CommentTypeEnum.SECOND_COMMENT.getType(),"create_time");
            if (comments.size() > 0) {
                int count2 = commentMapper.countCommentListByParentId(comment.getCommentId(),CommentTypeEnum.SECOND_COMMENT.getType());
                List<CommentDTO> commentDTOS = new ArrayList<>();
                for (Comment co : comments) {
                    MyUser myUser = userMapper.getUserById(co.getUserId());
                    MyUser receiver = userMapper.getUserById(co.getReceiverId());
                    CommentDTO dto = new CommentDTO();
                    BeanUtils.copyProperties(co,dto);
                    dto.setExercise(exercise);
                    dto.setReceiver(receiver);
                    dto.setMyUser(myUser);
                    commentDTOS.add(dto);
                }
                commentDTO.setPaginationDTO(new PaginationDTO<>(1, 2, (int) Math.ceil((double) count2 / (double) 2), count2, null, null, commentDTOS));
            }
            MyUser myUser = userMapper.getUserById(comment.getUserId());
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setExercise(exercise);
            commentDTO.setMyUser(myUser);
            commentDTOList.add(commentDTO);
        }
        PaginationDTO<CommentDTO> paginationDTO = new PaginationDTO<>(currentPage,2,(int) Math.ceil((double) count1 / (double) 2),count1,null,null,commentDTOList);
        return paginationDTO;
    }

    public ResultDTO addComment(Comment comment, UserDetails userDetails) {
        MyUser myUser = userMapper.findUserByAccount(userDetails.getUsername());
        comment.setUserId(myUser.getUserId());
        comment.setCreateTime(new Date());
        comment.setLikeCount(0);
        ResultDTO resultDTO = ResultDTO.okOf();
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
            comment.setParentId(dbComment.getCommentId());
            if (myUser.getUserId() != user.getUserId()) {
                Notification notification = new Notification(null, myUser.getUserId(), user.getUserId(), dbComment.getParentId(), 2, new Date(), 0, comment.getContent());
                notificationMapper.addNotification(notification);
            }
            commentMapper.addComment(comment);
        }else { // 一级评论
            comment.setType(1);
            Exercise exercise = exerciseService.getExerciseByExerciseId(comment.getParentId());
            if (exercise != null){
                comment.setParentId(exercise.getExerciseId());
                comment.setReceiverId(-1);
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.EXERCISE_NOT_FOUND);
            }
            commentMapper.addComment(comment);
            int count = commentMapper.countCommentListByParentId(comment.getParentId(), CommentTypeEnum.FIRST_COMMENT.getType());
            resultDTO = ResultDTO.okOf((int)Math.ceil((double)count/(double)2));
        }
        return resultDTO;
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
            if (nofiter.getUserId() != receiver.getUserId()) {
                Notification notification = new Notification(null, nofiter.getUserId(), receiver.getUserId(), dbComment.getParentId(), 1, new Date(), 0, comment.getContent());
                notificationMapper.addNotification(notification);
            }
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

    public PaginationDTO<CommentDTO> getSecondComment(Integer commentId, Integer currentPage) {
        List<Comment> commentList = commentMapper.getCommentListByParentId(commentId,(currentPage-1)*2,2,CommentTypeEnum.SECOND_COMMENT.getType(),"create_time");
        Integer count = commentMapper.countCommentListByParentId(commentId,CommentTypeEnum.SECOND_COMMENT.getType());
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            MyUser user = userMapper.getUserById(comment.getUserId());
            MyUser receiver = userMapper.getUserById(comment.getReceiverId());
            commentDTO.setMyUser(user);
            commentDTO.setReceiver(receiver);

            Comment parentComment = commentMapper.getCommentByCommentId(comment.getParentId());
            Exercise exercise = exerciseService.getExerciseByExerciseId(parentComment.getParentId());
            commentDTO.setExercise(exercise);
            commentDTOS.add(commentDTO);
        }
        PaginationDTO paginationDTO = new PaginationDTO(currentPage,2,(int)Math.ceil((double)count/(double)2),count,null,null,commentDTOS);
        return paginationDTO;
    }

    public PaginationDTO<CommentDTO> getNewSecondComment(Integer commentId) {
        Comment comment1 = commentMapper.getCommentByCommentId(commentId);
        while (comment1.getType() != 1){
            comment1 = commentMapper.getCommentByCommentId(comment1.getParentId());
        }
        Integer count = commentMapper.countCommentListByParentId(comment1.getCommentId(),CommentTypeEnum.SECOND_COMMENT.getType());
        List<Comment> commentList = commentMapper.getCommentListByParentId(comment1.getCommentId(),((int)Math.ceil((double)count/(double)2)-1)*2,2,CommentTypeEnum.SECOND_COMMENT.getType(),"create_time");
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            MyUser user = userMapper.getUserById(comment.getUserId());
            MyUser receiver = userMapper.getUserById(comment.getReceiverId());
            commentDTO.setMyUser(user);
            commentDTO.setReceiver(receiver);
            Comment parentComment = commentMapper.getCommentByCommentId(comment.getParentId());
            Exercise exercise = exerciseService.getExerciseByExerciseId(parentComment.getParentId());
            commentDTO.setExercise(exercise);
            commentDTOS.add(commentDTO);
        }
        PaginationDTO paginationDTO = new PaginationDTO((int)Math.ceil((double)count/(double)2),2,(int)Math.ceil((double)count/(double)2),count,null,null,commentDTOS);
        return paginationDTO;
    }

    public Integer getFirstCommentId(Integer commentId) {
        Comment comment = commentMapper.getCommentByCommentId(commentId);
        while (comment.getType() != 1){
            comment = commentMapper.getCommentByCommentId(comment.getParentId());
        }
        return comment.getCommentId();
    }
}
