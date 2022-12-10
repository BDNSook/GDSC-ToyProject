package com.jojoldu.book.freelecspringboot2webservice.web;

import com.jojoldu.book.freelecspringboot2webservice.config.auth.LoginUser;
import com.jojoldu.book.freelecspringboot2webservice.config.auth.dto.SessionUser;
import com.jojoldu.book.freelecspringboot2webservice.domain.user.User;
import com.jojoldu.book.freelecspringboot2webservice.service.posts.PostsService;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.CommentsResponseDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsResponseDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsUpdateResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostsService postsService;

    @GetMapping("/") //URL접속 시 기본페이지(/)에 index 템플릿 매핑
    public String index(Model model, @LoginUser SessionUser user){
        model.addAttribute("posts", postsService.findAllDesc());
        //findAllDesc()로 가져온 결과를 posts 객체로 -> index에 전달

        if (user != null) { //세션에 저장된 값이 있을 경우에만 model에 userName 등록
            model.addAttribute("loginUserName", user.getName());
        }

        return "index"; //머스테치 플러그인 -> 앞 경로 + 뒤 파일 확장자 명 자동 생략 가능
    }

    @GetMapping("/posts/save")//앞 슬래쉬 까먹지 말기
    public String postSave(){
        return "post-save"; //마찬가지로 URL (/posts/save) - 머스태치 파일 매핑
    }

    @GetMapping("/posts/update/{id}")
    public String postUpdate(@PathVariable Long id, @LoginUser SessionUser user, Model model) {
        Long userId = user.getUserId();
        PostsUpdateResDto dto = postsService.findForUpdate(id);
        if (!(postsService.isAuthor(userId, id))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        model.addAttribute("post", dto);

        return "posts-update";
    }

    @GetMapping("/posts/read/{id}")
    public String postRead(@PathVariable Long id, Model model, @LoginUser SessionUser user) {
        if(user != null){
            if (postsService.isAuthor(id, user.getUserId())) {
                model.addAttribute("writer", user.getUserId()); //글 작성자일 경우에만 수정, 삭제버튼 추가
            }
        }
        PostsResponseDto postsResponseDto = postsService.findById(id);
        List<CommentsResponseDto> comments = postsResponseDto.getComments();

        /* 댓글 리스트 */
        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        model.addAttribute("post", postsResponseDto);
        return "posts-read";
    }
}
