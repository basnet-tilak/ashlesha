package org.ashlesha.common.domain.security;

public enum MFAType {
    TOTP,
    SMS,
    EMAIL,
    FIDO2,
    RECOVERY_CODE
}
