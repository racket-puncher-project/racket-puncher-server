package com.example.demo.openfeign.service.address;

import com.example.demo.openfeign.dto.address.AddressResponseDto;
import java.util.List;

public interface AddressService {

    List<AddressResponseDto> getAddressService(String keyword);

}