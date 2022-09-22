package com.example.societepersonnel.domain.enterprise;

import com.example.societepersonnel.core.exception.EntreprisePersonnelException;
import com.example.societepersonnel.core.rest.Codes;
import com.example.societepersonnel.core.utils.StringUtils;
import com.example.societepersonnel.domain.address.Address;
import com.example.societepersonnel.domain.address.AddressMapper;
import com.example.societepersonnel.domain.address.AddressService;
import com.example.societepersonnel.dto.AddressDto;
import com.example.societepersonnel.dto.EnterpriseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
@Slf4j
public class EnterpriseService {

    private final EnterpriseMapper enterpriseMapper;
    private final EnterpriseRepository enterpriseRepository;
    private final AddressMapper addressMapper;
    private final AddressService addressService;

    public EnterpriseService(EnterpriseMapper enterpriseMapper, EnterpriseRepository enterpriseRepository, AddressMapper addressMapper, AddressService addressService) {
        this.enterpriseMapper = enterpriseMapper;
        this.enterpriseRepository = enterpriseRepository;
        this.addressMapper = addressMapper;
        this.addressService = addressService;
    }

    private Address AddAddressEnterprise(Long id_address) {
        System.out.println("li des eeeeeeeeeeee : " + id_address);
        AddressDto addressDto = addressService.findAddressById(id_address);
        Address address = addressMapper.toEntity(addressDto);
        return address;
    }

    private Enterprise searchEnterpriseById(Long id) {
        return enterpriseRepository.findById(id).orElseThrow(()
                -> new EntreprisePersonnelException(Codes.ERR_ENTREPRESE_NOT_FOUND));

    }

    public EnterpriseDto createEnterprise(EnterpriseDto enterpriseDto) {
        Enterprise enterprise = enterpriseMapper.toEntity(enterpriseDto);
        Long id_addLong = enterpriseDto.getAddressId();
        Address address = AddAddressEnterprise(id_addLong);
        enterprise.setAddress(address);
        enterprise = enterpriseRepository.save(enterprise);
        log.info("the addition of the company {}", enterpriseDto.getName());
        return enterpriseMapper.toDto(enterprise);
    }

    public List<EnterpriseDto> findEnterprises() {
        List<Enterprise> enterprises = enterpriseRepository.findAll();
        log.info("list the companies {}", enterprises.size());
        return enterpriseMapper.toDtos(enterprises);
    }

    public EnterpriseDto findEnterpriseById(Long id) {
        Enterprise enterprise = searchEnterpriseById(id);
        log.info("The company searched with id {}", id);
        return enterpriseMapper.toDto(enterprise);
    }

    public EnterpriseDto updateEnterprise(Long id, EnterpriseDto enterpriseDto) {
        Enterprise currentEnterprise = searchEnterpriseById(id);
        Enterprise enterpriseUpdate = enterpriseMapper.toEntity(enterpriseDto);
        Long id_address = enterpriseDto.getAddressId();
        if (!StringUtils.isNullOrEmpty(enterpriseUpdate.getName())) {
            currentEnterprise.setName(enterpriseUpdate.getName());
        }
        if (!StringUtils.isNullOrEmpty(enterpriseUpdate.getTaxNumber())) {
            currentEnterprise.setTaxNumber(enterpriseUpdate.getTaxNumber());
        }
        if (id_address != null) {
            Address address = AddAddressEnterprise(id_address);
            currentEnterprise.setAddress(address);
        }
        if (enterpriseUpdate.getPersonals() != null) {
            currentEnterprise.setPersonals(enterpriseUpdate.getPersonals());
        }
        currentEnterprise = enterpriseRepository.save(currentEnterprise);
        log.info("The company is successfully modified {}", enterpriseDto.getName());
        return enterpriseMapper.toDto(currentEnterprise);
    }

    public void deleteEnterprise(Long id) {
        searchEnterpriseById(id);
        enterpriseRepository.deleteById(id);
        log.info("The company is successfully deleted with the id {}", id);
    }

}