package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.PostsDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Posts;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.PostsRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostsService {

    private final PostsRepo postsRepo;

    public PostsService(PostsRepo postsRepo) {
        this.postsRepo = postsRepo;
    }

    public List<PostsDto> getAllPosts() {
        return postsRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<PostsDto> getPostById(UUID postId) {
        return postsRepo.findById(postId).map(this::mapToDto);
    }

    public PostsDto savePost(PostsDto dto) {
        Posts post = mapToEntity(dto);
        post = postsRepo.save(post);
        return mapToDto(post);
    }


    private PostsDto mapToDto(Posts post) {
        PostsDto dto = new PostsDto();
        dto.setPostId(post.getPostId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setImages(post.getImages());
        return dto;
    }


    private Posts mapToEntity(PostsDto dto) {
        Posts post = new Posts();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setImages(dto.getImages());
        return post;
    }
}

