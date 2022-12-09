package com.jojoldu.book.freelecspringboot2webservice.service.posts;


import com.jojoldu.book.freelecspringboot2webservice.config.auth.dto.SessionUser;
import com.jojoldu.book.freelecspringboot2webservice.domain.posts.PostRepository;
import com.jojoldu.book.freelecspringboot2webservice.domain.posts.Posts;
import com.jojoldu.book.freelecspringboot2webservice.domain.user.User;
import com.jojoldu.book.freelecspringboot2webservice.domain.user.UserRepository;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsListResponseDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsResponseDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.h2.mvstore.FreeSpaceBitSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto, MultipartFile file ,SessionUser user) throws IOException {
        User author = userRepository.getById(user.getUserId());
        //이미지 저장할 파일 경로 생성
        String storePath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images";
        File storeFolder = new File(storePath);
        //헤당 폴더 없는 경우에만 생성
        if (!storeFolder.exists()) {
            try {
                storeFolder.mkdirs();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        //고유 식별자 생성
        UUID uuid = UUID.randomUUID();
        //파일명 생성
        String fileName = null;
        if (file != null) { //파일을 첨부된 경우만 이름 꺼냄
            fileName = uuid + "_"+ file.getOriginalFilename();
            file.transferTo(new File(storePath, fileName));
        }

        return postRepository.save(Posts.builder()
                                    .title(requestDto.getTitle())
                                    .content(requestDto.getContent())
                                    .user(author)
                                    .image(fileName)
                                    .build()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto, SessionUser user) {
        Posts posts = postRepository.findById(id) //수정할 객체 id로 찾아서
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        /*//requestDto 내용으로 title, content 수정
        posts.update(requestDto.getTitle(), requestDto.getContent());*/
        //2.해당 id 연관된 image 객체의 path확인 -> null이거나 같지 않은 경우 posts로 객체 조회 후

        //해당 data id 반환
        return id;
    }

    public PostsResponseDto findById (Long id) {
        Posts entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        //찾은 엔티티 data Dto에 담아 반환 (view에 뿌릴거니까 Dto)
        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postRepository.findById(id) //id로 해당 data 찾기
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));
                //게시글 없을 경우 예외처리
        postRepository.delete(posts);
    }

    //글 수정할 때 작성자인지 확인
    public Boolean isAuthor(Long userId, Long postId){
        Posts post = postRepository.findById(postId)
                .orElseThrow(()-> new NullPointerException());
        if (userId.equals(post.getAuthor().getId())) {
            //System.out.println("1열람자"+userId+"     1글 작성자: "+post.getAuthor().getId());
            return true;
        }
        //System.out.println("2열람자"+userId+"    2글 작성자: "+post.getAuthor().getId());
        return false;
    }
}
