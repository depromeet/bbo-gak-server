package com.server.bbo_gak.domain.user.controller;

import com.server.bbo_gak.domain.user.dto.request.UserJobUpdateRequest;
import com.server.bbo_gak.domain.user.dto.request.UserOnboardStatusUpdateRequest;
import com.server.bbo_gak.domain.user.dto.response.UserInfoResponse;
import com.server.bbo_gak.domain.user.dto.response.UserOnboardStatusGetResponse;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.domain.user.service.UserService;
import com.server.bbo_gak.global.annotation.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> memberInfo() {
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/job")
    public ResponseEntity<UserInfoResponse> updateMemberJob(
        @AuthUser User user,
        @RequestBody UserJobUpdateRequest request
    ) {
        userService.updateUserJob(user, request.job());
        return ResponseEntity.ok(null);
    }

    @GetMapping("/onboard-status")
    public ResponseEntity<UserOnboardStatusGetResponse> getMemberOnboardStatus(
        @AuthUser User user
    ) {
        return ResponseEntity.ok(userService.getUserOnboardStatus(user));
    }

    @PutMapping("/onboard-status")
    public ResponseEntity<Void> updateMemberOnboardStatus(
        @AuthUser User user,
        @RequestBody UserOnboardStatusUpdateRequest request
    ) {
        userService.updateUserOnboardStatus(user, request.onboardStatus());
        return ResponseEntity.ok(null);
    }

}
