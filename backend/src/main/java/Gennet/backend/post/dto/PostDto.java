package Gennet.backend.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private String image;
    private String lifeCategory;
    private int viewCount;
    private Long postMemberId;
    private Long matchingMemberId;
    private String postStatus;
    private String created_at;
    private String modified_at;

    public PostDto(Long postId, String title, String content, String image, String lifeCategory,
                   int viewCount, Long postMemberId, Long matchingMemberId, String postStatus,
                   String created_at, String modified_at) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.image = image;
        this.lifeCategory = lifeCategory;
        this.viewCount = viewCount;
        this.postMemberId = postMemberId;
        this.matchingMemberId = matchingMemberId;
        this.postStatus = postStatus;
        this.created_at = created_at;
        this.modified_at = modified_at;
    }
}


