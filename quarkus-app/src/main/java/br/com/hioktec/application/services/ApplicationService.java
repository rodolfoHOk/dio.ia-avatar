package br.com.hioktec.application.services;

import br.com.hioktec.application.dto.CustomerDTO;
import br.com.hioktec.application.dto.ProfilePhotoDTO;
import br.com.hioktec.domain.services.CustomerReadService;
import br.com.hioktec.domain.services.ProfilePhotoCreateService;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ApplicationService {
  private final CustomerReadService customerReadService;
  private final ProfilePhotoCreateService profilePhotoCreateService;

  public ApplicationService(CustomerReadService customerReadService, ProfilePhotoCreateService profilePhotoCreateService) {
    this.customerReadService = customerReadService;
    this.profilePhotoCreateService = profilePhotoCreateService;
  }

  public List<CustomerDTO> searchCustomers() {
    return customerReadService.find().stream().map(CustomerDTO::fromDomain).toList();
  }

  public CustomerDTO getCustomer(String customerId) {
    return CustomerDTO.fromDomain(customerReadService.findById(customerId));
  }

  public void persistProfilePhoto(String customerId, ProfilePhotoDTO profilePhotoDTO) {
    profilePhotoCreateService.save(customerId, profilePhotoDTO.toDomain());
  }
}
