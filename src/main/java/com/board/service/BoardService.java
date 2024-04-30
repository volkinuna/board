package com.board.service;

import com.board.dto.BoardListDto;
import com.board.dto.SearchDto;
import com.board.dto.WriteFormDto;
import com.board.entity.Board;
import com.board.entity.Member;
import com.board.repository.BoardRepository;
import com.board.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    public Page<BoardListDto> getBoardListPage(Pageable pageable) {
        Page<BoardListDto> boardListPage =
                boardRepository.getBoardListPage(pageable);

        return boardListPage;
    }

    @Transactional(readOnly = true)
    public WriteFormDto getBlogDtl(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

        WriteFormDto writeFormDto = WriteFormDto.of(board);

        writeFormDto.setName(board.getMember().getName());
        writeFormDto.setRegDate(board.getRegTime());

        return writeFormDto;
    }

    //update
    public Long updateBlog(WriteFormDto writeFormDto) throws Exception {

        Board board = boardRepository.findById(writeFormDto.getId()).orElseThrow(EntityNotFoundException::new);

        //update 실행
        board.updateBoard(writeFormDto);

        return board.getId();
    }

    @Transactional(readOnly = true)
    public boolean validateBlog(Long BoardId, String email) {

        Member curMember = memberRepository.findByEmail(email);

        Board board = boardRepository.findById(BoardId)
                .orElseThrow(EntityNotFoundException::new);

        Member savedMember = board.getMember();

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }

        return true;
    }

    //delete
    public void deleteBlog(Long BoardId) {
        Board board = boardRepository.findById(BoardId)
                .orElseThrow(EntityNotFoundException::new);

        boardRepository.delete(board); //deletes
    }
}
