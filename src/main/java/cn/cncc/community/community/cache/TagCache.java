package cn.cncc.community.community.cache;

import cn.cncc.community.community.dto.TagDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class TagCache
{
  public static List<TagDTO> get()
  {
    ArrayList<TagDTO> tagDTOS = new ArrayList<>();
    TagDTO program = new TagDTO();
    program.setCategoryName("开发语言");
    program.setTags(Arrays.asList("js","php","css","html","java","node.js","python","javascript","c++"));
    tagDTOS.add(program);
  
    TagDTO framwork = new TagDTO();
    framwork.setCategoryName("平台框架");
    framwork.setTags(Arrays.asList("laravel","express","django","flask","koa","struts","tornado","ruby-on-rails","spring","yii"));
    tagDTOS.add(framwork);
  
    TagDTO server = new TagDTO();
    server.setCategoryName("服务器");
    server.setTags(Arrays.asList("linux","apache","nginx","tomcat","docker","ubuntu","centos","windows-server","unix","hadoop","负载均衡"));
    tagDTOS.add(server);
    
    return tagDTOS;
  }
  
  public static String filterInvalid(String tags)
  {
    String[] split = StringUtils.split(tags, ",");
    List<TagDTO> tagDTOS = get();
    List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
    String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
  
    return invalid;
  }
}
