package cn.cncc.community.community.mapper;

import cn.cncc.community.community.model.Question;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface QuestionMapper
{
  @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},#{gmt_create},#{gmt_modified},#{creator},#{tag})")
  public void create(Question question);
  
  @Select("select * from question")
  List<Question> list();
}
