package br.com.hioktec.application.dto;

import br.com.hioktec.domain.models.ProfilePhoto;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public record ProfilePhotoDTO(
  FileUpload fileUpload
) {
  public static ProfilePhotoDTO create(FileUpload fileUpload) {
    return new ProfilePhotoDTO(fileUpload);
  }

  public ProfilePhoto toDomain() {
    return ProfilePhoto.create(fileUpload().uploadedFile().toAbsolutePath().toString());
  }
}
