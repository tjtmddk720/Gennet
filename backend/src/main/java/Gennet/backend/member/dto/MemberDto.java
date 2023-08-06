package Gennet.backend.member.dto;

import Gennet.backend.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MemberDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpDto{
        private Member.memberType memberType;
        @Email
        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
                message = "올바른 이메일 구성이 아닙니다.")
        private String email;

        @NotBlank
        @Size(min = 8, message = "비밀번호는 특수문자 포함 8자 이상이어야합니다.")
        private String password;

        @NotBlank
        @Size(min = 8, message = "입력하신 비밀번호와 일치하지 않습니다.")
        private String samePassword;
        @NotBlank
        private String name;
        @NotBlank
        private String dateOfBirth;
        private Member.lifeCategory lifeCategory;
        private String introduction;
        private String image;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CheckEmailDto {
        @Email
        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
                message = "올바른 이메일 구성이 아닙니다.")
        private String email;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GetResponseDto {
        private Member.memberType memberType;
        private float avgStarRate;
        private String name;
        private String image;
        private String dateOfBirth;
        private String introduction;
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateDto {
        private Long memberId;
        private String name;
        private String image;
        private String dateOfBirth;
        private String introduction;

        public void addMemberId(Long memberId){
            this.memberId = memberId;
        }
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateResponseDto {

        private String name;
        private String image;
        private String dateOfBirth;
        private String introduction;
    }
}
