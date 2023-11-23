package com.example.demo.siteuser.service;

import com.example.demo.entity.Auth;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.impl.AuthAlreadyExistEmailException;
import com.example.demo.exception.impl.AuthEmailNotFoundException;
import com.example.demo.exception.impl.AuthWrongPasswordException;
import com.example.demo.exception.impl.EmailNotVerifiedException;
import com.example.demo.siteuser.repository.SiteUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final SiteUserRepository siteUserRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new AuthEmailNotFoundException());
    }

    public SiteUser register(Auth.SignUp member) {
        boolean exists = this.siteUserRepository.existsByEmail(member.getEmail());
        if (exists) {
            throw new AuthAlreadyExistEmailException();
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        var result = this.siteUserRepository.save(member.fromUser());
        return result;
    }

    public SiteUser withdraw(Auth.Quit member) {
        var user = this.siteUserRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new AuthEmailNotFoundException());

        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new AuthWrongPasswordException();
        }
        this.siteUserRepository.delete(user);
        return user;
    }

    public SiteUser authenticate(Auth.SignIn member) {

        var user = this.siteUserRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new AuthEmailNotFoundException());

        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new AuthWrongPasswordException();
        }

/* // 이메일
        if(!user.getIsMailVerified()){
            throw new EmailNotVerifiedException();
        }
*/

        return user;
    }

    public boolean isEmailExist(String email) {

        return siteUserRepository.existsByEmail(email);
    }

    public boolean isNicknameExist(String nickname) {
        return siteUserRepository.existsByNickname(nickname);
    }
}
