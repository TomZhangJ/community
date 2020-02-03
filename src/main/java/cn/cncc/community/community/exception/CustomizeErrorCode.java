package cn.cncc.community.community.exception;

public enum  CustomizeErrorCode implements ICustomizeErrorCode
{
  QUESTION_NOT_FOUND(2001,"你找到问题不在了，要不要换一个试试？"),
  TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复。"),
  NO_LOGIN(2003,"当前操作需要登陆，请登陆后重试。"),
  SYSTEM_ERROR(2004,"服务冒烟了，要不然你稍后再试试！！！"),
  TYPE_PARAM_WRONG(2005,"评论类型错误或不存在。"),
  COMMENT_NOT_FOUND(2006,"回复的评论不存在，要不要换一个试试？");
  
  private String message;
  
  private Integer code;
  
  @Override
  public String getMessage()
  {
    return message;
  }
  
  @Override
  public Integer getCode()
  {
    return code;
  }
  
  CustomizeErrorCode(Integer code, String message)
  {
    this.code = code;
    this.message = message;
  }
}
