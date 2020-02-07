package cn.cncc.community.community.service;

import cn.cncc.community.community.mapper.UserMapper;
import cn.cncc.community.community.model.User;
import cn.cncc.community.community.model.UserExample;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
  @Autowired(required = false)
  private UserMapper userMapper;
  
  public void createOrUpdate(User user)
  {
    UserExample userExample = new UserExample();
    userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
    List<User> users = userMapper.selectByExample(userExample);
    if(users.size() == 0)
    {
      // 插入新用户
      user.setGmtCreate(System.currentTimeMillis());
      user.setGmtModified(user.getGmtCreate());
      userMapper.insert(user);
    }
    else
    {
      // 更新用户
      User dbUser = users.get(0);
      User updateUser = new User();
      updateUser.setGmtModified(System.currentTimeMillis());
      updateUser.setAvatarUrl(user.getAvatarUrl());
      updateUser.setName(user.getName());
      updateUser.setToken(user.getToken());
      UserExample example = new UserExample();
      example.createCriteria().andIdEqualTo(dbUser.getId());
      userMapper.updateByExampleSelective(updateUser, example);
    }
  }
}
