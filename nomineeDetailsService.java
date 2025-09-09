package com.loanorigination.service;

import com.loanorigination.entity.NomineeDetails;
import com.loanorigination.repository.NomineeDetailsRepository;
import com.loanorigination.entity.LoanApplication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class NomineeDetailsService {

    private final NomineeDetailsRepository nomineeRepo;

    @PersistenceContext
    private EntityManager em;

    public NomineeDetailsService(NomineeDetailsRepository nomineeRepo) {
        this.nomineeRepo = nomineeRepo;
    }

    public Optional<NomineeDetails> getByApplicationId(Long applicationId) {
        return nomineeRepo.findByApplicationId(applicationId);
    }

    public NomineeDetails upsertNominee(Long applicationId, NomineeDetails request) {
        LoanApplication appRef = em.getReference(LoanApplication.class, applicationId);

        NomineeDetails nominee = nomineeRepo.findByApplicationId(applicationId)
                .orElseGet(NomineeDetails::new);

        if (nominee.getNomineeId() == null) {
            nominee.setNomineeId(applicationId); // only if no auto-generation
        }

        nominee.setLoanApplication(appRef);
        nominee.setNomineeName(request.getNomineeName());
        nominee.setRelationship(request.getRelationship());
        nominee.setNomineeDob(request.getNomineeDob());
        nominee.setNomineeAddress(request.getNomineeAddress());
        nominee.setNomineePhone(request.getNomineePhone());
        nominee.setNomineeEmail(request.getNomineeEmail());
        nominee.setNomineeAadhaar(request.getNomineeAadhaar());
        nominee.setNomineePan(request.getNomineePan());

        return nomineeRepo.save(nominee);
    }
}
