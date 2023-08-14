//package Gennet.backend.starrate.mapper;
//
//import Gennet.backend.starrate.dto.StarRateDto;
//import Gennet.backend.starrate.entity.StarRate;
//import Gennet.backend.member.entity.Member;
////import Gennet.backend.chattingroom.entity.ChattingRoom;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class StarRateMapper {
//    public StarRateDto toDto(StarRate starRate) {
//        StarRateDto dto = new StarRateDto();
//        dto.setStarRateId(starRate.getStarRateId());
//        dto.setMemberId(starRate.getMember().getMemberId());
////        dto.setChattingRoomId(starRate.getChattingRoom().getChattingRoomId());
//        dto.setStarRate(starRate.getStarRate());
//        return dto;
//    }
//
//    public List<StarRateDto> toDtoList(List<StarRate> starRates) {
//        return starRates.stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
//
//    public StarRate toEntity(StarRateDto dto) {
//        StarRate starRate = new StarRate();
//        starRate.setStarRateId(dto.getStarRateId());
//
//        // Member 엔티티 설정
//        Member member = new Member();
//        member.setMemberId(dto.getMemberId()); // dto로부터 받은 값 사용
//        starRate.setMember(member);
////
////        // ChattingRoom 엔티티 설정
////        ChattingRoom chattingRoom = new ChattingRoom();
////        chattingRoom.setChattingRoomId(dto.getChattingRoomId()); // dto로부터 받은 값 사용
////        starRate.setChattingRoom(chattingRoom);
//
//        starRate.setStarRate(dto.getStarRate());
//        return starRate;
//    }
//}
//
