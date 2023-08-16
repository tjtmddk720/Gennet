package Gennet.backend.starrate.mapper;

import Gennet.backend.member.entity.Member;
import Gennet.backend.starrate.dto.StarRateDto;
import Gennet.backend.starrate.entity.StarRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StarRateMapper {
    StarRateMapper INSTANCE = Mappers.getMapper(StarRateMapper.class);

    @Mapping(target = "member", expression = "java(toMember(starRateDto.getMemberId()))")
    @Mapping(target = "ratedMember", expression = "java(toMember(starRateDto.getRatedMemberId()))")
    StarRate starRateDtoToEntity(StarRateDto starRateDto);

    default Member toMember(Long memberId) {
        if (memberId == null) {
            return null;
        }
        Member member = new Member();
        member.setMemberId(memberId);
        return member;
    }

//        // ChattingRoom 엔티티 설정
//        ChattingRoom chattingRoom = new ChattingRoom();
//        chattingRoom.setChattingRoomId(dto.getChattingRoomId()); // dto로부터 받은 값 사용
//        starRate.setChattingRoom(chattingRoom);
}
