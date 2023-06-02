package br.com.hioktec.infrastructure.repositories;

import br.com.hioktec.domain.models.Customer;
import br.com.hioktec.domain.repositories.CustomerQuery;
import br.com.hioktec.domain.repositories.CustomerRepository;
import br.com.hioktec.infrastructure.repositories.entities.CustomerProfilePhotos;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class HibernateCustomerRepository implements CustomerRepository {

  private final EntityManager entityManager;

  public HibernateCustomerRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public List<Customer> find(CustomerQuery query) {
    var criteriaBuilder = entityManager.getCriteriaBuilder();
    var criteriaQuery = criteriaBuilder.createQuery(CustomerProfilePhotos.class);
    var root = criteriaQuery.from(CustomerProfilePhotos.class);

    criteriaQuery.select(root).where(conditions(query, criteriaBuilder, root));

    return entityManager.createQuery(criteriaQuery)
      .getResultStream()
      .map(CustomerProfilePhotos::toDomain)
      .collect(Collectors.groupingBy(Customer::id))
      .entrySet()
      .stream()
      .map(entry -> new Customer(
        entry.getKey(),
        entry.getValue().stream().flatMap(customer -> customer.profilePhotos().stream()).toList()))
      .toList();
  }

  private Predicate[] conditions(
    CustomerQuery query,
    CriteriaBuilder criteriaBuilder,
    Root<CustomerProfilePhotos> root
  ) {
    return Stream.of(query.ids()
      .map(id -> criteriaBuilder.in(root.get("compositeKey").get("customerId")).value(id)))
      .flatMap(Optional::stream)
      .toArray(Predicate[]::new);
  }
}
