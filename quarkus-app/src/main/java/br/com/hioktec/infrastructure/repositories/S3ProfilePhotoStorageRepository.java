package br.com.hioktec.infrastructure.repositories;

import br.com.hioktec.domain.models.ProfilePhoto;
import br.com.hioktec.domain.repositories.ProfilePhotoStorageRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class S3ProfilePhotoStorageRepository implements ProfilePhotoStorageRepository {

  @ConfigProperty(name = "quarkus.s3.devservices.buckets")
  private String bucket;

  private final S3AsyncClient s3;

  public S3ProfilePhotoStorageRepository(S3AsyncClient s3) {
    this.s3 = s3;
  }

  @Override
  public Uni<String> store(String customerId, ProfilePhoto profilePhoto) {
    String key = customerId + "/" + profilePhoto.id();
    var path = Path.of(profilePhoto.originalPhoto());

    CompletableFuture<PutObjectResponse> future = s3.putObject(
      PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType("image/png")
        .acl(ObjectCannedACL.PUBLIC_READ)
        .build(),
      AsyncRequestBody.fromFile(path)
    );

    return Uni.createFrom()
      .completionStage(() -> future)
      .onItem().delayIt().by(Duration.ofSeconds(5))
      .onItem()
      .transform(response -> s3.utilities()
        .getUrl(GetUrlRequest.builder()
          .bucket(bucket)
          .key(key)
          .build())
        .toString()
      );
  }

  @Override
  public Uni<String> store(String customerId, ProfilePhoto profilePhoto, String base64) {
    String key = customerId + "/" + profilePhoto.id();
    byte[] decodedBytes = Base64.getDecoder().decode(base64);

    CompletableFuture<PutObjectResponse> future = s3.putObject(
      PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType("image/png")
        .contentLength((long) decodedBytes.length)
        .acl(ObjectCannedACL.PUBLIC_READ)
        .build(),
      AsyncRequestBody.fromBytes(decodedBytes)
    );

    return Uni.createFrom()
      .completionStage(() -> future)
      .onItem().delayIt().by(Duration.ofSeconds(5))
      .onItem()
      .transform(response -> s3.utilities()
        .getUrl(GetUrlRequest.builder()
          .bucket(bucket)
          .key(key)
          .build())
        .toString()
      );
  }
}
