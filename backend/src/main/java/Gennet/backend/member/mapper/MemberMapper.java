package Gennet.backend.member.mapper;

import Gennet.backend.member.dto.MemberDto;
import Gennet.backend.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {
    Member memberSignUpDtoToMember(MemberDto.SignUpDto signUpDto);
    Member memberUpdateDtoToMember(MemberDto.UpdateDto updateDto);
    MemberDto.UpdateResponseDto memberToUpdateResponseDto(Member member);
    default MemberDto.GetResponseDto memberToGetResponseDto(Member member){
        MemberDto.GetResponseDto getResponseDto =
                MemberDto.GetResponseDto.builder()
                        .introduction(member.getIntroduction())
                        .image(member.getImage())
                        .memberType(member.getMemberType())
                        .name(member.getName())
                        .avgStarRate(member.getAvgStarRate())
                        .dateOfBirth(member.getDateOfBirth())
                        .build();
        return getResponseDto;
    }
}
