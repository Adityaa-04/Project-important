package com.loanorigination.repository;

import com.loanorigination.entity.NomineeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NomineeDetailsRepository extends JpaRepository<NomineeDetails, Long> {

    /**
     * Find the nominee row linked to a loan application (1:1 style).
     */
    @Query("SELECT nd FROM NomineeDetails nd WHERE nd.loanApplication.applicationId = :applicationId")
    Optional<NomineeDetails> findByApplicationId(@Param("applicationId") Long applicationId);

    /**
     * Optional additional helper — find by both nomineeId and applicationId (safe lookup).
     */
    @Query("SELECT nd FROM NomineeDetails nd WHERE nd.nomineeId = :nomineeId AND nd.loanApplication.applicationId = :applicationId")
    Optional<NomineeDetails> findByIdAndApplicationId(@Param("nomineeId") Long nomineeId,
                                                      @Param("applicationId") Long applicationId);

    /**
     * You can keep any other repository methods you already had (e.g. findByNomineePan).
     */
}
