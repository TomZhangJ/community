package cn.cncc.community.community.mapper;

import cn.cncc.community.community.model.Question;
import org.springframework.stereotype.Component;

@Component(value ="questionExtMapper")
public interface QuestionExtMapper
{
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION
     *
     * @mbg.generated Tue Jan 21 16:48:15 CST 2020
     */
    int incView(Question record);
    
    int incCommentCount(Question record);
}