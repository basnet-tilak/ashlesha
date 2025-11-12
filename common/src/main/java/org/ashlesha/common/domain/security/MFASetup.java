package org.ashlesha.common.domain.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "mfa_setups")
public class MFASetup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mfaId;

    @Enumerated(EnumType.STRING)
    private MFAType type;

    @Column(length = 256)
    private String secret;

    @Column(length = 512)
    private String backupCodes;

    private Boolean enabled = Boolean.FALSE;

    private LocalDateTime enabledAt;

    private LocalDateTime lastUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
