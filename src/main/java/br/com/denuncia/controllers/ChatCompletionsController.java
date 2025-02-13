package br.com.denuncia.controllers;

import br.com.denuncia.dto.ChatCompletionRequestDTO;
import br.com.denuncia.dto.ChatCompletionResponseDTO;
import br.com.denuncia.dto.MessageDTO;
import br.com.denuncia.model.OpenApiClient;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api/chat")
public class ChatCompletionsController {

    @Value("${openai.model}")
    private String model;

    @Autowired
    private OpenApiClient openApiClient;

    private static Map<String, List<String>> mapMessage;

    static {
        mapMessage = new HashMap<>();
        mapMessage.put("user", new ArrayList<>());
        mapMessage.put("assistant", new ArrayList<>());
    }

    @PostMapping("/ask")
    public String askQuestion(@RequestBody String question) {
        ChatCompletionRequestDTO requestDTO = new ChatCompletionRequestDTO();
        requestDTO.setModel(model);
        mapMessage.get("user").add(question);

        List<MessageDTO> messagesSend = new ArrayList<>();
        messagesSend.add(new MessageDTO("system", getIntrucoesSystem()));
        List<String> messagesUser = mapMessage.get("user");
        List<String> messagesAssistant = mapMessage.get("assistant");

        for (int i = 0; i < messagesUser.size(); i++) {
            messagesSend.add(new MessageDTO("user", messagesUser.get(i)));

            try {
                messagesSend.add(new MessageDTO("assistant", messagesAssistant.get(i)));
            } catch (IndexOutOfBoundsException e) {
            }
        }

        System.out.println(new Gson().toJson(messagesSend));
        requestDTO.setMessages(messagesSend);

        ResponseEntity<String> response = openApiClient.getChatCompletionResponse(requestDTO);
        ChatCompletionResponseDTO chatCompletionResponseDTO = new Gson().fromJson(response.getBody(), ChatCompletionResponseDTO.class);

        System.out.println("-------------------------------------");
        System.out.println(chatCompletionResponseDTO);

        String respostaOpenAI = chatCompletionResponseDTO
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();

        mapMessage.get("assistant").add(respostaOpenAI);
        return respostaOpenAI;
    }

    private static String getIntrucoesSystem() {
        return "Assuma o papel de um assistente para atendimentos de disque denúncia respondendo assuntos relacionados a Homicídios, Furto e Feminicídio\n" +
                "            Para melhor atender a seu usuário, siga as seguintes regras\n" +
                "            1 - Responda de maneira objetiva, porém gentil e profissional\n" +
                "            2 - Responda sempre em português, mesmo que o usuário pergunte em outras linguas\n" +
                "            3 - Se a pergunta não possuir relação com o tema do manual, devolva a recusa padrão\n" +
                "            4 - Não responda informando que é para ligar para o 190 ou outro número de emergência\n" +
                "            5 - Coloque a resposta em um json, separando por cabeçalho, tópicos relavantes ou de dicas, e mensagem principal\n" +
                "            Recusa padrão = Sinto muito, mas não tenho a informação para responder esta questão.";
    }
}
