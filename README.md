- Ashlesha (अश्लेषा) Group:- Strategic investment for transformative education.

project description:-
    step 1: gradle init  
    step 2: create the users-service model
note: there are different build.gradle 
    each module have different build.gradle file 
    and one will be for the root 
# ashlesha

classDiagram

    %% User Core Entity
    class User {
        +Long userId
        +String username
        +String email
        +String passwordHash
        +String passwordSalt
        +LocalDateTime lastPasswordChange
        +int failedLoginAttempts
        +LocalDateTime accountLockedUntil
        +String lockReason
        +Boolean termsAccepted
        +LocalDateTime termsAcceptedAt
        +String preferredLanguage
        +String timezone
        +LocalDateTime lastActivityAt
        +UserStatus status
        +LocalDateTime statusChangedAt
        +String statusChangeReason
        +addRole(Role role)
        +removeRole(Role role)
        +resetPassword(String newPassword)
        +lockAccount(String reason)
        +unlockAccount()
        +enableMFA(MFAType type)
        +disableMFA()
    }        


    %% User Profile Extension
    class UserProfile {
        +Long profileId
        +String title
        +String firstName
        +String middleName
        +String lastName
        +LocalDate dateOfBirth
        +String phoneNumber
        +String mobileNumber
        +Address address
        +String occupation
        +String employer
        +String taxId
        +String nationalId
        +String avatarUrl
        +String bio
        +Map~String,String~ customAttributes
    }

    %% Address Value Object
    class Address {
        +String street1
        +String street2
        +String city
        +String state
        +String postalCode
        +String country
        +AddressType type
    }

    %% Security & Authentication
    class LoginHistory {
        +Long entryId
        +LocalDateTime loginAt
        +String ipAddress
        +String userAgent
        +String deviceFingerprint
        +String location
        +Boolean success
        +String failureReason
    }

    class PasswordHistory {
        +Long historyId
        +String passwordHash
        +String passwordSalt
        +LocalDateTime changedAt
        +String changedBy
    }

    class MFASetup {
        +Long mfaId
        +MFAType type
        +String secret
        +String backupCodes
        +Boolean enabled
        +LocalDateTime enabledAt
        +LocalDateTime lastUsed
    }

    class APIToken {
        +Long tokenId
        +String tokenHash
        +String name
        +String[] scopes
        +LocalDateTime createdAt
        +LocalDateTime lastUsedAt
        +LocalDateTime expiresAt
        +Boolean revoked
    }

    %% KYC & Verification System
    class KYCVerification {
        +Long verificationId
        +KYCStatus status
        +VerificationLevel level
        +String submittedBy
        +Role submittedByRole
        +String reviewedBy
        +Role reviewedByRole
        +String reviewNotes
        +Integer riskScore
        +String riskFactors
        +LocalDateTime submittedAt
        +LocalDateTime reviewedAt
        +LocalDateTime expiryDate
        +VerificationSource source
        +approve(String reviewer, String notes)
        +reject(String reviewer, String notes)
        +requestResubmission(String reviewer, String notes)
    }

    class UserDocument {
        +Long documentId
        +DocumentType type
        +String filePath
        +String originalFilename
        +String fileType
        +Long fileSize
        +String fileHash
        +DocumentStatus status
        +String rejectionReason
        +LocalDateTime uploadedAt
        +LocalDateTime verifiedAt
        +LocalDateTime expiryDate
        +Integer version
        +approve()
        +reject(String reason)
    }

    %% Authorization System
    class Role {
        +Long roleId
        +String name
        +String description
        +Boolean systemRole
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +addPermission(Permission permission)
        +removePermission(Permission permission)
        +assignToUser(User user)
        +revokeFromUser(User user)
    }

    class Permission {
        +Long permissionId
        +String name
        +String description
        +String category
        +String service
    }

    class Policy {
        +Long policyId
        +String name
        +String description
        +String conditions
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
    }

    %% Audit & Compliance
    class AuditLog {
        +Long logId
        +AuditEventType eventType
        +String description
        +String ipAddress
        +String userAgent
        +String affectedResource
        +String oldValues
        +String newValues
        +LocalDateTime occurredAt
        +Boolean success
    }

    class ConsentRecord {
        +Long consentId
        +String consentType
        +String consentVersion
        +Boolean granted
        +LocalDateTime grantedAt
        +LocalDateTime revokedAt
        +String proofRecord
    }

    %% Relationships
    User "1" -- "1" UserProfile : has
    User "1" -- "*" LoginHistory : has
    User "1" -- "*" PasswordHistory : has
    User "1" -- "*" MFASetup : has
    User "1" -- "*" APIToken : has
    User "1" -- "*" KYCVerification : undergoes
    User "1" -- "*" AuditLog : generates
    User "1" -- "*" ConsentRecord : provides
    User "1" -- "*" Role : assigned
    
    UserProfile "1" -- "*" Address : has
    KYCVerification "1" -- "*" UserDocument : contains
    KYCVerification "1" -- "1" User : belongs to
    Role "1" -- "*" Permission : has
    Role "1" -- "*" Policy : has
    
    %% Enumerations
    class UserStatus {
        <<enumeration>>
        PENDING_ACTIVATION
        ACTIVE
        SUSPENDED
        LOCKED
        DEACTIVATED
        DELETED
    }

    class KYCStatus {
        <<enumeration>>
        NOT_STARTED
        PENDING
        UNDER_REVIEW
        APPROVED
        REJECTED
        RESUBMISSION_REQUESTED
        EXPIRED
    }

    class DocumentStatus {
        <<enumeration>>
        PENDING_REVIEW
        APPROVED
        REJECTED
        EXPIRED
    }

    class DocumentType {
        <<enumeration>>
        ID_CARD_FRONT
        ID_CARD_BACK
        PASSPORT
        DRIVER_LICENSE
        UTILITY_BILL
        BANK_STATEMENT
        SELFIE
        TAX_DOCUMENT
        PROOF_OF_INCOME
        PROOF_OF_ADDRESS
    }

    class VerificationLevel {
        <<enumeration>>
        BASIC
        STANDARD
        ENHANCED
    }

    class MFAType {
        <<enumeration>>
        TOTP
        SMS
        EMAIL
        FIDO2
        RECOVERY_CODE
    }

    class VerificationSource {
        <<enumeration>>
        MANUAL
        AUTOMATED
        THIRD_PARTY_PROVIDER
        GOVERNMENT_DATABASE
    }

    class AddressType {
        <<enumeration>>
        HOME
        WORK
        MAILING
        LEGAL
    }

    class AuditEventType {
        <<enumeration>>
        LOGIN_SUCCESS
        LOGIN_FAILURE
        PASSWORD_CHANGE
        PROFILE_UPDATE
        ROLE_ASSIGNMENT
        KYC_SUBMISSION
        DOCUMENT_UPLOAD
        CONSENT_GRANTED
        CONSENT_REVOKED
        ACCOUNT_LOCKED
        ACCOUNT_UNLOCKED
    }