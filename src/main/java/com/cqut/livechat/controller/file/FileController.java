package com.cqut.livechat.controller.file;

import com.cqut.livechat.dto.file.FileDto;
import com.cqut.livechat.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

/**
 * @author Augenstern
 * @date 2022/6/15
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/download/{id}")
    public ResponseEntity<Object> downloadFile(@PathVariable("id") Long id) throws FileNotFoundException {
        FileDto fileDto = fileService.downloadFile(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
        headers.add("Content-Disposition", String.format("attachment;filename=%s", fileDto.getRealName()));
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileDto.getFileSize())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(fileDto.getInput());
    }
}
