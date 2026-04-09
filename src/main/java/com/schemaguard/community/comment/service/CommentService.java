package com.schemaguard.community.comment.service;

import com.schemaguard.community.comment.dto.CommentCreateRequest;
import com.schemaguard.community.comment.dto.CommentResponse;
import com.schemaguard.community.comment.entity.Comment;
import com.schemaguard.community.comment.repository.CommentRepository;
import com.schemaguard.community.post.entity.Post;
import com.schemaguard.community.post.repository.PostRepository;
import com.schemaguard.community.user.entity.User;
import com.schemaguard.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public List<CommentResponse> getCommentsByPostId(Long postId, String loginUserEmail) {
    	Long loginUserId = (loginUserEmail != null)
    	        ? userService.getByEmail(loginUserEmail).getId()
    	        : null;

        return commentRepository.findByPostIdOrderByIdAsc(postId)
                .stream()
                .map(comment -> CommentResponse.from(comment, loginUserId))
                .toList();
    }

    @Transactional
    public Long createComment(Long postId, CommentCreateRequest request, String loginUserEmail) {
        Post post = postRepository.findWithAuthorAndCategoryById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        User author = userService.getByEmail(loginUserEmail);

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .author(author)
                .createdAt(LocalDateTime.now())
                .build();

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public void deleteComment(Long commentId, String loginUserEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        User loginUser = userService.getByEmail(loginUserEmail);

        if (!comment.getAuthor().getId().equals(loginUser.getId())) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public void deleteAllByPostId(Long postId) {
        commentRepository.deleteByPostId(postId);
    }
}