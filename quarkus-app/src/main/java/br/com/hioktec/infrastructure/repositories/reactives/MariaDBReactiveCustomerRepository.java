package br.com.hioktec.infrastructure.repositories.reactives;

import br.com.hioktec.domain.models.Customer;
import br.com.hioktec.domain.repositories.reactives.ReactiveCustomerRepository;
import br.com.hioktec.infrastructure.repositories.entities.CustomerProfilePhotos;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

@ApplicationScoped
public class MariaDBReactiveCustomerRepository implements ReactiveCustomerRepository {

  private final MySQLPool client;

  public MariaDBReactiveCustomerRepository(MySQLPool client) {
    this.client = client;
  }

  @Override
  public Multi<Customer> findAll() {
    Uni<RowSet<Row>> rowSet = client
      .query("SELECT * FROM profile_photos")
      .execute();

    var customersProfilePhotos = hydrateMultiCustomerProfilePhotos(rowSet);

    return convertToMultiConsumer(customersProfilePhotos);
  }

  @Override
  public Uni<Customer> findById(String id) {
    Uni<RowSet<Row>> rowSet = client
      .preparedQuery("SELECT * FROM profile_photos WHERE customer_id = ?")
      .execute(Tuple.of(id));

    var customersProfilePhotos = hydrateMultiCustomerProfilePhotos(rowSet);

    return convertToMultiConsumer(customersProfilePhotos).collect().first();
  }

  private static Multi<CustomerProfilePhotos> hydrateMultiCustomerProfilePhotos(Uni<RowSet<Row>> rowSet) {
    return rowSet
      .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
      .onItem().transform(row -> {
        CustomerProfilePhotos customer = new CustomerProfilePhotos();
        customer.setCompositeKey(new CustomerProfilePhotos.CompositeKey());
        customer.getCompositeKey().setCustomerId(row.getString("customer_id"));
        customer.getCompositeKey().setId(row.getString("id"));
        customer.setOriginalPhoto(row.getString("original_photo"));
        customer.setGeneratedPhoto(row.getString("generated_photo"));
        return customer;
      });
  }

  private static Multi<Customer> convertToMultiConsumer(Multi<CustomerProfilePhotos> customersProfilePhotos) {
    return customersProfilePhotos
      .onItem().transform(CustomerProfilePhotos::toDomain)
      .collect().asMultiMap(Customer::id)
      .map(Map::entrySet)
      .onItem().transform(set -> set.stream()
        .map(entry -> new Customer(
          entry.getKey(),
          entry.getValue().stream().flatMap(customer -> customer.profilePhotos().stream()).toList()))
        .toList())
      .onItem().transformToMulti(set -> Multi.createFrom().iterable(set));
  }
}
