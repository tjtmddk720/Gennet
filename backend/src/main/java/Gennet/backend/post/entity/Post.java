package Gennet.backend.post.entity;

import Gennet.backend.audit.Auditable;
import Gennet.backend.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String title;
    private String content;
    private String image;
    private int viewCount;
    @Enumerated(value = EnumType.STRING)
    private lifeCategory category;
    @Enumerated(value = EnumType.STRING)
    private  postStatus status = postStatus.RECRUTING;
    @ManyToOne
    @JoinColumn(name = "POST_MEMBER_ID")
    private Member postMember;
    @ManyToOne
    @JoinColumn(name = "MATCHING_MEMBER_ID")
    private Member matchingMember;
    public void addViewCount(int viewCount){
        this.viewCount = viewCount + 1;
    }
    public enum lifeCategory {
        EATING("식사주문"),
        ECONOMIC("경제생활"),
        SOCIAL("일상생활"),
        ETC("기타");

        private String category;

        lifeCategory(String category){
            this.category = category;
        }
        public String getCategory(){
            return category;
        }
    }
    public enum postStatus{
        RECRUTING("모집중"),
        COMPLETE("완료");
        @Getter
        private String status;

        postStatus(String status) {
            this.status = status;
        }
    }
}
