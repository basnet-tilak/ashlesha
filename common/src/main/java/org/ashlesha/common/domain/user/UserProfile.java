package org.ashlesha.common.domain.user;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Size(max = 32)
    private String title;

    @Size(max = 64)
    private String firstName;

    @Size(max = 64)
    private String middleName;

    @Size(max = 64)
    private String lastName;

    @Past
    private LocalDate dateOfBirth;

    @Size(max = 32)
    private String phoneNumber;

    @Size(max = 32)
    private String mobileNumber;

    @ElementCollection
    @CollectionTable(name = "user_addresses", joinColumns = @JoinColumn(name = "profile_id"))
    private List<Address> addresses = new ArrayList<>();

    @Size(max = 128)
    private String occupation;

    @Size(max = 128)
    private String employer;

    @Size(max = 64)
    private String taxId;

    @Size(max = 64)
    private String nationalId;

    @Size(max = 256)
    private String avatarUrl;

    @Size(max = 512)
    private String bio;

    @ElementCollection
    @CollectionTable(name = "user_profile_attributes", joinColumns = @JoinColumn(name = "profile_id"))
    @MapKeyColumn(name = "attribute_key")
    @Column(name = "attribute_value", length = 512)
    private Map<String, String> customAttributes = new HashMap<>();

    public String getFullName() {
        return java.util.stream.Stream.of(firstName, middleName, lastName)
                .filter(value -> value != null && !value.isBlank())
                .collect(java.util.stream.Collectors.joining(" "));
    }
}
