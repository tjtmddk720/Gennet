package Gennet.backend.post.service;

import Gennet.backend.exception.BusinessLogicException;
import Gennet.backend.exception.ExceptionCode;
import Gennet.backend.member.entity.Member;
import Gennet.backend.member.repository.MemberRepository;
import Gennet.backend.post.dto.PostDto;
import Gennet.backend.post.entity.Post;
import Gennet.backend.post.mapper.PostMapper;
import Gennet.backend.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final MemberRepository memberRepository;

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.memberRepository = memberRepository; // MemberRepository 주입
    }

    /**
     * 모든 게시물 가져오기
     *
     * @return 게시물 목록
     */
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 특정 ID의 게시물 가져오기
     *
     * @param postId 게시물 ID
     * @return 게시물 정보
     */
    public PostDto getPostById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        return postOptional.map(postMapper::toDto).orElse(null);
    }

    /**
     * 게시물 생성
     *
     * @param postDto 게시물 DTO
     * @return 생성된 게시물 정보
     */
    public PostDto createPost(PostDto postDto, Member currentMember) {
        Post post = postMapper.toEntity(postDto);
        post.setPostMember(currentMember);

        Post savedPost = postRepository.save(post);
        return postMapper.toDto(savedPost);
    }
    /**
     * 게시물 업데이트
     *
     * @param postId         게시물 ID
     * @param updatedPostDto 업데이트할 게시물 DTO
     * @return 업데이트된 게시물 정보
     */
    public PostDto updatePost(Long postId, PostDto updatedPostDto) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get(); // 수정된 부분: postOptional.get()을 post 변수에 할당

            post = postMapper.updateFromDto(updatedPostDto, post); // 수정된 부분: post를 업데이트

            Post updatedPost = postRepository.save(post);
            return postMapper.toDto(updatedPost);
        }
        return null;
    }

    /**
     * 게시물 삭제
     *
     * @param postId 게시물 ID
     */
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
}
