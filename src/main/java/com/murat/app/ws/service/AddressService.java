package com.murat.app.ws.service;

import java.util.List;

import com.murat.app.ws.shared.dto.AddressDto;

public interface AddressService {

	List<AddressDto> getUserAddresses(String userId);

	AddressDto getAddress(String addressId);
}
