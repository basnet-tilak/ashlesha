package org.ashlesha.common.domain.kyc;

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

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_documents")
public class UserDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    @Column(length = 512)
    private String filePath;

    @Column(length = 256)
    private String originalFilename;

    @Column(length = 64)
    private String fileType;

    private Long fileSize;

    @Column(length = 128)
    private String fileHash;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status = DocumentStatus.PENDING_REVIEW;

    @Column(length = 512)
    private String rejectionReason;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    private LocalDateTime verifiedAt;

    private LocalDateTime expiryDate;

    private Integer version = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verification_id")
    private KYCVerification verification;

    public void approve() {
        this.status = DocumentStatus.APPROVED;
        this.verifiedAt = LocalDateTime.now();
    }

    public void reject(String reason) {
        this.status = DocumentStatus.REJECTED;
        this.rejectionReason = reason;
        this.verifiedAt = LocalDateTime.now();
    }
}
