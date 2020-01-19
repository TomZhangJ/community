package cn.cncc.community.community.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    @Value("img/headpic2.png")
    private String avatarUrl;
}
