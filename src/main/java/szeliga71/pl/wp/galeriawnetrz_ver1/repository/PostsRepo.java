package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Posts;

import java.util.UUID;

public interface PostsRepo extends JpaRepository<Posts, UUID> {
}
