package com.example.demo.entity;

import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
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

    //@ElementCollection
    //@Enumerated(EnumType.STRING)
    @Convert(converter = SiteUserRoleConverter.class)
    private List<String> roles;
    //private List<Authority> roles;

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

    public Double getMannerScore() {
        return mannerScore;
    }

    public void setMannerScore(Double mannerScore) {
        this.mannerScore = mannerScore;
    }

    @Column(name = "PENALTY_SCORE")
    private Integer penaltyScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", length = 50, nullable = false)
    private GenderType gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "NTRP")
    private Ntrp ntrp;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    public String getSiteusername() {
        return siteusername;
    }

    public void setSiteusername(String siteusername) {
        this.siteusername = siteusername;
    }

    @Column(name = "NAME", nullable = false)
    private String siteusername;

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

    // 이메일
    // @Column(name = "IS_MAIL_VERIFIED") // 1:true, 0:false, default = 0
    // private Boolean isMailVerified;

    @OneToMany(mappedBy = "siteUser")
    private List<Matching> hostedMatches; // 주최한 매칭

    @OneToMany(mappedBy = "siteUser")
    private List<Apply> applies; // 신청 내역

    @OneToMany(mappedBy = "siteUser")
    private List<Notification> notifications; // 알림

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Collection<GrantedAuthority> authorities = new ArrayList<>();
        //authorities.add(new SimpleGrantedAuthority(this.getRoles().toString()));
        //return authorities;
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setNtrp(Ntrp ntrp) {
        this.ntrp = ntrp;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setPenaltyScore(Integer penaltyScore) {
        this.penaltyScore = penaltyScore;
    }

    // 이메일
/*    public void setEmailVerified() {
        this.isMailVerified = true;
    }*/
}
