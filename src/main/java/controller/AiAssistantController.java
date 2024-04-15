package controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import controller.dto.MessageDTO;
import dev.langchain4j.model.chat.ChatLanguageModel;
import factory.AiAssistantFactory;
import factory.ContentRetriverFactory;
import factory.DocumentAssistantFactory;
import factory.EmbeddingFactory;

@RestController
@RequestMapping("/api/chat")
public class AiAssistantController {

	@Value("${langchain.huggingface.accessToken}")
	private String token;
	
	@PostMapping
	public ResponseEntity chat(@RequestBody MessageDTO dto) {
		  ChatLanguageModel chatModel = AiAssistantFactory.createLocalChatModel();
		  var embeddingModel = EmbeddingFactory.createEmbeddingModel();
		  var embeddingStore = EmbeddingFactory.createEmbeddingStore();
		  var fileContentRetriever = ContentRetriverFactory.createFileContentRetriever(
		          embeddingModel,
		          embeddingStore,
		          "movie.txt");

		  var documentAssistant = new DocumentAssistantFactory(chatModel, fileContentRetriever);
		  String response = documentAssistant.chat("List all movies with Comedy category");
		  return ResponseEntity.ok().body(response);
	}
}
