package br.com.hioktec.application.dto;

import br.com.hioktec.domain.models.Customer;
import br.com.hioktec.domain.models.ProfilePhoto;

import java.util.List;

public record CustomerDTO(
  String customerId,
  List<String> photos
) {
  public static CustomerDTO fromDomain(Customer customer) {
    return new CustomerDTO(customer.id(), customer.profilePhotos().stream().map(ProfilePhoto::generatedPhoto).toList());
  }
}
