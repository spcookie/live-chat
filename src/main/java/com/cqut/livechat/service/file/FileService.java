package com.cqut.livechat.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * @author Augenstern
 * @date 2022/6/12
 */
public interface FileService {
    /**
     * 保存文件到目录
     * @param file 文件
     * @return 文件实际路径
     */
    Optional<String> saveFileToDist(MultipartFile file);
}
