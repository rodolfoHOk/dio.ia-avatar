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

@ApplicationScoped
public class MariaDBReactiveCustomerRepository implements ReactiveCustomerRepository {

  private final MySQLPool client;

  public MariaDBReactiveCustomerRepository(MySQLPool client) {
    this.client = client;
  }

  @Override
  public Multi<Customer> findAll() {
    Uni<RowSet<Row>> rowSet = client.query("SELECT * FROM profile_photos").execute();

    Multi<CustomerProfilePhotos> customersProfilePhotos = rowSet
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

    return customersProfilePhotos
      .onItem().transform(CustomerProfilePhotos::toDomain);
  }

  @Override
  public Uni<Customer> findById(String id) {
    Uni<RowSet<Row>> rowSet = client
      .preparedQuery("SELECT * FROM profile_photos WHERE id = $1")
      .execute(Tuple.of(id));

    Uni<CustomerProfilePhotos> customerProfilePhotos = rowSet
      .onItem().transform(RowSet::iterator)
      .onItem().transform(iterator -> {
        if (iterator.hasNext()) {
          var row = iterator.next();
          var customer = new CustomerProfilePhotos();
          customer.setCompositeKey(new CustomerProfilePhotos.CompositeKey());
          customer.getCompositeKey().setCustomerId(row.getString("customer_id"));
          customer.getCompositeKey().setId(row.getString("id"));
          customer.setOriginalPhoto(row.getString("original_photo"));
          customer.setGeneratedPhoto(row.getString("generated_photo"));
          return customer;
        }
        return null;
      });

    return customerProfilePhotos.onItem().transform(CustomerProfilePhotos::toDomain);
  }
}
