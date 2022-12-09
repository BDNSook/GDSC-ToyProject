package com.jojoldu.book.freelecspringboot2webservice.domain.posts;

import com.jojoldu.book.freelecspringboot2webservice.domain.BaseTimeEntity;
import com.jojoldu.book.freelecspringboot2webservice.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.*;

//*어노테이션 순서 - 주요 어노테이션을 클래스와 가깝게

@Getter //lombok
@NoArgsConstructor //lombok
@Entity //<< 주요 어노테이션
public class Posts extends BaseTimeEntity {
    //실제 DB table과 매칭 될 클래스 == Entity 클래스

    //id 정의
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @Column
    private String filePath;

    @Column
    private String fileName;




    @Builder
    public Posts(String title, String content, User user, String filePath, String fileName) {
        this.title = title;
        this.content = content;
        this.author = user;
        this.filePath = filePath;
        this.fileName = fileName;
    }


    public void update_content(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public void update_File(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;

    }

}
