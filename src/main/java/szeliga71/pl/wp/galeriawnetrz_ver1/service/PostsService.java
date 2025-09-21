package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.PostsDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Posts;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.PostsRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
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
        return postsRepo.findById(postId)
                .map(this::mapToDto);
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
        dto.setContent(splitText(post.getContent(), 100));
        dto.setImages(new ArrayList<>(post.getImages()));
        return dto;
    }


    private Posts mapToEntity(PostsDto dto) {
        Posts post = new Posts();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent() != null ? String.join("", dto.getContent()) : null);
        post.setImages(dto.getImages());
        return post;
    }

    private List<String> splitText(String text, int size) {
        if (text == null) return null;
        List<String> parts = new ArrayList<>();
        for (int i = 0; i < text.length(); i += size) {
            parts.add(text.substring(i, Math.min(text.length(), i + size)));
        }
        return parts;
    }

}

