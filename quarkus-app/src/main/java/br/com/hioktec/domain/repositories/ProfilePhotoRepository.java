package br.com.hioktec.domain.repositories;

import br.com.hioktec.domain.models.ProfilePhoto;

import java.util.Map;

public interface ProfilePhotoRepository {
  void registerEntities(Map<String, ProfilePhoto> entities);
  void commit();
  void rollback();
}
