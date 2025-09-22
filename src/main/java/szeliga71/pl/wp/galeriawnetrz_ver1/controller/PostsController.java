package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.PostsDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.PostsRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.PostsService;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    @Autowired
    private final PostsService postsService;
    private final PostsRepo postsRepo;

    public PostsController(PostsService postsService,PostsRepo postsRepo) {
        this.postsService = postsService;
        this.postsRepo = postsRepo;
    }

    @GetMapping
    public List<PostsDto> getAllPosts() {
        return postsService.getAllPosts();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PostsDto> getPostById(@PathVariable UUID id) {
        return postsService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<PostsDto> createPost(@RequestBody PostsDto dto) {
        PostsDto saved = postsService.savePost(dto);
        return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllPosts() {
        postsService.deleteAllPosts();
        return ResponseEntity.noContent().build(); // HTTP 204
    }
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable UUID id) {
        postsService.deletePostById(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<PostsDto> updatePost(@PathVariable UUID id, @RequestBody PostsDto dto) {
        return postsService.updatePost(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<PostsDto> patchPost(@PathVariable UUID id, @RequestBody PostsDto dto) {
        return postsService.patchPost(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
