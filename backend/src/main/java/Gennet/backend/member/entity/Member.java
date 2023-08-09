package Gennet.backend.member.entity;

import Gennet.backend.audit.Auditable;
import Gennet.backend.post.entity.Post;
import Gennet.backend.starrate.entity.StarRate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;
    private String name;
    @Column(nullable = false)
    private String email;
    private String password;
    private String image;
    private String dateOfBirth;
    private String introduction;
    private float avgStarRate;

    //사용자 등록 시, 사용자의 권한을 등록하기 위한 권한 테이블을 생성
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();
    @Enumerated(value = EnumType.STRING)
    private memberType memberType;
    @Enumerated(value = EnumType.STRING)
    private lifeCategory lifeCategory;
    @OneToMany(mappedBy = "postMember",cascade = CascadeType.REMOVE)
    private List<Post> postMemberPostList = new ArrayList<>();
    @OneToMany(mappedBy = "matchingMember",cascade = CascadeType.REMOVE)
    private List<Post> matchingMemberPostList = new ArrayList<>();
    @OneToMany(mappedBy = "member",cascade = CascadeType.REMOVE)
    private List<StarRate> starRateList = new ArrayList<>();
    public enum memberType{
        SENIOR("시니어"),
        YOUTH("청년");
        private String type;
        memberType(String type) {
            this.type = type;
        }
        public String getType(){
            return type;
        }
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
}
