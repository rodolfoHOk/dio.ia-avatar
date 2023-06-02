package br.com.hioktec.domain.models;

import java.util.UUID;

public record ProfilePhoto(
  String id,
  String originalPhoto,
  String generatedPhoto
) {
  public static ProfilePhoto create(String originalPhoto) {
    return new ProfilePhoto(UUID.randomUUID().toString(), originalPhoto, null);
  }
}
