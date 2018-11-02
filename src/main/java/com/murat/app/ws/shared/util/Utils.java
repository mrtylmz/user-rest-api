package com.murat.app.ws.shared.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class Utils {

	public String generateUserId(){
		UUID generatedUserId = UUID.randomUUID();
		return generatedUserId.toString();
	}
	
	public enum OperationNamesEnum{
		DELETE
	}
	
	public enum OperationStatusEnum{
		SUCCESS,ERROR
	}
}
