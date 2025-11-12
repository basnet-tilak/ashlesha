package org.ashlesha.common.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Address {

    @Size(max = 128)
    @Column(name = "street_1")
    private String street1;

    @Size(max = 128)
    @Column(name = "street_2")
    private String street2;

    @Size(max = 64)
    private String city;

    @Size(max = 64)
    private String state;

    @Size(max = 32)
    @Column(name = "postal_code")
    private String postalCode;

    @NotBlank
    @Size(max = 64)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType type = AddressType.HOME;
}
