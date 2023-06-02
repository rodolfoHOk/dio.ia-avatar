package br.com.hioktec.infrastructure.repositories.reactives;

import br.com.hioktec.domain.models.ProfilePhoto;
import br.com.hioktec.domain.repositories.reactives.ReactiveProfilePhotoRepository;
import br.com.hioktec.infrastructure.repositories.S3ProfilePhotoStorageRepository;
import br.com.hioktec.infrastructure.rest.services.StableDiffusionService;
import org.jboss.logging.Logger;

import java.util.Map;

public class ReactiveUnitOfWorkProfilePhotoRepository implements ReactiveProfilePhotoRepository {

  private final MariaDBReactiveProfilePhotoPersistenceRepository persistenceRepository;
  private final S3ProfilePhotoStorageRepository storageRepository;
  private final StableDiffusionService stableDiffusionService;

  private Map<String, ProfilePhoto> entities;

  public ReactiveUnitOfWorkProfilePhotoRepository(
    MariaDBReactiveProfilePhotoPersistenceRepository persistenceRepository,
    S3ProfilePhotoStorageRepository storageRepository,
    StableDiffusionService stableDiffusionService
  ) {
    this.persistenceRepository = persistenceRepository;
    this.storageRepository = storageRepository;
    this.stableDiffusionService = stableDiffusionService;
    this.entities = Map.of();
  }

  @Override
  public void registerEntities(Map<String, ProfilePhoto> entities) {
    this.entities = entities;
  }

  @Override
  public void commit() {
    entities.forEach(((customerId, profilePhoto) -> {
      try {
        persistenceRepository.save(customerId, profilePhoto).await().indefinitely();

        var generatedBase64Image = stableDiffusionService.generate(profilePhoto).await().indefinitely();

        var originalS3Url = storageRepository.store(customerId, profilePhoto).await().indefinitely();
        var generatedS3Url = storageRepository.store(customerId, profilePhoto, generatedBase64Image)
          .await().indefinitely();

        var updateProfilePhoto = new ProfilePhoto(profilePhoto.id(), originalS3Url, generatedS3Url);

        persistenceRepository.save(customerId, updateProfilePhoto).await().indefinitely();

      } catch (Exception ex) {
        Logger.getLogger(getClass()).error(ex);
      }
    }));
  }

  @Override
  public void rollback() {

  }
}
