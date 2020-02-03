package cn.cncc.community.community.service;

import cn.cncc.community.community.dto.PaginationDTO;
import cn.cncc.community.community.dto.QuestionDTO;
import cn.cncc.community.community.exception.CustomizeErrorCode;
import cn.cncc.community.community.exception.CustomizeException;
import cn.cncc.community.community.mapper.QuestionExtMapper;
import cn.cncc.community.community.mapper.QuestionMapper;
import cn.cncc.community.community.mapper.UserMapper;
import cn.cncc.community.community.model.Question;
import cn.cncc.community.community.model.QuestionExample;
import cn.cncc.community.community.model.User;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
  @Autowired(required = false)
  private UserMapper userMapper;
  
  @Autowired(required = false)
  private QuestionMapper questionMapper;
  
  @Autowired(required = false)
  private QuestionExtMapper questionExtMapper;

  // 首页问题列表分页
  public PaginationDTO list(Integer page, Integer size)
  {
    PaginationDTO paginationDTO = new PaginationDTO();
  
    Integer totalPage;
  
    Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
  
    if(totalCount % size == 0)
    {
      totalPage = totalCount / size;
    }
    else
    {
      totalPage = totalCount / size + 1;
    }
  
    if(page < 1)
    {
      page = 1;
    }
  
    if(page > totalPage)
    {
      page = totalPage;
    }
  
    paginationDTO.setPagination(totalPage,page);
    
    Integer offset = size * (page - 1);
    
    List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,size));
    List<QuestionDTO> questionDTOList = new ArrayList<>();
    
    for (Question question : questions)
    {
      User user = userMapper.selectByPrimaryKey(question.getCreator().longValue());
      QuestionDTO questionDTO = new QuestionDTO();

      BeanUtils.copyProperties(question, questionDTO);
      questionDTO.setUser(user);
      questionDTOList.add(questionDTO);
    }
    paginationDTO.setQuestions(questionDTOList);
    
    return paginationDTO;
  }
  
  // 用户问题列表分页
  public PaginationDTO list(Long userId, Integer page, Integer size)
  {
    PaginationDTO paginationDTO = new PaginationDTO();
  
    Integer totalPage;
    
    QuestionExample questionExample = new QuestionExample();
    questionExample.createCriteria().andCreatorEqualTo(userId);
    Integer totalCount = (int) questionMapper.countByExample(questionExample);
    
    if(totalCount % size == 0)
    {
      totalPage = totalCount / size;
    }
    else
    {
      totalPage = totalCount / size + 1;
    }
    
    if(page < 1)
    {
      page = 1;
    }
  
    if(page > totalPage)
    {
      page = totalPage;
    }
  
    paginationDTO.setPagination(totalPage,page);
  
    Integer offset = size * (page - 1);
  
    QuestionExample example = new QuestionExample();
    example.createCriteria().andCreatorEqualTo(userId);
    List<Question> questions = questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
    List<QuestionDTO> questionDTOList = new ArrayList<>();
  
    for (Question question : questions)
    {
      User user = userMapper.selectByPrimaryKey(question.getCreator().longValue());
      QuestionDTO questionDTO = new QuestionDTO();
    
      BeanUtils.copyProperties(question, questionDTO);
      questionDTO.setUser(user);
      questionDTOList.add(questionDTO);
    }
    paginationDTO.setQuestions(questionDTOList);
  
    return paginationDTO;
  }
  
  public QuestionDTO getById(Long id)
  {
    Question question = questionMapper.selectByPrimaryKey(id);
    if(question == null)
    {
      throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
    }
    QuestionDTO questionDTO = new QuestionDTO();
    BeanUtils.copyProperties(question,questionDTO);
    User user = userMapper.selectByPrimaryKey(question.getCreator().longValue());
    questionDTO.setUser(user);
    return questionDTO;
  }
  
  public void createOrUpdate(Question question)
  {
    if(question.getId() == null)
    {
      question.setGmtCreate(System.currentTimeMillis());
      question.setGmtModified(question.getGmtCreate());
      question.setViewCount(0);
      question.setLikeCount(0);
      question.setCommentCount(0);
      questionMapper.insert(question);
    }
    else
    {
      Question updateQuestion = new Question();
      updateQuestion.setGmtModified(System.currentTimeMillis());
      updateQuestion.setTitle(question.getTitle());
      updateQuestion.setDescription(question.getDescription());
      updateQuestion.setTag(question.getTag());
      QuestionExample questionExample = new QuestionExample();
      questionExample.createCriteria().andIdEqualTo(question.getId());
      int updated = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
      if(updated != 1)
      {
        throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
      }
    }
  }
  
  public void incView(Long id)
  {
    Question question = new Question();
    question.setId(id);
    question.setViewCount(1);
    questionExtMapper.incView(question);
  }
}
