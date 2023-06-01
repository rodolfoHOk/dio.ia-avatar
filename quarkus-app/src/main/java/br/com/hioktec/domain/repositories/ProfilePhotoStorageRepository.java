package br.com.hioktec.domain.repositories;

import br.com.hioktec.domain.models.ProfilePhoto;
import io.smallrye.mutiny.Uni;

public interface ProfilePhotoStorageRepository {
  Uni<String> store(String customerId, ProfilePhoto profilePhoto);

  Uni<String> store(String customerId, ProfilePhoto profilePhoto, String base64);
}
