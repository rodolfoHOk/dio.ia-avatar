package br.com.hioktec.infrastructure.repositories.entities;

import br.com.hioktec.domain.models.Customer;
import br.com.hioktec.domain.models.ProfilePhoto;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

@Entity(name = "profile_photos")
public class CustomerProfilePhotos {

  @EmbeddedId
  private CompositeKey compositeKey;

  @Column(name = "original_photo", nullable = false)
  private String originalPhoto;

  @Column(name = "generated_photo", nullable = true)
  private String generatedPhoto;

  @Column(name = "created_at")
  @CreationTimestamp
  private OffsetDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private OffsetDateTime updatedAt;

  public CompositeKey getCompositeKey() {
    return compositeKey;
  }

  public void setCompositeKey(CompositeKey compositeKey) {
    this.compositeKey = compositeKey;
  }

  public String getOriginalPhoto() {
    return originalPhoto;
  }

  public void setOriginalPhoto(String originalPhoto) {
    this.originalPhoto = originalPhoto;
  }

  public String getGeneratedPhoto() {
    return generatedPhoto;
  }

  public void setGeneratedPhoto(String generatedPhoto) {
    this.generatedPhoto = generatedPhoto;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  @Embeddable
  public static class CompositeKey {

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "id", nullable = false)
    private String id;

    public String getCustomerId() {
      return customerId;
    }

    public void setCustomerId(String customerId) {
      this.customerId = customerId;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }
  }

  public Customer toDomain() {
    return new Customer(
      compositeKey.customerId,
      List.of(new ProfilePhoto(compositeKey.id, originalPhoto, generatedPhoto))
    );
  }

  public static CustomerProfilePhotos fromDomain(String customerId, ProfilePhoto profilePhoto) {
    var entity = new CustomerProfilePhotos();

    entity.compositeKey = new CompositeKey();
    entity.compositeKey.customerId = customerId;
    entity.compositeKey.id = profilePhoto.id();

    entity.originalPhoto = profilePhoto.originalPhoto();
    entity.generatedPhoto = profilePhoto.generatedPhoto();

    return entity;
  }
}
