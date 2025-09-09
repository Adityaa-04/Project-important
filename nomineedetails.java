package com.loanorigination.repository;

import com.loanorigination.entity.NomineeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NomineeDetailsRepository extends JpaRepository<NomineeDetails, Long> {

    @Query("SELECT nd FROM NomineeDetails nd WHERE nd.loanApplication.applicationId = :applicationId")
    Optional<NomineeDetails> findByApplicationId(@Param("applicationId") Long applicationId);

}
