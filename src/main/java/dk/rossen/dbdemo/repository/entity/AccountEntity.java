package dk.rossen.dbdemo.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = @UniqueConstraint(columnNames = "account_no")
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "account_no")
    private String accountNumber;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "balance")
    private float balance;

    @Column(name = "customer_id")
    private String customerId;

}
