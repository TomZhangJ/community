package cn.cncc.community.community.service;

import cn.cncc.community.community.dto.QuestionDTO;
import cn.cncc.community.community.mapper.QuestionMapper;
import cn.cncc.community.community.mapper.UserMapper;
import cn.cncc.community.community.model.Question;
import cn.cncc.community.community.model.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService
{
  @Autowired private UserMapper userMapper;
  @Autowired private QuestionMapper questionMapper;
  
  public List<QuestionDTO> list()
  {
  List<Question> questions = questionMapper.list();
  List<QuestionDTO> questionDTOList = new ArrayList<>();
  for (Question question:questions)
  {
    User user = userMapper.findById(question.getCreator());
    QuestionDTO questionDTO = new QuestionDTO();
    
    BeanUtils.copyProperties(question,questionDTO);
    questionDTO.setUser(user);
    questionDTOList.add(questionDTO);
  }
  return questionDTOList;
  }
}
