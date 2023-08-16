package Gennet.backend.post.controller;

import Gennet.backend.member.entity.Member;
import Gennet.backend.member.repository.MemberRepository;
import Gennet.backend.post.dto.PostDto;
import Gennet.backend.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final MemberRepository memberRepository;

    @Autowired
    public PostController(PostService postService, MemberRepository memberRepository) {
        this.postService = postService;
        this.memberRepository = memberRepository;
    }

    /**
     * 모든 게시물 가져오기
     *
     * @return 게시물 목록
     */
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> postDtos = postService.getAllPosts();
        return ResponseEntity.ok(postDtos);
    }

    /**
     * 특정 ID의 게시물 가져오기
     *
     * @param postId 게시물 ID
     * @return 게시물 정보
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        PostDto postDto = postService.getPostById(postId);
        if (postDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(postDto);
    }

    /**
     * 게시물 생성
     *
     * @param postDto 게시물 DTO
     * @return 생성된 게시물 정보
     */
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // 현재 로그인된 회원의 이메일

        Optional<Member> currentMemberOptional = memberRepository.findByEmail(userEmail);
        if (currentMemberOptional.isPresent()) {
            PostDto createdPostDto = postService.createPost(postDto, currentMemberOptional.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPostDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    /**
     * 게시물 업데이트
     *
     * @param postId         게시물 ID
     * @param updatedPostDto 업데이트할 게시물 DTO
     * @return 업데이트된 게시물 정보
     */
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @RequestBody PostDto updatedPostDto) {
        PostDto updatedPost = postService.updatePost(postId, updatedPostDto);
        if (updatedPost == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPost);
    }

    /**
     * 게시물 삭제
     *
     * @param postId 게시물 ID
     * @return 삭제 결과
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
