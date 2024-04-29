package com.board.repository;

import com.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    //select * from board where board_id = ?;
//    List<Board> findByBoardId(Long boardId);
}
