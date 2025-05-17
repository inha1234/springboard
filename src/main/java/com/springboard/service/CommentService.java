package com.springboard.service;

import com.springboard.dto.comment.CommentRequestDto;
import com.springboard.dto.comment.CommentResponseDto;
import com.springboard.dto.comment.CommentUpdateRequestDto;
import com.springboard.entity.Comment;
import com.springboard.entity.Post;
import com.springboard.entity.User;
import com.springboard.repository.CommentRepository;
import com.springboard.repository.PostRepository;
import com.springboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentResponseDto createComment(Long postId, CommentRequestDto dto, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Comment parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
        }

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setUser(user);
        comment.setPost(post);
        comment.setParent(parent);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public List<CommentResponseDto> getComment(Long postId){
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않거나 삭제된 게시글입니다."));

        List<Comment> comments = commentRepository.findByPost(post);

        Map<Long, CommentResponseDto> dtoMap = new HashMap<>();
        List<CommentResponseDto> roots = new ArrayList<>();

        for(Comment comment : comments){
            CommentResponseDto dto = new CommentResponseDto(comment);
            dtoMap.put(dto.getId(), dto);
        }

        for(Comment comment : comments){
            CommentResponseDto dto = dtoMap.get(comment.getId());
            if(comment.getParent() != null){
                CommentResponseDto parentDto = dtoMap.get(comment.getParent().getId());
                parentDto.addChild(dto);
            } else {
                roots.add(dto);
            }
        }

        return roots;
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentUpdateRequestDto dto, String username){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if(comment.isDeleted()){
            throw new IllegalStateException("삭제된 댓글은 수정할 수 없습니다.");
        }

        if(!comment.getUser().getUsername().equals(username)){
            throw new IllegalArgumentException("작성자만 댓글을 수정할 수 있습니다.");
        }

        comment.setContent(dto.getContent());
        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String username){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if(!comment.getUser().getUsername().equals(username)){
            throw new IllegalArgumentException("작성자만 댓글을 삭제할 수 있습니다.");
        }

        comment.setDeleted(true);
    }
}
