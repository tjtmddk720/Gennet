package Gennet.backend.member.service;

import Gennet.backend.auth.utils.CustomAuthorityUtils;
import Gennet.backend.exception.BusinessLogicException;
import Gennet.backend.exception.ExceptionCode;
import Gennet.backend.member.dto.MemberDto;
import Gennet.backend.member.entity.Member;
import Gennet.backend.member.repository.MemberRepository;
import Gennet.backend.utils.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CustomBeanUtils<Member> beanUtils;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    /** 회원 가입 **/
    public Member createMember(MemberDto.SignUpDto requestbody,Member member){

        if(!requestbody.getPassword().equals(requestbody.getSamePassword()))
            throw new BusinessLogicException(ExceptionCode.PASSWORD_NOT_SAME);

        // Password 암호화
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        // DB에 User Role 저장
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        return memberRepository.save(member);
    }

    /** 이메일 중복 체크 **/
    public boolean checkEmail(String email){

        boolean duplicateCheck= memberRepository.existsByEmail(email);

        return duplicateCheck;
    }

    /** 회원 정보 조회 **/
    public Member getMember(Long memberId){

        return findVerifiedMember(memberId);
    }

    /** 회원 정보 수정 **/
    public Member updateMember(Member member){

        Member findMember = findVerifiedMember(member.getMemberId());

        Member updatedMember = beanUtils.copyNonNullProperties(member,findMember);

        return memberRepository.save(updatedMember);

    }
    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember =
                memberRepository.findById(memberId);
        return optionalMember.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

    }
}
