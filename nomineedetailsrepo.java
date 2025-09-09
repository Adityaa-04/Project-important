import com.loanorigination.entity.NomineeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NomineeDetailsRepository extends JpaRepository<NomineeDetails, Long> {
    List<NomineeDetails> findByApplicationId(Long applicationId);
    Optional<NomineeDetails> findByIdAndApplicationId(Long id, Long applicationId);
    void deleteByIdAndApplicationId(Long id, Long applicationId);
}
