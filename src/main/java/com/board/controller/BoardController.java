package com.board.controller;

import com.board.dto.BoardListDto;
import com.board.dto.SearchDto;
import com.board.dto.WriteFormDto;
import com.board.entity.Board;
import com.board.service.BoardService;
import com.board.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    //등록 화면
    @GetMapping(value = "/board/blog/new")
    public String writeForm(Model model) {
        model.addAttribute("writeFormDto", new WriteFormDto());

        return "board/write";
    }

    //등록하기
    @PostMapping(value = "/board/blog/new")
    public String blogNew(@Valid WriteFormDto writeFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "board/write";

        try {
            boardService.saveBlog(writeFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "등록 중 에러가 발생했습니다.");

            return "board/write";
        }

        return "redirect:/";
    }

    //리스트
    @GetMapping(value = {"/board/blog", "/board/blog/{page}"})
    public String blogList(@PathVariable("page") Optional<Integer> page,
                           Principal principal, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<BoardListDto> boardList = boardService.getBoardListPage(pageable);

        model.addAttribute("boardList", boardList);
        model.addAttribute("principal", principal);
        model.addAttribute("maxPage", 5);

        return "board/list";
    }

    //상세 페이지
    @GetMapping(value = "/board/{boardId}")
    public String blogDtl(Model model, @PathVariable("boardId") Long boardId) {
        WriteFormDto writeFormDto = boardService.getBlogDtl(boardId);
        model.addAttribute("board", writeFormDto);

        return "board/blogDtl";
    }

    //수정페이지 보기
    @GetMapping(value = "/board/modify/{boardId}")
    public String blogModify(@PathVariable("boardId") Long boardId, Model model) {
        try {
            WriteFormDto writeFormDto = boardService.getBlogDtl(boardId);
            model.addAttribute("writeFormDto", writeFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "해당 글 정보를 가져오는 도중 에러가 발생했습니다.");

            model.addAttribute("writeFormDto", new WriteFormDto());

            return "board/blogModify"; //수정화면으로 이동
        }

        return "board/blogModify";
    }

    //update
    @PostMapping(value = "/board/modify/{boardId}")
    public String blogUpdate(@Valid WriteFormDto writeFormDto, Model model, BindingResult bindingResult,
                             @PathVariable("boardId") Long boardId) {

        if(bindingResult.hasErrors()) return "board/blogModify";

        WriteFormDto getWriteFormDto = boardService.getBlogDtl(boardId);

        try {
            boardService.updateBlog(writeFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "수정중 에러가 발생했습니다.");
            model.addAttribute("writeFormDto", getWriteFormDto);

            return "board/blogModify";
        }

        return "redirect:/board/blog";
    }

    //delete
    @DeleteMapping("/board/{boardId}/delete")
    public @ResponseBody ResponseEntity deleteBlog(@PathVariable("boardId") Long boardId,
                                                    Principal principal) {

        //1. 본인확인
        if (!boardService.validateBlog(boardId, principal.getName())) {
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        //2. 주문 삭제
        boardService.deleteBlog(boardId);

        return new ResponseEntity<Long>(boardId, HttpStatus.OK);
    }
}
