package cn.cncc.community.community.controller;

import cn.cncc.community.community.dto.ResultDTO;
import cn.cncc.community.community.dto.TcommentDTO;
import cn.cncc.community.community.exception.CustomizeErrorCode;
import cn.cncc.community.community.model.Tcomment;
import cn.cncc.community.community.model.User;
import cn.cncc.community.community.service.TcommentService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TcommentController
{
  @Autowired(required = false)
  private TcommentService tcommentService;
  
  @ResponseBody
  @RequestMapping(value = "/comment",method = RequestMethod.POST)
  public Object post(@RequestBody TcommentDTO tcommentDTO, HttpServletRequest request)
  {
    User user = (User) request.getSession().getAttribute("user");
    if(user == null)
    {
      return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
    }
    Tcomment tcomment = new Tcomment();
    tcomment.setParentId(tcommentDTO.getParentId());
    tcomment.setContent(tcommentDTO.getContent());
    tcomment.setType(tcommentDTO.getType());
    tcomment.setGmtModified(System.currentTimeMillis());
    tcomment.setGmtCreate(System.currentTimeMillis());
    tcomment.setCommentator(user.getId());
    tcomment.setLikeCount(0L);
    tcommentService.insert(tcomment);
    return ResultDTO.okOf();
  }
}
