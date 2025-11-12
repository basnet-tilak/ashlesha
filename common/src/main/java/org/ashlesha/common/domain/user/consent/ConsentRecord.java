package org.ashlesha.common.domain.user.consent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ashlesha.common.domain.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "consent_records")
public class ConsentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consentId;

    @Column(length = 128)
    private String consentType;

    @Column(length = 64)
    private String consentVersion;

    private Boolean granted = Boolean.TRUE;

    private LocalDateTime grantedAt = LocalDateTime.now();

    private LocalDateTime revokedAt;

    @Column(length = 256)
    private String proofRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
