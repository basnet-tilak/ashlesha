package org.ashlesha.common.domain.security;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ashlesha.common.domain.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "api_tokens")
public class APIToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @Column(length = 256)
    private String tokenHash;

    @Column(length = 128)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "scope")
    private List<String> scopes = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime lastUsedAt;

    private LocalDateTime expiresAt;

    private Boolean revoked = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
