package org.deltacore.delta.model.user;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.model.GeneralData;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@Entity(name = "user_delta")
@NoArgsConstructor
public class User extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @ElementCollection
    private List<Email> emails;

    @Transient
    private String email;

    @Column(nullable = false, length = 70)
    private String passwordHash;

    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userMonitor")
    private Monitor monitor;

    /*
    Esta rotina será usada pelo mapstruct toda
    vez que esta entidade for instanciada por ele.
    */
    public void setEmail(String email) {
        this.email = email;
        if (email != null && !email.isEmpty()) {
            String[] parts = email.split("@", 2);
            Email newEmail = Email
                    .builder()
                    .userMail(parts[0])
                    .domain(parts[1])
                    .isVerified(false)
                    .isMain(true)
                    .build();
            this.emails = List.of(newEmail);
        } else {
            this.emails = List.of();
        }
    }

    public String getEmail() {
        if (emails != null && !emails.isEmpty())
            return emails.stream()
                .filter(Email::getIsMain)
                .map(email -> email.getUserMail() + "@" + email.getDomain())
                .findFirst()
                .orElse(null);

        return null;
    }
}
