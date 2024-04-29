package com.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardListDto {

    private Long id;
    private String title;
    private String name;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    @QueryProjection
    public BoardListDto(Long id, String title, String name,
                        LocalDateTime regTime, LocalDateTime updateTime) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }
}
