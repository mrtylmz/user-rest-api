package com.murat.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murat.app.ws.service.AddressService;
import com.murat.app.ws.service.UserService;
import com.murat.app.ws.shared.dto.AddressDto;
import com.murat.app.ws.shared.dto.UserDto;
import com.murat.app.ws.shared.util.Utils.OperationNamesEnum;
import com.murat.app.ws.shared.util.Utils.OperationStatusEnum;
import com.murat.app.ws.ui.model.request.UserDetailsRequestModel;
import com.murat.app.ws.ui.model.response.AddressRest;
import com.murat.app.ws.ui.model.response.OperationStatusModel;
import com.murat.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("/users")
public class UserController {

	Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@GetMapping(path="/{userId}",
			produces={MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE}
	)
	public UserRest getUser(@PathVariable String userId)
	{
		UserRest returnValue=new UserRest();
		UserDto userDto=userService.getUserByUsedId(userId);
		BeanUtils.copyProperties(userDto, returnValue);
		return returnValue;
	}
	
	@PostMapping(
			consumes = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
			produces={MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE}
			)
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails){
		
		log.debug("Create User Method.");
		//UserDto userDto=new UserDto();
		//BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto userDto=modelMapper.map(userDetails, UserDto.class);
		
		UserDto createdUser=userService.createUser(userDto);
		
		//UserRest returnValue=new UserRest();
		UserRest returnValue=modelMapper.map(createdUser, UserRest.class);
		//BeanUtils.copyProperties(createdUser, returnValue);
		
		return returnValue;
	}
	
	@PutMapping(path="/{userId}",
			consumes = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
			produces={MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE}
			)
	public UserRest updateUser(@PathVariable String userId,@RequestBody UserDetailsRequestModel userDetails)
	{
		UserDto userDto=new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto updatedUser=userService.updateUser(userId,userDto);
		UserRest returnValue=new UserRest();
		BeanUtils.copyProperties(updatedUser, returnValue);
		return returnValue;
	}

	@DeleteMapping(path="/{userId}",
			produces={MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE}
	)
	public OperationStatusModel deleteUser(@PathVariable String userId){
		OperationStatusModel operationStatusModel=new OperationStatusModel();
		operationStatusModel.setOperationName(OperationNamesEnum.DELETE.name());
		
		userService.deleteUser(userId);
		operationStatusModel.setOperationStatus(OperationStatusEnum.SUCCESS.name());
		return operationStatusModel;
	}
	
	@GetMapping(
			produces={MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE}
			)
	public List<UserRest> getUsers(@RequestParam(value="page",defaultValue="0") int page,
			@RequestParam(value="limit",defaultValue="25") int limit)
	{
		List<UserRest> returnValues=new ArrayList<>();
		List<UserDto> users=userService.getUsers(page,limit);
		
		for(UserDto userDtos:users){
			UserRest userRest=new UserRest();
			BeanUtils.copyProperties(userDtos, userRest);
			returnValues.add(userRest);
		}
		return returnValues;
	}
	
	@GetMapping(path="/{userId}/addresses",
			produces={MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE}
	)//Bir kullanıcya ait olan adresler /users/userıdjasdkas/addresses
	public List<AddressRest> getUserAddresses(@PathVariable String userId)
	{
		List<AddressRest> returnValue=new ArrayList<>();
		List<AddressDto> addressDto=addressService.getUserAddresses(userId);
		if(addressDto!=null && !addressDto.isEmpty()){
			Type listType = new TypeToken<List<AddressRest>>() {}.getType();
			returnValue=modelMapper.map(addressDto, listType);
		}
		
		return returnValue;
	}
	
	@GetMapping(path="/address/{addressId}",
			produces={MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE}
	)//Bir adrese ait bilgiler
	public AddressRest getAddres(@PathVariable String addressId)
	{
		AddressDto adressDto = addressService.getAddress(addressId);
		AddressRest returnValue=new ModelMapper().map(adressDto, AddressRest.class);
		return returnValue;
	}
	
}
