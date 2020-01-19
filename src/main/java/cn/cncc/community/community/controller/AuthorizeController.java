package cn.cncc.community.community.controller;

import cn.cncc.community.community.dto.AccessTokenDTO;
import cn.cncc.community.community.dto.GithubUser;
import cn.cncc.community.community.mapper.UserMapper;
import cn.cncc.community.community.model.User;
import cn.cncc.community.community.provider.GithubProvider;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
  @Autowired private GithubProvider githubProvider;

  @Value("${github.client.id}")
  private String clientId;

  @Value("${github.client.secret}")
  private String clientSecret;

  @Value("${github.redirect.uri}")
  private String redirectUri;

  @Autowired(required = false)
  private UserMapper userMapper;

  @GetMapping("/callback")
  public String callback(
      @RequestParam(name = "code") String code,
      @RequestParam(name = "state") String state,
      HttpServletResponse response) {
    AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
    accessTokenDTO.setClient_id(clientId);
    accessTokenDTO.setClient_secret(clientSecret);
    accessTokenDTO.setCode(code);
    accessTokenDTO.setRedirect_uri(redirectUri);
    accessTokenDTO.setState(state);
    String accessToken = githubProvider.getAccessToken(accessTokenDTO);
    GithubUser githubUser = githubProvider.getUser(accessToken);

    if (githubUser != null && githubUser.getId() != null)
    {
      User user = new User();
      String token = UUID.randomUUID().toString();
      user.setToken(token);
      user.setName(githubUser.getName());
      user.setAccountId(String.valueOf(githubUser.getId()));
      user.setGmtCreate(System.currentTimeMillis());
      user.setGmtModified(user.getGmtCreate());
      user.setAvatarUrl(githubUser.getAvatarUrl());
      userMapper.insert(user);

      // 登录成功，写cookie 和 session
      // 记录Cookie
      response.addCookie(new Cookie("token", token));
    } else {
      // 登录失败，重新登录
    }
    return "redirect:/";
    //        return "index";
  }
}
