package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.PostCreateDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.PostDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Post;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.PostRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepo postRepo;

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        return postRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PostDto> getPostById(UUID id) {
        return postRepo.findById(id).map(this::mapToDto);
    }

    @Transactional
    public PostDto savePost(PostCreateDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setCoverImageUrl(dto.getCoverImageUrl());
        post.setContent(dto.getContent());
        post.setImages(dto.getImages());

        return mapToDto(postRepo.save(post));
    }

    @Transactional
    public Optional<PostDto> updatePost(UUID id, PostCreateDto dto) {
        return postRepo.findById(id).map(existing -> {
            existing.setTitle(dto.getTitle());
            existing.setCoverImageUrl(dto.getCoverImageUrl());
            existing.setContent(dto.getContent());
            existing.setImages(dto.getImages());
            return mapToDto(postRepo.save(existing));
        });
    }

    @Transactional
    public Optional<PostDto> patchPost(UUID id, PostCreateDto dto) {
        return postRepo.findById(id).map(existing -> {
            if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
            if (dto.getCoverImageUrl() != null) existing.setCoverImageUrl(dto.getCoverImageUrl());
            if (dto.getContent() != null) existing.setContent(dto.getContent());
            if (dto.getImages() != null) existing.setImages(dto.getImages());
            return mapToDto(postRepo.save(existing));
        });
    }

    public void deletePostById(UUID id) {
        postRepo.deleteById(id);
    }

    public void deleteAllPosts() {
        postRepo.deleteAll();
    }

    // --- Helper Methods ---

    private PostDto mapToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setPostId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setCoverImageUrl(post.getCoverImageUrl());
        dto.setContent(splitText(post.getContent(), 100)); // Logika dzielenia tekstu
        dto.setImages(post.getImages() != null ? new ArrayList<>(post.getImages()) : new ArrayList<>());
        return dto;
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