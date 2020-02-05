package cn.cncc.community.community.controller;

import cn.cncc.community.community.dto.CommentDTO;
import cn.cncc.community.community.dto.ResultDTO;
import cn.cncc.community.community.exception.CustomizeErrorCode;
import cn.cncc.community.community.model.Comment;
import cn.cncc.community.community.model.User;
import cn.cncc.community.community.service.CommentService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommentController
{
  @Autowired
  private CommentService commentService;
  
  @ResponseBody
  @RequestMapping(value = "/comment",method = RequestMethod.POST)
  public Object post(@RequestBody CommentDTO commentDTO, HttpServletRequest request)
  {
    User user = (User) request.getSession().getAttribute("user");
    if(user == null)
    {
      return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
    }
    Comment comment = new Comment();
    comment.setParentId(commentDTO.getParentId());
    comment.setContent(commentDTO.getContent());
    comment.setType(commentDTO.getType());
    comment.setGmtModified(System.currentTimeMillis());
    comment.setGmtCreate(System.currentTimeMillis());
    comment.setCommentator(1L);
    comment.setLikeCount(0L);
    commentService.insert(comment);
    return ResultDTO.okOf();
  }
}
