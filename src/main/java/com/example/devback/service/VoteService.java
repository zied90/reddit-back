package com.example.devback.service;

import com.example.devback.dto.VoteDto;
import com.example.devback.exceptions.PostNotFoundException;
import com.example.devback.exceptions.SpringRedditException;
import com.example.devback.model.Post;
import com.example.devback.model.Vote;
import com.example.devback.model.VoteType;
import com.example.devback.repository.PostRepository;
import com.example.devback.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;

    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPost = voteRepository.findTopByPost(post);

        if (voteByPost.isPresent() &&
                voteByPost.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }
        if(VoteType.UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount()+1);
        }else {
            post.setVoteCount(post.getVoteCount()+1);
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }
    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .build();
    }
}
