package Gennet.backend.starrate.repository;

import Gennet.backend.member.entity.Member;
import Gennet.backend.starrate.entity.StarRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface StarRateRepository extends JpaRepository<StarRate, Long> {
    // 사용자의 ID로 별점 목록을 조회하는 메서드 추가
    List<StarRate> findByMemberEmail(String memberEmail);
}
