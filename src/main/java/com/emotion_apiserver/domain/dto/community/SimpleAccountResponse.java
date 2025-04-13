package com.emotion_apiserver.domain.dto.community;


import com.emotion_apiserver.domain.account.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleAccountResponse {

    private Long id;
    private String nickname;

    public SimpleAccountResponse(Account account) {
        this.id = account.getId();
        this.nickname = account.getNickname();
    }
}
