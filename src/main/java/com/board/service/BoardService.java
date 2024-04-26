package com.board.service;

import com.board.dto.BoardListDto;
import com.board.dto.SearchDto;
import com.board.dto.WriteFormDto;
import com.board.entity.Board;
import com.board.entity.Member;
import com.board.repository.BoardRepository;
import com.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    //insert
    public Long saveBlog(WriteFormDto writeFormDto) throws Exception {
        Board board = writeFormDto.createBoard();

        Member member = memberRepository.findByEmail(getCurrentUserId());
        board.setMember(member);

        boardRepository.save(board);

        return board.getId();
    }

    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";

        if(authentication != null) {
            userId = authentication.getName();
        }

        return userId;
    }

    public Page<BoardListDto> getBoardListPage(SearchDto searchDto, Pageable pageable) {
        Page<BoardListDto> boardListPage =
                boardRepository.getBoardListPage(searchDto, pageable);

        return boardListPage;
    }
}
