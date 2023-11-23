package com.example.demo.oauth2.service;

import com.example.demo.oauth2.dto.AccessToken;
import com.example.demo.oauth2.dto.OAuthRequest;
import com.example.demo.oauth2.OAuthRequestFactory;
import com.example.demo.oauth2.dto.ProfileDto;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.naming.CommunicationException;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final OAuthRequestFactory oAuthRequestFactory;
    public AccessToken getAccessToken(String code, String provider) throws CommunicationException {
        HttpHeaders httpHeaders = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        Gson gson = new Gson();

        OAuthRequest oAuthRequest = oAuthRequestFactory.getRequest(code, provider);
        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(oAuthRequest.getMap() , httpHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity(oAuthRequest.getUrl(), request, String.class);
        try {
            if (response.getStatusCode() == HttpStatus.OK) {
                return gson.fromJson(response.getBody(), AccessToken.class);
            }
        } catch (Exception e) {
            throw new CommunicationException();
        }
        throw new CommunicationException();
    }

    public ProfileDto getProfile(String accessToken, String provider) throws CommunicationException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        String profileUrl = oAuthRequestFactory.getProfileUrl(provider);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(profileUrl, request, String.class);

        try {
            if (response.getStatusCode() == HttpStatus.OK) {
                return extractProfile(response, provider);
            }
        } catch (Exception e) {
            throw new CommunicationException();
        }
        throw new CommunicationException();
    }

    private ProfileDto extractProfile(ResponseEntity<String> response, String provider) {
        if (!provider.equals("kakao")) {
            return null;
        }
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(response.getBody());
        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
        String nickname = properties.getAsJsonObject().get("nickname").getAsString();
        String profileImage = properties.getAsJsonObject().get("profile_image").getAsString();
        ProfileDto profileDto = new ProfileDto();
        profileDto.setNickname(nickname);
        profileDto.setProfileImage(profileImage);
        return profileDto;
    }
}
