package cn.cncc.community.community.service;

import cn.cncc.community.community.enums.CommentTypeEnum;
import cn.cncc.community.community.exception.CustomizeErrorCode;
import cn.cncc.community.community.exception.CustomizeException;
import cn.cncc.community.community.mapper.CommentMapper;
import cn.cncc.community.community.mapper.QuestionExtMapper;
import cn.cncc.community.community.mapper.QuestionMapper;
import cn.cncc.community.community.model.Question;
import cn.cncc.community.community.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService
{
  @Autowired(required = false)
  private CommentMapper commentMapper;
  
  @Autowired(required = false)
  private QuestionMapper questionMapper;
  
  @Autowired
  private QuestionExtMapper questionExtMapper;
  
  @Transactional
  public void insert(Comment comment)
  {
    if (comment.getParentId() == null || comment.getParentId() == 0)
    {
      throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
    }
    
    if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType()))
    {
      throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
    }
  
    if (comment.getType() == CommentTypeEnum.COMMENT.getType())
    {
      // 回复评论
      Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
      if (dbComment == null)
      {
        throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
      }
  
      commentMapper.insert(comment);
    }
    else
    {
      // 回复问题
      Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
      if(question == null)
      {
        throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
      }
  
      commentMapper.insert(comment);        // 需要事务处理
      question.setCommentCount(1);
      questionExtMapper.incCommentCount(question);
    }
  }
}
