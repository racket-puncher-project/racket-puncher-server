package com.example.demo.auth.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.example.demo.exception.RacketPuncherException;
import com.example.demo.util.sms.MessageSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class PhoneAuthServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private MessageSender messageSender;

    @InjectMocks
    private PhoneAuthService phoneAuthService;

    private static final String SMS_PREFIX = "sms";
    private final String phoneNumber = "01012345678";
    private final String randomNumber = "123456";

    @Test
    public void sendCode() {
        //given
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        //when
        var result = phoneAuthService.sendCode(phoneNumber);

        //then
        assertEquals("sms 문자가 전송되었습니다.", result.getMessage());
        verify(messageSender, times(1)).send(eq(phoneNumber), anyString());
    }

    @Test
    public void verifyCodeSuccess() {
        //given
        String key = SMS_PREFIX + ":" + phoneNumber;
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(key)).willReturn(randomNumber);

        //when
        var result = phoneAuthService.verifyCode(phoneNumber, randomNumber);

        //then
        assertEquals("휴대폰 번호가 성공적으로 인증되었습니다.", result.getMessage());
    }

    @Test
    public void verifyCodeFailByCodeExpired() {
        //given
        String key = SMS_PREFIX + ":" + phoneNumber;
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(key)).willReturn(null);

        //when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> phoneAuthService.verifyCode(phoneNumber, randomNumber));

        //then
        assertEquals(exception.getMessage(), "휴대폰 인증 번호가 만료되었습니다.");
    }

    @Test
    public void verifyCodeFailByCodeDoesNotMatch() {
        //given
        String key = SMS_PREFIX + ":" + phoneNumber;
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        String wrongCode = "654321";
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(key)).willReturn(randomNumber);

        //when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> phoneAuthService.verifyCode(phoneNumber, wrongCode));
        //then
        assertEquals(exception.getMessage(), "휴대폰 인증 번호가 일치하지 않습니다.");
    }
}