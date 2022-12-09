package com.jojoldu.book.freelecspringboot2webservice.web;

import com.jojoldu.book.freelecspringboot2webservice.config.auth.LoginUser;
import com.jojoldu.book.freelecspringboot2webservice.config.auth.dto.SessionUser;
import com.jojoldu.book.freelecspringboot2webservice.service.posts.PostsService;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsListResponseDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsResponseDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostsApiController {

    private final PostsService postsService;

    //게시판 등록 기능
    @PostMapping("/save")
    public Long save(@RequestPart(name = "p") PostsSaveRequestDto requestDto, @RequestPart(name = "f", required = false) MultipartFile file
            ,@LoginUser SessionUser user) throws IOException {
        return postsService.save(requestDto, file, user);
    }

    //게시판 조회 기능 -RestController
    @GetMapping
    public List<PostsListResponseDto> getPostsList(){
        return postsService.findAllDesc();
    }
    //게시판 수정 기능
    @PutMapping("/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto,
                       @LoginUser SessionUser user) {
        return postsService.update(id, requestDto,user);
    }

    //게시판 조회 기능
    @GetMapping("/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);

    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }


}
