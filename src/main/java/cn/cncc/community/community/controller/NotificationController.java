package cn.cncc.community.community.controller;

import cn.cncc.community.community.dto.NotificationDTO;
import cn.cncc.community.community.enums.NotificationTypeEnum;
import cn.cncc.community.community.model.User;
import cn.cncc.community.community.service.NotificationService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NotificationController {
  
  @Autowired
  private NotificationService notificationService;
  
  @GetMapping("/notification/{id}")
  public String profile(@PathVariable(name = "id") Long id,
      Model model,
      HttpServletRequest request){
    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
      return "redirect:/";
    }
  
    NotificationDTO notificationDTO = notificationService.read(id,user);
    
    if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
        || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){
      return "redirect:/question/" + notificationDTO.getOuterid();
    }
    else{
      return "redirect:/";
    }
  }
}
