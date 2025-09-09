package com.loanorigination.service;

import com.loanorigination.entity.LoanApplication;
import com.loanorigination.entity.NomineeDetails;
import com.loanorigination.repository.NomineeDetailsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Create or update the nominee row for the given applicationId.
     * Uses em.getReference(LoanApplication.class, applicationId) to set the FK.
     *
     * If the NomineeDetails row does not exist, this creates a new entity and
     * (if nomineeId is null) sets nomineeId = applicationId — this preserves the
     * 1:1 mapping used in your project.
     */
    public NomineeDetails upsertNominee(Long applicationId, NomineeDetails req) {
        // avoid loading full LoanApplication; get a reference for FK
        LoanApplication appRef = em.getReference(LoanApplication.class, applicationId);

        NomineeDetails nominee = nomineeRepo.findByApplicationId(applicationId).orElseGet(NomineeDetails::new);

        // If your entity doesn't auto-generate nomineeId, use applicationId as default PK for a simple 1:1 mapping
        if (nominee.getNomineeId() == null) {
            nominee.setNomineeId(applicationId);
        }

        nominee.setLoanApplication(appRef);
        nominee.setNomineeName(req.getNomineeName());
        nominee.setRelationship(req.getRelationship());
        nominee.setNomineeDob(req.getNomineeDob());
        nominee.setNomineeAddress(req.getNomineeAddress());
        nominee.setNomineePhone(req.getNomineePhone());
        nominee.setNomineeEmail(req.getNomineeEmail());
        nominee.setNomineeAadhaar(req.getNomineeAadhaar());
        nominee.setNomineePan(req.getNomineePan());

        return nomineeRepo.save(nominee);
    }

    /**
     * Delete nominee row for the application (if exists).
     */
    public void deleteByApplicationId(Long applicationId) {
        nomineeRepo.findByApplicationId(applicationId).ifPresent(nomineeRepo::delete);
    }
}
