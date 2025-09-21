package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import java.util.List;
import java.util.UUID;

public class PostsDto {

    private UUID postId;
    private String title;
    private List<String> content;
    private List<String> images;


    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}

