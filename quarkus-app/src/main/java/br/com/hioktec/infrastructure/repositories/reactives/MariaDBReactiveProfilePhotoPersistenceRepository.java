package br.com.hioktec.infrastructure.repositories.reactives;

import br.com.hioktec.domain.models.ProfilePhoto;
import br.com.hioktec.domain.repositories.reactives.ReactiveProfilePhotoPersistenceRepository;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Tuple;

public class MariaDBReactiveProfilePhotoPersistenceRepository implements ReactiveProfilePhotoPersistenceRepository {

  private final MySQLPool client;

  public MariaDBReactiveProfilePhotoPersistenceRepository(MySQLPool client) {
    this.client = client;
  }

  @Override
  public Uni<String> save(String customerId, ProfilePhoto profilePhoto) {
    return client
      .preparedQuery("INSERT INTO profile_photos (customer_id, id, original_photo, generated_photo) VALUES ($1, $2, $3, $4)")
      .execute(Tuple.of(customerId, profilePhoto.id(), profilePhoto.originalPhoto(), profilePhoto.generatedPhoto()))
      .onItem().transform(rowSet -> rowSet.iterator().next().getString("customer_id"));
  }
}