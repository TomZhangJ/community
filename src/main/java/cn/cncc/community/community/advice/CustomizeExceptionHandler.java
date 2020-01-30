package cn.cncc.community.community.advice;

import cn.cncc.community.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomizeExceptionHandler
{
  @ExceptionHandler(Exception.class)
  ModelAndView handle(Throwable e, Model model) {
//    HttpStatus status = getStatus(request);
    if (e instanceof CustomizeException)
    {
      model.addAttribute("message",e.getMessage());
    }
    else
    {
      model.addAttribute("message","服务冒烟了，稍后再重试。");
    }
    return new ModelAndView("error");
  }
  
//  private HttpStatus getStatus(HttpServletRequest request) {
//    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//    if (statusCode == null) {
//      return HttpStatus.INTERNAL_SERVER_ERROR;
//    }
//    return HttpStatus.valueOf(statusCode);
//  }
}
