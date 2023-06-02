package br.com.hioktec.infrastructure.rest.services;

import br.com.hioktec.domain.models.ProfilePhoto;
import br.com.hioktec.infrastructure.rest.clients.StableDiffusionRestClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.NoSuchElementException;

@ApplicationScoped
public class StableDiffusionService {
  private final StableDiffusionRestClient stableDiffusionRestClient;

  public StableDiffusionService(@RestClient StableDiffusionRestClient stableDiffusionRestClient) {
    this.stableDiffusionRestClient = stableDiffusionRestClient;
  }

  @Transactional
  public Uni<String> generate(ProfilePhoto profilePhoto) throws IOException {
    var fileContent = FileUtils.readFileToByteArray(new File(profilePhoto.originalPhoto()));
    var encodedString = Base64.getEncoder().encodeToString(fileContent);

    return stableDiffusionRestClient
      .img2img(new StableDiffusionRestClient.StableDiffusionRequest(encodedString))
      .onItem()
      .transform(response -> response.images().stream().findFirst().orElseThrow(NoSuchElementException::new));
  }
}
