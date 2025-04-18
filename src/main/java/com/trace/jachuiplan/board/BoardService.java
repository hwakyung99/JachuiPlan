/// 김성현, 이화경
package com.trace.jachuiplan.board;

import com.trace.jachuiplan.DataNotFoundException;
import com.trace.jachuiplan.likes.LikesRepository;
import com.trace.jachuiplan.reply.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/// 김성현
@RequiredArgsConstructor
@Service
public class BoardService {
    @Autowired
    private final BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private LikesRepository likesRepository;

    public Page<Board> getInfoBoards(Pageable pageable) {
        // 최근에 작성된 글부터 정렬 (regdate 필드를 기준으로 내림차순 정렬)
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("regdate")));
        Page<Board> boardPage = boardRepository.findByType('0', sortedPageable);
        // 각 게시글에 댓글 수와 좋아요 수를 추가
        boardPage.forEach(board -> {
            board.setReplyCount(replyRepository.countByBoard(board)); // 댓글 수 계산
            board.setLikeCount(likesRepository.countByBoard(board));   // 좋아요 수 계산
        });
        return boardPage;
    }

    public Page<Board> getGeneralBoards(Pageable pageable) {
        // 최근에 작성된 글부터 정렬 (regdate 필드를 기준으로 내림차순 정렬)
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("regdate")));
        Page<Board> boardPage = boardRepository.findByType('1', sortedPageable);
        // 각 게시글에 댓글 수와 좋아요 수를 추가
        boardPage.forEach(board -> {
            board.setReplyCount(replyRepository.countByBoard(board)); // 댓글 수 계산
            board.setLikeCount(likesRepository.countByBoard(board));   // 좋아요 수 계산
        });

        return boardPage;
    }

    public Page<Board> getQnABoards(Pageable pageable) {
        // 최근에 작성된 글부터 정렬 (regdate 필드를 기준으로 내림차순 정렬)
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("regdate")));
        Page<Board> boardPage = boardRepository.findByType('2', sortedPageable);
        // 각 게시글에 댓글 수와 좋아요 수를 추가
        boardPage.forEach(board -> {
            board.setReplyCount(replyRepository.countByBoard(board)); // 댓글 수 계산
            board.setLikeCount(likesRepository.countByBoard(board));   // 좋아요 수 계산
        });

        return boardPage;
    }

    public void saveBoard(Board board) {
        boardRepository.save(board);  // Board 엔티티를 DB에 저장
    }

    // 게시글 ID로 게시글을 조회하는 메소드
    public Board getBoardById(Long id) {
        // ID로 게시글을 찾음
        return boardRepository.findById(id)
                .orElse(null); // 게시글이 없으면 null 반환
    }

    /// 이화경
    // 조회수 증가
    @Transactional
    public void addViewCount(Board board) {
        board.setViews(board.getViews() + 1);
    }

    // 게시글 삭제
    public Board deleteBoard(Long bno, UserDetails userDetails) throws Exception {
        // 게시글이 있는지 확인
        Optional<Board> board = boardRepository.findById(bno);
        if(board.isEmpty()){
            throw new DataNotFoundException("해당 게시글이 없습니다.");
        }

        // 삭제 권한 확인
        if (!board.get().getUsers().getUsername().equals(userDetails.getUsername()) &&
                userDetails.getAuthorities().stream()
                        .noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }

        boardRepository.delete(board.get());

        return board.get();
    }

    /// 김정은
    // 게시글 수정
    public void modifyBoard(Long bno, Board board, UserDetails userDetails) {
        Board existingBoard = boardRepository.findById(bno)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다: " + bno));

        // 수정 권한 확인(작성자, 관리자)
        if (!existingBoard.getUsers().getUsername().equals(userDetails.getUsername()) &&
                userDetails.getAuthorities().stream()
                        .noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        // 게시글 수정
        existingBoard.setTitle(board.getTitle());
        existingBoard.setContent(board.getContent());
        existingBoard.setUpdatedate(java.time.LocalDateTime.now());

        boardRepository.save(existingBoard);
    }

}
