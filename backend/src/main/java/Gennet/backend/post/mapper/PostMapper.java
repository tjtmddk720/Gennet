package Gennet.backend.post.mapper;

import Gennet.backend.member.entity.Member;
import Gennet.backend.post.dto.PostDto;
import Gennet.backend.post.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostDto toDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setPostId(post.getPostId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setImage(post.getImage());
        postDto.setLifeCategory(post.getCategory().getCategory()); // enum 값을 String으로 변환
        postDto.setViewCount(post.getViewCount());
        postDto.setPostMemberId(post.getPostMember().getMemberId());
        postDto.setMatchingMemberId(post.getMatchingMember().getMemberId());
        postDto.setPostStatus(post.getStatus().getStatus()); // enum 값을 String으로 변환
        postDto.setCreated_at(post.getCreatedAt().toString());
        postDto.setModified_at(post.getModifiedAt().toString());
        return postDto;
    }

    public Post toEntity(PostDto postDto) {
        Post post = new Post();
        post.setPostId(postDto.getPostId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImage(postDto.getImage());
        post.setCategory(Post.lifeCategory.valueOf(postDto.getLifeCategory())); // String 값을 enum으로 변환
        post.setViewCount(postDto.getViewCount());
        // Member 엔티티 설정
        Member postMember = new Member();
        postMember.setMemberId(postDto.getPostMemberId()); // postDto로부터 받은 값 사용
        post.setPostMember(postMember);

        // matchingMemberId 값이 있는 경우에만 설정
        if (postDto.getMatchingMemberId() != null) {
            Member matchingMember = new Member();
            matchingMember.setMemberId(postDto.getMatchingMemberId()); // postDto로부터 받은 값 사용
            post.setMatchingMember(matchingMember);
        }


        post.setStatus(Post.postStatus.valueOf(postDto.getPostStatus())); // String 값을 enum으로 변환
        // created_at 및 modified_at은 엔티티 생성 시 자동 설정됨
        return post;
    }
    public Post updateFromDto(PostDto postDto, Post post) {
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImage(postDto.getImage());
        post.setCategory(Post.lifeCategory.valueOf(postDto.getLifeCategory()));
        return post;
    }

}