package com.zsf.agent.service;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PdfService {

    @Value("${pdf.upload.directory:./uploads/pdf}")
    private String uploadDirectory;

    public Map<String, Object> processPdfUpload(String chatId, MultipartFile file) throws IOException {
        // 确保目录存在
        Path directoryPath = Paths.get(uploadDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        String uniqueFilename = chatId + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDirectory, uniqueFilename);
        file.transferTo(filePath);
        FileUrlResource fileUrlResource = new FileUrlResource(filePath.toString());
//        PagePdfDocumentReader reader = new PagePdfDocumentReader(fileUrlResource, PdfDocumentReaderConfig.defaultConfig());
//        List<Document> read = reader.read();
//        TextSplitter splitter = new TokenTextSplitter();
//        splitter.apply(read);
//        System.out.println("read:"+ JSONObject.toJSONString(read));

        return new HashMap<>();

    }
}