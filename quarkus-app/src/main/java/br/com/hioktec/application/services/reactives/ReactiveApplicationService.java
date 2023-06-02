package br.com.hioktec.application.services.reactives;

import br.com.hioktec.application.dto.CustomerDTO;
import br.com.hioktec.application.dto.ProfilePhotoDTO;
import br.com.hioktec.domain.services.reactives.ReactiveCustomerReadService;
import br.com.hioktec.domain.services.reactives.ReactiveProfilePhotoCreateService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReactiveApplicationService {
  private final ReactiveCustomerReadService customerReadService;
  private final ReactiveProfilePhotoCreateService profilePhotoCreateService;

  public ReactiveApplicationService(
    ReactiveCustomerReadService customerReadService,
    ReactiveProfilePhotoCreateService profilePhotoCreateService
  ) {
    this.customerReadService = customerReadService;
    this.profilePhotoCreateService = profilePhotoCreateService;
  }

  public Multi<CustomerDTO> searchCustomers() {
    return customerReadService.findAll().onItem().transform(CustomerDTO::fromDomain);
  }

  public Uni<CustomerDTO> getCustomer(String customerId) {
    return customerReadService.findById(customerId).onItem().transform(CustomerDTO::fromDomain);
  }

  public void persistProfilePhoto(String customerId, ProfilePhotoDTO profilePhotoDTO) {
    profilePhotoCreateService.save(customerId, profilePhotoDTO.toDomain());
  }
}
