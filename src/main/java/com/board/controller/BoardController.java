package com.board.controller;

import com.board.dto.BoardListDto;
import com.board.dto.SearchDto;
import com.board.dto.WriteFormDto;
import com.board.entity.Board;
import com.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

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
}
