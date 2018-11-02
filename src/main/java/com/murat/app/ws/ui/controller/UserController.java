package com.murat.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.murat.app.ws.service.UserService;
import com.murat.app.ws.shared.dto.UserDto;
import com.murat.app.ws.shared.util.Utils.OperationNamesEnum;
import com.murat.app.ws.shared.util.Utils.OperationStatusEnum;
import com.murat.app.ws.ui.model.request.UserDetailsRequestModel;
import com.murat.app.ws.ui.model.response.OperationStatusModel;
import com.murat.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;
	
	
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
		
		UserDto userDto=new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto createdUser=userService.createUser(userDto);
		UserRest returnValue=new UserRest();
		BeanUtils.copyProperties(createdUser, returnValue);
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
}
