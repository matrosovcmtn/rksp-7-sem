package service;

import model.Post;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.PostRepository;

import java.time.LocalDateTime;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Mono<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Flux<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Mono<Post> createPost(Post post) {
        post.setDate(LocalDateTime.now());
        return postRepository.save(post);
    }

    public Mono<Post> updatePost(Long id, Post post) {
        return postRepository.findById(id)
                .switchIfEmpty(Mono.error(new Exception("Post not found")))
                .flatMap(existingPost -> {
                    existingPost.setTitle(post.getTitle());
                    existingPost.setContent(post.getContent());
                    existingPost.setAuthor(post.getAuthor());
                    existingPost.setDate(LocalDateTime.now());
                    return postRepository.save(existingPost);
                })
                .onErrorResume(e -> {
                    // Логирование ошибки
                    return Mono.error(new RuntimeException("Update failed", e));
                });
    }

    public Mono<Void> deletePost(Long id) {
        return postRepository.deleteById(id);
    }

    public Flux<Post> getPostsByAuthor(String author) {
        return postRepository.findByAuthor(author);
    }
}
