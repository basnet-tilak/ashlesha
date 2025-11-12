package org.ashlesha.common.domain.kyc;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ashlesha.common.domain.authorization.Role;
import org.ashlesha.common.domain.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "kyc_verifications")
public class KYCVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;

    @Enumerated(EnumType.STRING)
    private KYCStatus status = KYCStatus.NOT_STARTED;

    @Enumerated(EnumType.STRING)
    private VerificationLevel level = VerificationLevel.BASIC;

    @Column(length = 128)
    private String submittedBy;

    @jakarta.persistence.ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by_role_id")
    private Role submittedByRole;

    @Column(length = 128)
    private String reviewedBy;

    @jakarta.persistence.ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_role_id")
    private Role reviewedByRole;

    @Column(length = 1024)
    private String reviewNotes;

    private Integer riskScore;

    @Column(length = 1024)
    private String riskFactors;

    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;

    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    private VerificationSource source = VerificationSource.MANUAL;

    @OneToMany(mappedBy = "verification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDocument> documents = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void approve(String reviewer, String notes) {
        this.status = KYCStatus.APPROVED;
        this.reviewedBy = reviewer;
        this.reviewNotes = notes;
        this.reviewedAt = LocalDateTime.now();
    }

    public void reject(String reviewer, String notes) {
        this.status = KYCStatus.REJECTED;
        this.reviewedBy = reviewer;
        this.reviewNotes = notes;
        this.reviewedAt = LocalDateTime.now();
    }

    public void requestResubmission(String reviewer, String notes) {
        this.status = KYCStatus.RESUBMISSION_REQUESTED;
        this.reviewedBy = reviewer;
        this.reviewNotes = notes;
        this.reviewedAt = LocalDateTime.now();
    }
}
