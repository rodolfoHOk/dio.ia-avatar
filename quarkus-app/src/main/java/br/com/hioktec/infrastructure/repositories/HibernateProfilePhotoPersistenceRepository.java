package br.com.hioktec.infrastructure.repositories;

import br.com.hioktec.domain.models.ProfilePhoto;
import br.com.hioktec.domain.repositories.ProfilePhotoPersistenceRepository;
import br.com.hioktec.infrastructure.repositories.entities.CustomerProfilePhotos;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

public class HibernateProfilePhotoPersistenceRepository implements ProfilePhotoPersistenceRepository {

  private final EntityManager entityManager;

  public HibernateProfilePhotoPersistenceRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  @Transactional
  public void save(String customerId, ProfilePhoto profilePhoto) {
    entityManager.merge(CustomerProfilePhotos.fromDomain(customerId, profilePhoto));
  }
}
