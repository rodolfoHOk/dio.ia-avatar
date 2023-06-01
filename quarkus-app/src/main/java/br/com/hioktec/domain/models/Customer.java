package br.com.hioktec.domain.models;

import java.util.List;

public record Customer(
  String id,
  List<ProfilePhoto> profilePhotos
) {
}
