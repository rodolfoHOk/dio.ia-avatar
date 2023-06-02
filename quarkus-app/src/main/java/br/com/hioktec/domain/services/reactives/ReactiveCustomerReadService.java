package br.com.hioktec.domain.services.reactives;

import br.com.hioktec.domain.models.Customer;
import br.com.hioktec.domain.repositories.reactives.ReactiveCustomerRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReactiveCustomerReadService {
  private final ReactiveCustomerRepository repository;

  public ReactiveCustomerReadService(ReactiveCustomerRepository repository) {
    this.repository = repository;
  }

  public Multi<Customer> findAll() {
    return repository.findAll();
  }

  public Uni<Customer> findById(String id) {
    return repository.findById(id);
  }
}
