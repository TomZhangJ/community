package cn.cncc.community.community.dto;

import java.util.List;
import lombok.Data;

@Data
public class TagDTO
{
  private String categoryName;
  private List<String> tags;
}
