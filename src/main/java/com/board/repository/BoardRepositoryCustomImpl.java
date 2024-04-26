package com.board.repository;

import com.board.dto.BoardListDto;
import com.board.dto.QBoardListDto;
import com.board.dto.SearchDto;
import com.board.entity.QBoard;
import com.board.entity.QMember;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public BoardRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardListDto> getBoardListPage(SearchDto SearchDto, Pageable pageable) {
        /*
          select board.id, board.title, member.name, board.reg_date, board.update_date
          from board, member
          where member.member_id = board.member_id
          order by board.board_id desc;
        */

        QBoard board = QBoard.board;
        QMember member = QMember.member;

        List<BoardListDto> content = queryFactory
                .select(
                        new QBoardListDto(
                                board.id,
                                board.title,
                                member.name,
                                board.regDate,
                                board.updateDate                                )
                ).from(board)
                .join(board.member, member)
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(board)
                .join(board.member, member)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
