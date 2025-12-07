package com.zsf.agent.controller;


import com.alibaba.fastjson2.JSONObject;
import com.zsf.agent.service.PdfService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai/pdf")
public class PdfController {

    @Autowired
    VectorStore vectorStore;

    // 文件保存路径（可配置在application.yml）
    @Value("${pdf.upload.path:./pdf_uploads}")
    private String pdfUploadPath;

    @Autowired
    PdfService pdfService;

    @Autowired
    ChatClient pdfChatClient;

    /**
     * 接收PDF上传请求（带路径变量uploadChatId）
     *
     * @param file 前端上传的PDF文件
     * @return 包含chatId的响应结果
     */
    @PostMapping("/upload/{chatId}")
    public ResponseEntity<Map<String, Object>> uploadPdf(
            @PathVariable("chatId") String chatId,
            @RequestParam("file") MultipartFile file) {
        Map<String, Object> result = null;
        try {
            // 使用file参数，避免编译器警告
            result = pdfService.processPdfUpload(chatId, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(result);

    }
    @Value("${pdf.upload.directory:./uploads/pdf}")
    private String uploadDirectory;
    @Autowired
    ChatMemory agentChatMemory;
    @GetMapping("/chat")
    public Flux<String > chat(
            @RequestParam("prompt") String prompt,@RequestParam("chatId") String chatId,@RequestParam("fileName") String fileName) throws MalformedURLException {

        System.out.println("prompt:"+prompt);
        System.out.println("chatId:"+chatId);
        System.out.println("fileName:"+fileName);

        Path filePath = Paths.get(uploadDirectory, chatId+"_"+fileName);
        FileUrlResource fileUrlResource = new FileUrlResource(filePath.toString());
        PagePdfDocumentReader reader = new PagePdfDocumentReader(fileUrlResource, PdfDocumentReaderConfig.defaultConfig());
        List<Document> read = reader.read();
        TextSplitter splitter = new TokenTextSplitter();
        List<Document> apply = splitter.apply(read);
        System.out.println("read:"+ JSONObject.toJSONString(read));

        // 分批添加文档到向量存储，每批最多10个文档
        int batchSize = 10;
        for (int i = 0; i < apply.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, apply.size());
            List<Document> batch = apply.subList(i, endIndex);
            vectorStore.add(batch);
        }

        return pdfChatClient.prompt().user(prompt)
                .advisors(new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(agentChatMemory).build(),
                        QuestionAnswerAdvisor.builder(vectorStore).searchRequest(
                                SearchRequest.builder().query(prompt).topK(5).similarityThreshold(0.5).build()
                        ).build()
                ).stream().content();


    }

}