package Gennet.backend.auth.repository;

import Gennet.backend.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AuthRepository extends JpaRepository<RefreshToken,Long> {
    RefreshToken findRefreshTokenByMemberId(Long memberId);

    @Transactional
    void deleteRefreshTokenByMemberId(Long memberId);
}
