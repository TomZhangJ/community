package cn.cncc.community.community.service;

import cn.cncc.community.community.dto.CommentDTO;
import cn.cncc.community.community.enums.CommentTypeEnum;
import cn.cncc.community.community.enums.NotificationTypeEnum;
import cn.cncc.community.community.enums.NotificationStatusEnum;
import cn.cncc.community.community.exception.CustomizeErrorCode;
import cn.cncc.community.community.exception.CustomizeException;
import cn.cncc.community.community.mapper.CommentExtMapper;
import cn.cncc.community.community.mapper.CommentMapper;
import cn.cncc.community.community.mapper.NotificationMapper;
import cn.cncc.community.community.mapper.QuestionExtMapper;
import cn.cncc.community.community.mapper.QuestionMapper;
import cn.cncc.community.community.mapper.UserMapper;
import cn.cncc.community.community.model.Comment;
import cn.cncc.community.community.model.CommentExample;
import cn.cncc.community.community.model.Notification;
import cn.cncc.community.community.model.Question;
import cn.cncc.community.community.model.User;
import cn.cncc.community.community.model.UserExample;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
  @Autowired(required = false)
  private CommentMapper commentMapper;

  @Autowired(required = false)
  private QuestionMapper questionMapper;

  @Autowired private QuestionExtMapper questionExtMapper;

  @Autowired(required = false)
  private UserMapper userMapper;

  @Autowired(required = false)
  private CommentExtMapper commentExtMapper;

  @Autowired(required = false)
  private NotificationMapper notificationMapper;

  @Transactional
  public void insert(Comment comment,User commentator) {
    if (comment.getParentId() == null || comment.getParentId() == 0) {
      throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
    }
    if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
      throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
    }
    if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
      // 回复评论
      Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
      if (dbComment == null) {
        throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
      }
  
      // 回复问题
      Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
      if (question == null) {
        throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
      }
      
      commentMapper.insert(comment);

      // 增加评论数
      Comment parentComment = new Comment();
      parentComment.setId(comment.getParentId());
      parentComment.setCommentCount(1);
      commentExtMapper.incCommentCount(parentComment);
      
      // 创建通知
      createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
    } else {
      // 回复问题
      Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
      if (question == null) {
        throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
      }
      comment.setCommentCount(0);
      commentMapper.insert(comment); // 需要事务处理
      question.setCommentCount(1);
      questionExtMapper.incCommentCount(question);
  
      // 创建通知
      createNotify(comment,question.getCreator(), commentator.getName(),question.getTitle(),NotificationTypeEnum.REPLY_QUESTION, question.getId());
    }
  }
  
  private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
    if (receiver == comment.getCommentator()){
      return;
    }
    
    Notification notification = new Notification();
    notification.setGmtCreate(System.currentTimeMillis());
    notification.setType(notificationType.getType());
    notification.setOuterid(outerId);
    notification.setNotifier(comment.getCommentator());
    notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
    notification.setReceiver(receiver);
    notification.setNotifierName(notifierName);
    notification.setOuterTitle(outerTitle);
    notificationMapper.insert(notification);
  }
  
  public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
    CommentExample commentExample = new CommentExample();
    commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
    commentExample.setOrderByClause("gmt_create desc");
    List<Comment> comments = commentMapper.selectByExample(commentExample);

    if (comments.size() == 0) {
      return new ArrayList<>();
    }

    // 获取去重的评论人
    Set<Long> commentators =
        comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
    List<Long> userIds = new ArrayList();
    userIds.addAll(commentators);

    // 获取评论人并转换为 Map
    UserExample userExample = new UserExample();
    userExample.createCriteria().andIdIn(userIds);
    List<User> users = userMapper.selectByExample(userExample);
    Map<Long, User> userMap =
        users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

    List<CommentDTO> commentDTOS =
        comments.stream()
            .map(
                comment -> {
                  CommentDTO commentDTO = new CommentDTO();
                  BeanUtils.copyProperties(comment, commentDTO);
                  commentDTO.setUser(userMap.get(comment.getCommentator()));
                  return commentDTO;
                })
            .collect(Collectors.toList());

    return commentDTOS;
  }
}
