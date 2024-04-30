package com.board.entity;

import com.board.dto.WriteFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
public class Board extends BaseEntity {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    private LocalDateTime regDate;

    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    //엔티티 수정
    public void updateBoard(WriteFormDto writeFormDto) {
        this.title = writeFormDto.getTitle();
        this.content = writeFormDto.getContent();
    }
}
