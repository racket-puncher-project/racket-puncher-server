package com.example.demo.entity;

import com.example.demo.auth.dto.SignUpDto;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.AuthType;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "SITE_USER")
@Table(name = "SITE_USER")
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class SiteUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PASSWORD", length = 1023, nullable = false)
    private String password;

    @Column(name = "NICKNAME", length = 50, nullable = false)
    private String nickname;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "PHONE_NUMBER", length = 50, nullable = false)
    private String phoneNumber;

    @Column(name = "MANNER_SCORE")
    private Double mannerScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", length = 50, nullable = false)
    private GenderType gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "NTRP")
    private Ntrp ntrp;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "NAME", nullable = false)
    private String siteUserName;

    @Column(name = "ZIP_CODE", length = 50, nullable = false)
    private String zipCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "AGE_GROUP", length = 50, nullable = false)
    private AgeGroup ageGroup;

    @Column(name = "PROFILE_IMG", length = 1023)
    private String profileImg;

    @CreatedDate
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "IS_PHONE_VERIFIED") // 1:true, 0:false, default = 0
    private Boolean isPhoneVerified;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUTH_TYPE", length = 50, nullable = false)
    private AuthType authType;

    @OneToMany(mappedBy = "siteUser")
    private List<Matching> hostedMatches; // 주최한 매칭

    @OneToMany(mappedBy = "siteUser")
    private List<Apply> applies; // 신청 내역

    @OneToMany(mappedBy = "siteUser")
    private List<Notification> notifications; // 알림

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static SiteUser fromDto(SignUpDto signUpDto) {
        return SiteUser.builder()
                .email(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .nickname(signUpDto.getNickname())
                .phoneNumber(signUpDto.getPhoneNumber())
                .gender(signUpDto.getGender())
                .ntrp(signUpDto.getNtrp())
                .address(signUpDto.getAddress())
                .zipCode(signUpDto.getZipCode())
                .ageGroup(signUpDto.getAgeGroup())
                .profileImg(signUpDto.getProfileImg())
                .siteUserName(signUpDto.getSiteUserName())
                .authType(signUpDto.getAuthType())
                .build();
    }
}
