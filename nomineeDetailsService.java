import com.loanorigination.entity.NomineeDetails;
import com.loanorigination.repository.NomineeDetailsRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NomineeDetailsService {

    private final NomineeDetailsRepository nomineeRepo;
    private final EntityManager em;

    public NomineeDetailsService(NomineeDetailsRepository nomineeRepo, EntityManager em) {
        this.nomineeRepo = nomineeRepo;
        this.em = em;
    }

    // READ all
    public List<NomineeDetails> getAllByApplicationId(Long applicationId) {
        return nomineeRepo.findByApplicationId(applicationId);
    }

    // READ one
    public Optional<NomineeDetails> getOne(Long applicationId, Long nomineeId) {
        return nomineeRepo.findByIdAndApplicationId(nomineeId, applicationId);
    }

    // CREATE
    public NomineeDetails createNominee(Long applicationId, NomineeDetails nominee) {
        nominee.setApplicationId(applicationId); // make sure entity has field `applicationId`
        return nomineeRepo.save(nominee);
    }

    // UPDATE
    public NomineeDetails updateNominee(Long applicationId, Long nomineeId, NomineeDetails nominee) {
        NomineeDetails existing = nomineeRepo.findByIdAndApplicationId(nomineeId, applicationId)
                .orElseThrow(() -> new RuntimeException("Nominee not found"));
        existing.setNomineeName(nominee.getNomineeName());
        existing.setRelationship(nominee.getRelationship());
        existing.setNomineeDob(nominee.getNomineeDob());
        existing.setNomineeAddress(nominee.getNomineeAddress());
        existing.setNomineePhone(nominee.getNomineePhone());
        existing.setNomineeEmail(nominee.getNomineeEmail());
        existing.setNomineeAadhaar(nominee.getNomineeAadhaar());
        existing.setNomineePan(nominee.getNomineePan());
        return nomineeRepo.save(existing);
    }

    // DELETE
    public void deleteNominee(Long applicationId, Long nomineeId) {
        nomineeRepo.deleteByIdAndApplicationId(nomineeId, applicationId);
    }
}
