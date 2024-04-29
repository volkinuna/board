package com.board.repository;

import com.board.dto.BoardListDto;
import com.board.dto.SearchDto;
import com.board.dto.WriteFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<BoardListDto> getBoardListPage(Pageable pageable);
}
