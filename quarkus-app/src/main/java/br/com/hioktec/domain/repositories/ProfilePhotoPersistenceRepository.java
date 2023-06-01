package br.com.hioktec.domain.repositories;

import br.com.hioktec.domain.models.ProfilePhoto;

public interface ProfilePhotoPersistenceRepository {
  void save(String customerId, ProfilePhoto profilePhoto);
}
