package com.board.repository;

import com.board.dto.BoardListDto;
import com.board.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<BoardListDto> getBoardListPage(SearchDto SearchDto, Pageable pageable);
}
