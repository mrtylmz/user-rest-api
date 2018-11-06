package com.murat.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;


import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murat.app.ws.io.entity.UserEntity;
import com.murat.app.ws.io.repo.UserReposity;
import com.murat.app.ws.service.UserService;
import com.murat.app.ws.shared.dto.AddressDto;
import com.murat.app.ws.shared.dto.UserDto;
import com.murat.app.ws.shared.util.Utils;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserReposity userReposity;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDto createUser(UserDto user) {
		UserEntity isUserRecorded=userReposity.findByEmail(user.getEmail());
		if(isUserRecorded!=null)
			throw new RuntimeException("Same Record Already Exist");
		
		for(int i=0;i<user.getAddresses().size();i++){
			AddressDto address=user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId());
			user.getAddresses().set(i, address);
		}
		
		ModelMapper modelMapper=new ModelMapper();
		UserEntity userEntity=modelMapper.map(user,UserEntity.class);
		//BeanUtils.copyProperties(user, userEntity);
		
		String generatedUserId=utils.generateUserId();
		userEntity.setUserId(generatedUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		UserEntity storedUserDetails = userReposity.save(userEntity);
		
		//BeanUtils.copyProperties(storedUserDetails, returnValue);
		UserDto returnValue=modelMapper.map(storedUserDetails,UserDto.class);
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity=userReposity.findByEmail(email);
		if(userEntity==null) 
			throw new UsernameNotFoundException("Username Not Found.");
		return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity=userReposity.findByEmail(email);
		if(userEntity==null) 
			throw new UsernameNotFoundException("Username Not Found.");
		UserDto returnValue=new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public UserDto getUserByUsedId(String userId) {
		UserDto returnValue=new UserDto();
		UserEntity userEntity=userReposity.findByUserId(userId);
		if(userEntity==null) 
			throw new UsernameNotFoundException("Username Not Found.");
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		UserDto returnValue=new UserDto();
		UserEntity userEntity=userReposity.findByUserId(userId);
		if(userEntity==null) 
			throw new UsernameNotFoundException("Username Not Found.");
		
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		
		UserEntity updatedUser=userReposity.save(userEntity);
		BeanUtils.copyProperties(updatedUser, returnValue);
		
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity=userReposity.findByUserId(userId);
		if(userEntity==null) 
			throw new UsernameNotFoundException("Username Not Found.");
		userReposity.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValues=new ArrayList<>();
		
		Pageable pageAbleRequest=PageRequest.of(page, limit);
		Page<UserEntity> usersPage=userReposity.findAll(pageAbleRequest);
		List<UserEntity> users=usersPage.getContent();
		
		for(UserEntity user:users){
			UserDto userDto=new UserDto();
			BeanUtils.copyProperties(user, userDto);
			returnValues.add(userDto);
		}
		return returnValues;
	}

}
