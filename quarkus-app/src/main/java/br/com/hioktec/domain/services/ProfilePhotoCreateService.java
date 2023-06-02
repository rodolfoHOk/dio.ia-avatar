package br.com.hioktec.domain.services;

import br.com.hioktec.domain.models.ProfilePhoto;
import br.com.hioktec.domain.repositories.ProfilePhotoRepository;

import java.util.Map;

public class ProfilePhotoCreateService {
  private final ProfilePhotoRepository repository;

  public ProfilePhotoCreateService(ProfilePhotoRepository repository) {
    this.repository = repository;
  }

  public void save(String customerId, ProfilePhoto profilePhoto) {
    repository.registerEntities(Map.of(customerId, profilePhoto));
    repository.commit();
  }
}
