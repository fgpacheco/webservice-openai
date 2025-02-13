package br.com.denuncia.model;

import br.com.denuncia.dto.ChatCompletionRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "openAiClient", url = "https://api.openai.com")
public interface OpenApiClient {
    @PostMapping("v1/chat/completions")
    ResponseEntity<String> getChatCompletionResponse(@RequestBody ChatCompletionRequestDTO request);

}
