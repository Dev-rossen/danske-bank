package dk.rossen.dbdemo.repository;

import dk.rossen.dbdemo.repository.entity.PostingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostingRepository extends JpaRepository<PostingEntity, Long> {
    Optional<List<PostingEntity>> findPostingsByDebtorAccountNumber(String debtorAccountNumber);
}
