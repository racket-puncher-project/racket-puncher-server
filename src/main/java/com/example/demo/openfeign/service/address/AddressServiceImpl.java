package com.example.demo.openfeign.service.address;

import com.example.demo.openfeign.feignclient.AddressApiFeignClient;
import com.example.demo.openfeign.dto.address.AddressRequestDto;
import com.example.demo.openfeign.dto.address.AddressResponseDto;
import com.example.demo.openfeign.dto.address.JusoResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressApiFeignClient addressApiFeignClient;

    @Value("${address-api.key}")
    private String apiKey;

    @Override
    public List<AddressResponseDto> getAddressService(String keyword) {
        AddressRequestDto addressRequestDto = AddressRequestDto.fromKeyword(keyword);
        JusoResponse jusoResponse = addressApiFeignClient
                .getAddress(apiKey,
                        addressRequestDto.getCurrentPage(),
                        addressRequestDto.getCountPerPage(),
                        keyword,
                        addressRequestDto.getResultType(),
                        addressRequestDto.getHstryYn(),
                        addressRequestDto.getFirstSort());

        return jusoResponse.getResults().getJuso().stream()
                .map(juso
                        -> AddressResponseDto.builder()
                        .roadAddr(juso.getRoadAddr())
                        .jibunAddr(juso.getJibunAddr())
                        .zipNo(juso.getZipNo())
                        .build())
                .collect(Collectors.toList());
    }
}