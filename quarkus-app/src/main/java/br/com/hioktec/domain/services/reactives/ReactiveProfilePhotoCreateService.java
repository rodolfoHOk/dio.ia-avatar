package br.com.hioktec.domain.services.reactives;

import br.com.hioktec.domain.models.ProfilePhoto;
import br.com.hioktec.domain.repositories.reactives.ReactiveProfilePhotoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

@ApplicationScoped
public class ReactiveProfilePhotoCreateService {
  private final ReactiveProfilePhotoRepository repository;

  public ReactiveProfilePhotoCreateService(ReactiveProfilePhotoRepository repository) {
    this.repository = repository;
  }

  public void save(String customerId, ProfilePhoto profilePhoto) {
    repository.registerEntities(Map.of(customerId, profilePhoto));
    repository.commit();
  }
}
