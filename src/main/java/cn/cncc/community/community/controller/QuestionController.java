package cn.cncc.community.community.controller;

import cn.cncc.community.community.dto.CommentDTO;
import cn.cncc.community.community.dto.QuestionDTO;
import cn.cncc.community.community.service.CommentService;
import cn.cncc.community.community.service.QuestionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController
{
  @Autowired
  private QuestionService questionService;
  
  @Autowired
  private CommentService commentService;
  
  @GetMapping("/question/{id}")
  public String question(@PathVariable(name = "id") Long id,
                         Model model)
  {
    QuestionDTO questionDTO = questionService.getById(id);
  
    List<CommentDTO> comments = commentService.listByQuestionId(id);
    
    // 累加阅读数
    questionService.incView(id);
    model.addAttribute("question",questionDTO);
    model.addAttribute("comments",comments);
    return "question";
  }
}
