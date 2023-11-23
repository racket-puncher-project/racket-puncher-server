/*
package com.example.demo.siteuser.service;

import com.example.demo.entity.EmailToken;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.AbstractException;
import com.example.demo.exception.impl.InvalidEmailTokenException;
import com.example.demo.exception.impl.UserNotFoundException;
import com.example.demo.siteuser.repository.EmailTokenRepository;
import com.example.demo.siteuser.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final SiteUserRepository siteUserRepository;
    private final JavaMailSender javaMailSender;
    private final EmailTokenRepository emailTokenRepository;

    @Transactional
    public void verifyEmail(String token) {
        // 이메일 토큰을 찾아오고, 메일 검증 컬럼을 true로 만든 다음 저장
        EmailToken findEmailToken = findByIdAndExpirationDateAfterAndExpired(token);
        SiteUser siteUser = siteUserRepository.findById(findEmailToken.getSiteUserId())
                .orElseThrow(UserNotFoundException::new);
        siteUser.setEmailVerified();
        siteUserRepository.save(siteUser);
    }

    @Transactional
    public void sendEmail(Long siteUserId, String receiverEmail) {
        // 이메일 토큰 저장
        EmailToken emailToken = EmailToken.createEmailToken(siteUserId);
        EmailToken savedEmailToken = emailTokenRepository.save(emailToken);

        // 이메일 전송
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiverEmail);
        mailMessage.setSubject("회원가입 이메일 인증");
        mailMessage.setText("라켓펀처 회원가입을 환영합니다! 아래 링크를 클릭해서 메일 인증을 마무리하세요🌞 \n" +
                "http://3.38.50.101/api/auth/confirm-email?token="+emailToken.getId());
                // "http://localhost:8080/api/auth/confirm-email?token="+emailToken.getId()); // 로컬에서 테스트시 사용
        javaMailSender.send(mailMessage);
    }

    // 유효한 토큰 가져오기
    private EmailToken findByIdAndExpirationDateAfterAndExpired(String emailTokenId) throws AbstractException {

        Optional<EmailToken> emailToken = emailTokenRepository
                .findByIdAndExpirationTimeAfter(emailTokenId, LocalDateTime.now());

        // 토큰이 없다면 예외 발생
        return emailToken.orElseThrow(InvalidEmailTokenException::new);
    }
}*/
