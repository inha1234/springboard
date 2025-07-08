package com.springboard.service;

import com.springboard.dto.comment.CommentCreateRequestDto;
import com.springboard.dto.comment.CommentResponseDto;
import com.springboard.dto.comment.CommentUpdateRequestDto;
import com.springboard.entity.Comment;
import com.springboard.entity.Post;
import com.springboard.entity.User;
import com.springboard.exception.custom.comment.*;
import com.springboard.exception.custom.post.PostNotFoundException;
import com.springboard.exception.custom.user.UserNotFoundException;
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

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentCreateRequestDto dto, String username){
        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(()-> new UserNotFoundException());

        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new PostNotFoundException());

        Comment parent = null;
        if (dto.getParentId() != null && dto.getParentId() > 0) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new ParentCommentNotFoundException());
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
                .orElseThrow(() -> new PostNotFoundException());

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
                .orElseThrow(()->new CommentNotFoundException());

        if(comment.isDeleted()){
            throw new CommentAlreadyDeletedException();
        }

        if(!comment.getUser().getUsername().equals(username)){
            throw new CommentUpdateForbiddenException();
        }

        comment.setContent(dto.getContent());
        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String username){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new CommentNotFoundException());

        if(!comment.getUser().getUsername().equals(username)){
            throw new CommentDeleteForbiddenException();
        }

        comment.setDeleted(true);
    }
}
