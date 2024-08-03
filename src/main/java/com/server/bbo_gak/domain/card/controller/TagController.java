package com.server.bbo_gak.domain.card.controller;

import com.server.bbo_gak.domain.card.dto.response.TagGetResponse;
import com.server.bbo_gak.domain.card.service.TagService;
import com.server.bbo_gak.domain.user.entity.User;
import com.server.bbo_gak.global.annotation.AuthUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/tags")
    public ResponseEntity<List<TagGetResponse>> getAllTagList() {
        return ResponseEntity.ok(tagService.getAllTagList());
    }

    @GetMapping("/cards/{card-id}/tags")
    public ResponseEntity<List<TagGetResponse>> getcCardTagList(
        @AuthUser User user,
        @PathVariable("card-id") Long cardId) {

        return ResponseEntity.ok(tagService.getCardTagList(user, cardId));
    }

    @PostMapping("/cards/{card-id}/tag/{tag-id}")
    public ResponseEntity<Void> addCardTag(
        @AuthUser User user,
        @PathVariable("card-id") Long cardId, @PathVariable("tag-id") Long tagId) {

        tagService.addCardTag(user, cardId, tagId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/cards/{card-id}/tag/{tag-id}")
    public ResponseEntity<Void> deleteCardTag(
        @AuthUser User user,
        @PathVariable("card-id") Long cardId, @PathVariable("tag-id") Long tagId
    ) {

        tagService.deleteCardTag(user, cardId, tagId);
        return ResponseEntity.ok(null);
    }


}
