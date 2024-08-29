package com.server.bbo_gak.domain.user.service;

import com.server.bbo_gak.domain.auth.dto.response.oauth.OauthUserInfoResponse;
import com.server.bbo_gak.domain.user.entity.Job;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.domain.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(OauthUserInfoResponse oauthUserInfo) {
        User user = User.from(oauthUserInfo.toEntity());
        userRepository.save(user);

        return user;
    }


    @Override
    public void updateUser() {

    }

    @Transactional
    public void updateUserJob(User user, String job) {
        user.updateJob(Job.findByValue(job));
        userRepository.save(user);
    }

    @Override
    public void getUser() {

    }

    @Override
    public void deleteUser() {

    }

}
