package Gennet.backend.starrate.entity;

import Gennet.backend.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StarRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long starRateId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "RATED_MEMBER_ID")
    private Member ratedMember;

    private Long rating;
//    @ManyToOne
//    @JoinColumn(name = "chattingroom_id")
//    private ChattingRoom chattingRoom;
}
