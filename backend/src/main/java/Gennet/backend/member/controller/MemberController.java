package Gennet.backend.member.controller;

import Gennet.backend.member.dto.MemberDto;
import Gennet.backend.member.entity.Member;
import Gennet.backend.member.mapper.MemberMapper;
import Gennet.backend.member.service.MemberService;
import Gennet.backend.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/members")
@Validated
@RequiredArgsConstructor
public class MemberController {
    public final static String MEMBER_DEFAULT_URL = "/members";
    public final MemberService memberService;
    public final MemberMapper memberMapper;

    /** 회원 가입 **/
    @PostMapping("/signup")
    public ResponseEntity postMember(@Valid @RequestBody MemberDto.SignUpDto requestbody) {

        Member member = memberService.createMember(requestbody,memberMapper.memberSignUpDtoToMember(requestbody));

        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, member.getMemberId());

        return ResponseEntity.created(location).build();
    }

    /** 이메일 중복 체크 **/
    @PostMapping("/check-email")
    public ResponseEntity checkEmail(@Valid @RequestBody MemberDto.CheckEmailDto requestbody){

        boolean duplicateCheck = memberService.checkEmail(requestbody.getEmail());

        if(duplicateCheck)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        else
            return new ResponseEntity<>(HttpStatus.OK);
    }

    /** 회원 정보 조회 **/
    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@Positive @PathVariable("member-id") Long memberId){

        Member member = memberService.getMember(memberId);

        return new ResponseEntity<>(memberMapper.memberToGetResponseDto(member),HttpStatus.OK);
    }

    /** 회원 정보 수정 **/
    @PatchMapping("/{member-id}/edit")
    public ResponseEntity updateMember(@Positive @PathVariable("member-id") Long memberId,
                                       @RequestBody MemberDto.UpdateDto requestbody){
        requestbody.addMemberId(memberId);
        Member member = memberMapper.memberUpdateDtoToMember(requestbody);

        Member updateMember = memberService.updateMember(member);

        return new ResponseEntity<>(memberMapper.memberToUpdateResponseDto(updateMember),HttpStatus.OK);
    }
}
