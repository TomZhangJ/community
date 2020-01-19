package cn.cncc.community.community.service;

import cn.cncc.community.community.dto.PaginationDTO;
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
public class QuestionService {
  @Autowired private UserMapper userMapper;
  @Autowired private QuestionMapper questionMapper;

  public PaginationDTO list(Integer page, Integer size)
  {
    PaginationDTO paginationDTO = new PaginationDTO();
    Integer totalCount = questionMapper.count();
    paginationDTO.setPagination(totalCount,page,size);
    
    if(page < 1)
    {
      page = 1;
    }
    
    if(page > paginationDTO.getTotalPage())
    {
      page = paginationDTO.getTotalPage();
    }
    
    Integer offset = size * (page - 1);
    
    List<Question> questions = questionMapper.list(offset,size);
    List<QuestionDTO> questionDTOList = new ArrayList<>();
    
    for (Question question : questions) {
      User user = userMapper.findById(question.getCreator());
      QuestionDTO questionDTO = new QuestionDTO();

      BeanUtils.copyProperties(question, questionDTO);
      questionDTO.setUser(user);
      questionDTOList.add(questionDTO);
    }
    paginationDTO.setQuestions(questionDTOList);
    
    return paginationDTO;
  }
}
