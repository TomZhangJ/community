package cn.cncc.community.community.controller;

import cn.cncc.community.community.mapper.QuestionMapper;
import cn.cncc.community.community.mapper.UserMapper;
import cn.cncc.community.community.model.Question;
import cn.cncc.community.community.model.User;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublishController {
  @Autowired private QuestionMapper questionMapper;

  @Autowired private UserMapper userMapper;

  @GetMapping("/publish")
  public String publish() {
    return "publish";
  }

  @PostMapping("/publish")
  public String doPublish(
      @RequestParam(value = "title", required = false) String title,
      @RequestParam(value = "description", required = false) String description,
      @RequestParam(value = "tag", required = false) String tag,
      HttpServletRequest request,
      Model model) {
    model.addAttribute("title", title);
    model.addAttribute("description", description);
    model.addAttribute("tag", tag);

    if ((title == null) || (title == "")) {
      model.addAttribute("error", "标题不能为空");
      return "publish";
    }

    if ((description == null) || (description == "")) {
      model.addAttribute("error", "问题补充不能为空");
      return "publish";
    }
  
    if ((tag == null) || (tag == "")) {
      model.addAttribute("error", "标签不能为空");
      return "publish";
    }
  
    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
      model.addAttribute("error", "用户未登录");
      return "publish";
    }

    Question question = new Question();
    question.setTitle(title);
    question.setDescription(description);
    question.setTag(tag);
    question.setCreator(user.getId());
    question.setGmtCreate(System.currentTimeMillis());
    question.setGmtModified(question.getGmtCreate());
    questionMapper.create(question);

    return "redirect:/";
  }
}
