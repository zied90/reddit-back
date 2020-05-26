package com.example.devback.service;

import com.example.devback.dto.PostRequest;
import com.example.devback.dto.PostResponse;
import com.example.devback.dto.SubredditDto;
import com.example.devback.exceptions.PostNotFoundException;
import com.example.devback.exceptions.SpringRedditException;
import com.example.devback.exceptions.SubredditNotFoundException;
import com.example.devback.mapper.PostMapper;
import com.example.devback.model.Post;
import com.example.devback.model.Subreddit;
import com.example.devback.repository.PostRepository;
import com.example.devback.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        System.out.println(postMapper.map(postRequest, subreddit));
        postRepository.save(postMapper.map(postRequest, subreddit));

    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        System.out.println("xxxx" + subreddit);
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }
}
