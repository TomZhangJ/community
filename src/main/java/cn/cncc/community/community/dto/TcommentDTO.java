package cn.cncc.community.community.dto;

import lombok.Data;

@Data
public class TcommentDTO
{
  private Long parentId;
  private String content;
  private Integer type;
}
