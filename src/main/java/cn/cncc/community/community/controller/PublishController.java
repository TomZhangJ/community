package cn.cncc.community.community.controller;

import cn.cncc.community.community.cache.TagCache;
import cn.cncc.community.community.dto.QuestionDTO;
import cn.cncc.community.community.model.Question;
import cn.cncc.community.community.model.User;
import cn.cncc.community.community.service.QuestionService;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublishController
{
  @Autowired private QuestionService questionService;

  @GetMapping("/publish/{id}")
  public String edit(@PathVariable(name = "id") Long id, Model model)
  {
    QuestionDTO question = questionService.getById(id);
    model.addAttribute("title", question.getTitle());
    model.addAttribute("description", question.getDescription());
    model.addAttribute("tag", question.getTag());
    model.addAttribute("id", question.getId());
    model.addAttribute("tags", TagCache.get());
    return "publish";
  }
  @GetMapping("/publish")
  public String publish(Model model) {
    model.addAttribute("tags", TagCache.get());
    return "publish";
  }

  @PostMapping("/publish")
  public String doPublish(
      @RequestParam(value = "title", required = false) String title,
      @RequestParam(value = "description", required = false) String description,
      @RequestParam(value = "tag", required = false) String tag,
      @RequestParam(value = "id", required = false) Long id,
      HttpServletRequest request,
      Model model) {
    model.addAttribute("title", title);
    model.addAttribute("description", description);
    model.addAttribute("tag", tag);
    model.addAttribute("tags", TagCache.get());

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
  
    String invalid = TagCache.filterInvalid(tag);
    if (StringUtils.isNoneBlank(invalid))
    {
      model.addAttribute("error","输入非法标签：" + invalid);
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
    question.setId(id);
    questionService.createOrUpdate(question);

    return "redirect:/";
  }
}
