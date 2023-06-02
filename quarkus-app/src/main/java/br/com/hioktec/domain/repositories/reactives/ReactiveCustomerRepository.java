package br.com.hioktec.domain.repositories.reactives;

import br.com.hioktec.domain.models.Customer;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface ReactiveCustomerRepository {
  Multi<Customer> findAll();

  Uni<Customer> findById(String id);
}
