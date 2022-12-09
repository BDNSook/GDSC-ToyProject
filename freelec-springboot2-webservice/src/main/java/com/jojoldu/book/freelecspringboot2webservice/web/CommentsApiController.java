package com.jojoldu.book.freelecspringboot2webservice.web;

import com.jojoldu.book.freelecspringboot2webservice.config.auth.LoginUser;
import com.jojoldu.book.freelecspringboot2webservice.config.auth.dto.SessionUser;
import com.jojoldu.book.freelecspringboot2webservice.service.comments.CommentsService;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.CommentsRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class CommentsApiController {
    private final CommentsService commentsService;

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity commentSave(@PathVariable Long id, @RequestBody CommentsRequestDto commentsRequestDto, @LoginUser SessionUser user) {
        return ResponseEntity.ok(commentsService.commentSave(id, commentsRequestDto, user.getUserId()));
    }
}
