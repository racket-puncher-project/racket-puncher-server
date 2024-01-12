package com.example.demo.auth.service;

import com.example.demo.auth.dto.StringResponseDto;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.util.sms.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.example.demo.exception.type.ErrorCode.PHONE_AUTH_NUM_DOESNT_MATCH;
import static com.example.demo.exception.type.ErrorCode.PHONE_AUTH_NUM_EXPIRED;

@RequiredArgsConstructor
@Service
public class PhoneAuthService {
    private final static String SMS_PREFIX = "sms";
    private final static String SMS_SEND_SUCCESS = "sms 문자가 전송되었습니다.";
    private final static String SMS_VERIFICATION_SUCCESS = "휴대폰 번호가 성공적으로 인증되었습니다.";

    private final RedisTemplate<String, String> redisTemplate;
    private final MessageSender messageSender;

    public StringResponseDto sendCode(String phoneNumber) {
        String randomNumber = makeRandomNumber();
        messageSender.send(phoneNumber, makeText(randomNumber));
        redisTemplate.opsForValue().set(SMS_PREFIX + ":" + phoneNumber, randomNumber, 5, TimeUnit.MINUTES);
        return new StringResponseDto(SMS_SEND_SUCCESS);
    }

    public StringResponseDto verifyCode(String phoneNumber, String authCode) {
        String key = SMS_PREFIX + ":" + phoneNumber;
        String storedCode = redisTemplate.opsForValue().get(key);
        if (storedCode == null) {
            throw new RacketPuncherException(PHONE_AUTH_NUM_EXPIRED);
        }
        if (storedCode.equals(authCode)) {
            redisTemplate.delete(key);
            return new StringResponseDto(SMS_VERIFICATION_SUCCESS);
        }
        throw new RacketPuncherException(PHONE_AUTH_NUM_DOESNT_MATCH);
    }

    private String makeText(String randomNumber) {
        return "[라켓펀처] 인증번호는 " + randomNumber + "입니다. (제한시간 5분)";
    }

    private String makeRandomNumber() {
        StringBuilder randomNum = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            String random = Integer.toString(rand.nextInt(10));
            randomNum.append(random);
        }
        return randomNum.toString();
    }
}