package com.murat.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murat.app.ws.io.entity.AddressEntity;
import com.murat.app.ws.io.entity.UserEntity;
import com.murat.app.ws.io.repo.AddressReposity;
import com.murat.app.ws.io.repo.UserReposity;
import com.murat.app.ws.service.AddressService;
import com.murat.app.ws.shared.dto.AddressDto;

@Service
@Transactional  //herhangi bir vtde hata durumunca rollback yapsın.
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserReposity userReposity;
	
	@Autowired
	AddressReposity addressReposity;
	
	@Override
	public List<AddressDto> getUserAddresses(String userId) {
		List<AddressDto> returnValue=new ArrayList<>();
		ModelMapper modelMapper=new ModelMapper();
		
		UserEntity userEntity=userReposity.findByUserId(userId);
		if(userEntity==null) 
			throw new UsernameNotFoundException("Username Not Found.");
		
		List<AddressEntity> addressesEntity= addressReposity.findAllByUserDetails(userEntity);
		for(AddressEntity adrEntity:addressesEntity){
			AddressDto addressDTO=modelMapper.map(adrEntity, AddressDto.class);
			returnValue.add(addressDTO);
		}
		return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		AddressEntity adressEntity=addressReposity.findByAddressId(addressId);
		if(adressEntity==null)
			throw new RuntimeException("No records Found");
		ModelMapper modelMapper=new ModelMapper();
		AddressDto returnValue=modelMapper.map(adressEntity, AddressDto.class);
		return returnValue;
	}

}
