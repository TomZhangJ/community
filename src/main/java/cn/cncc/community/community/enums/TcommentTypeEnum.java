package cn.cncc.community.community.enums;

public enum TcommentTypeEnum
{
  QUESTION(1),
  COMMENT(2);
  private Integer type;
  
  public static boolean isExist(Integer type)
  {
    for (TcommentTypeEnum tcommentTypeEnum : TcommentTypeEnum.values())
    {
      if (tcommentTypeEnum.getType() == type)
      {
        return true;
      }
    }
    return false;
  }
  
  public Integer getType()
  {
    return type;
  }

  TcommentTypeEnum(Integer type)
  {
    this.type = type;
  }
}
