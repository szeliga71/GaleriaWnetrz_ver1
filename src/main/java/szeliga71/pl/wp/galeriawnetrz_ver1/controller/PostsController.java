package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.PostsDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.PostsService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping
    public List<PostsDto> getAllPosts() {
        return postsService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostsDto> getPostById(@PathVariable UUID id) {
        return postsService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PostsDto> createPost(@RequestBody PostsDto dto) {
        PostsDto saved = postsService.savePost(dto);
        return ResponseEntity.ok(saved);
    }
}
