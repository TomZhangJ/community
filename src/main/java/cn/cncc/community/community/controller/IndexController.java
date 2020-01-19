package cn.cncc.community.community.controller;

import cn.cncc.community.community.dto.QuestionDTO;
import cn.cncc.community.community.mapper.UserMapper;
import cn.cncc.community.community.model.User;
import cn.cncc.community.community.service.QuestionService;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController
{
  @Autowired private UserMapper userMapper;
  
  @Autowired private QuestionService questionService;

  @GetMapping("/")
  public String index(HttpServletRequest request, Model model)
  {
    User user = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length != 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("token")) {
          String token = cookie.getValue();
          user = userMapper.findByToken(token);
          if (user != null) {
            request.getSession().setAttribute("user", user);
          }
          break;
        }
      }
    }
  
    List<QuestionDTO> questionList = questionService.list();
    model.addAttribute("questions",questionList);
    
    return "index";
  }
}
