package cn.cncc.community.community.interceptor;

import cn.cncc.community.community.mapper.UserMapper;
import cn.cncc.community.community.model.User;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Service
public class SessionInterceptor implements HandlerInterceptor
{
  @Autowired
  private UserMapper userMapper;
  
  @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
  {
    User user = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length != 0)
    {
      for (Cookie cookie : cookies)
      {
        if (cookie.getName().equals("token"))
        {
          String token = cookie.getValue();
          user = userMapper.findByToken(token);
          if (user != null)
          {
            request.getSession().setAttribute("user", user);
          }
          break;
        }
      }
    }
    return true;
  }
  
  @Override public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
  {
  
  }
  
  @Override public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
  {
  
  }
}
