package br.com.hioktec.domain.repositories.reactives;

import br.com.hioktec.domain.models.ProfilePhoto;

import java.util.Map;

public interface ReactiveProfilePhotoRepository {
  void registerEntities(Map<String, ProfilePhoto> entities);
  void commit();
  void rollback();
}
