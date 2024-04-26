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
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    @QueryProjection
    public BoardListDto(Long id, String title, String name,
                        LocalDateTime regDate, LocalDateTime updateDate) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.regDate = regDate;
        this.updateDate = updateDate;
    }
}
