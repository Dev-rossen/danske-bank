package dk.rossen.dbdemo.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "postings"
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "debtor_account_no")
    private String debtorAccountNumber;

    @Column(name = "creditor_account_no")
    private String creditorAccountNumber;

    @Column(name = "amount")
    private float amount;

    @Column(name = "posting_text")
    private String postingText;

    @Column(name = "balance")
    private float balance;
}
