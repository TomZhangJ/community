package cn.cncc.community.community.interceptor;

import cn.cncc.community.community.mapper.UserMapper;
import cn.cncc.community.community.model.User;
import cn.cncc.community.community.model.UserExample;
import cn.cncc.community.community.service.NotificationService;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Service
public class SessionInterceptor implements HandlerInterceptor {
  @Autowired(required = false)
  private UserMapper userMapper;
  
  @Autowired
  private NotificationService notificationService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length != 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("token")) {
          String token = cookie.getValue();
          UserExample userExample = new UserExample();
          userExample.createCriteria().andTokenEqualTo(token);
          List<User> users = userMapper.selectByExample(userExample);
          if (users.size() != 0) {
            request.getSession().setAttribute("user", users.get(0));
            Long unreadCount = notificationService.unreadCount(users.get(0).getId());
            request.getSession().setAttribute("unreadCount",unreadCount);
          }
          break;
        }
      }
    }
    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {}

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {}
}
