package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.PostCreateDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.PostDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.PostService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable UUID id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateDto dto) {
        PostDto saved = postService.savePost(dto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable UUID id, @RequestBody PostCreateDto dto) {
        return postService.updatePost(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable UUID id) {
        postService.deletePostById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllPosts() {
        postService.deleteAllPosts();
        return ResponseEntity.noContent().build();
    }
}