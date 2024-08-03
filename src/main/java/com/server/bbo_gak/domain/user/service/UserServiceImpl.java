package com.server.bbo_gak.domain.user.service;

import com.server.bbo_gak.domain.auth.dto.response.oauth.OauthUserInfoResponse;
import com.server.bbo_gak.domain.user.entity.OauthInfo;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.domain.user.entity.UserRepository;
import com.server.bbo_gak.domain.user.entity.UserRole;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(OauthUserInfoResponse oauthUserInfo) {
        User user = User.builder()
            .role(UserRole.USER)
            .oauthInfo(OauthInfo.builder()
                .oauthId(oauthUserInfo.oauthId())
                .email(oauthUserInfo.email())
                .name(oauthUserInfo.name())
                .provider(oauthUserInfo.provider()).build()
            ).build();
        userRepository.save(user);
        return user;
    }


    @Override
    public void updateUser() {

    }

    @Override
    public void getUser() {

    }

    @Override
    public void deleteUser() {

    }

    @Override
    public Optional<User> findUserByOauthInfo(OauthInfo oAuthInfo) {
        return userRepository.findUserByOauthInfo(oAuthInfo);
    }
}