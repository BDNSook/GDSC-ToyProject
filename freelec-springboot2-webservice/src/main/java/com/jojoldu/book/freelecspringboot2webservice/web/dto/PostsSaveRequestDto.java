package com.jojoldu.book.freelecspringboot2webservice.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;

    @Builder
    public PostsSaveRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
 /*       this.author = author;*/
    }

    /*public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .image(null)
                .build();
    }*/

}
