package com.murat.app.ws.io.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.murat.app.ws.io.entity.AddressEntity;
import com.murat.app.ws.io.entity.UserEntity;

@Repository
public interface AddressReposity extends CrudRepository<AddressEntity, Long> {

	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

	AddressEntity findByAddressId(String addressId);
}
