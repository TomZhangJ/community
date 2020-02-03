package cn.cncc.community.community.service;

import cn.cncc.community.community.enums.TcommentTypeEnum;
import cn.cncc.community.community.exception.CustomizeErrorCode;
import cn.cncc.community.community.exception.CustomizeException;
import cn.cncc.community.community.mapper.QuestionExtMapper;
import cn.cncc.community.community.mapper.QuestionMapper;
import cn.cncc.community.community.mapper.TcommentMapper;
import cn.cncc.community.community.model.Question;
import cn.cncc.community.community.model.Tcomment;
import org.springframework.beans.factory.annotation.Autowired;

public class TcommentService
{
  @Autowired
  private TcommentMapper tcommentMapper;
  
  @Autowired
  private QuestionMapper questionMapper;
  
  @Autowired
  private QuestionExtMapper questionExtMapper;
  
  public void insert(Tcomment tcomment)
  {
    if (tcomment.getParentId() == null || tcomment.getParentId() == 0)
    {
      throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
    }
    
    if (tcomment.getType() == null || TcommentTypeEnum.isExist(tcomment.getType()))
    {
      throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
    }
  
    if (tcomment.getType() == TcommentTypeEnum.COMMENT.getType())
    {
      // 回复评论
      Tcomment dbTcomment = tcommentMapper.selectByPrimaryKey(tcomment.getParentId());
      if (dbTcomment == null)
      {
        throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
      }
  
      tcommentMapper.insert(tcomment);
    }
    else
    {
      // 回复问题
      Question question = questionMapper.selectByPrimaryKey(tcomment.getParentId());
      if(question == null)
      {
        throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
      }
  
      tcommentMapper.insert(tcomment);
      question.setCommentCount(1);
      questionExtMapper.incCommentCount(question);
    }
  }
}
