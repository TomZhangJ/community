package cn.cncc.community.community.controller;

import cn.cncc.community.community.dto.PaginationDTO;
import cn.cncc.community.community.mapper.UserMapper;
import cn.cncc.community.community.model.User;
import cn.cncc.community.community.service.QuestionService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController
{
  @Autowired private UserMapper userMapper;
  
  @Autowired private QuestionService questionService;

  @GetMapping("/")
  public String index(HttpServletRequest request, Model model,
                      @RequestParam(name = "page",defaultValue = "1") Integer page,
                      @RequestParam(name = "size",defaultValue = "2") Integer size)
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
  
    PaginationDTO pagination = questionService.list(page,size);
    model.addAttribute("pagination",pagination);
    
    return "index";
  }
}
