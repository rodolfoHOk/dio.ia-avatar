package br.com.hioktec.domain.repositories.reactives;

import br.com.hioktec.domain.models.ProfilePhoto;
import io.smallrye.mutiny.Uni;

public interface ReactiveProfilePhotoPersistenceRepository {

  Uni<String> save(String customerId, ProfilePhoto profilePhoto);

}
