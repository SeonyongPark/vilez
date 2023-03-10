package kr.co.vilez.oauth.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String id;
    private String nickName;
    private String account;
    private String path;
}
