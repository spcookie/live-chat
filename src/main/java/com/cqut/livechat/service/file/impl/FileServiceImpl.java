package com.cqut.livechat.service.file.impl;

import cn.hutool.core.util.IdUtil;
import com.cqut.livechat.LiveChatException;
import com.cqut.livechat.config.properties.FileProperties;
import com.cqut.livechat.dto.file.FileDto;
import com.cqut.livechat.entity.message.ChatFileMessage;
import com.cqut.livechat.entity.user.Account;
import com.cqut.livechat.repository.message.ChatFileMessageRepository;
import com.cqut.livechat.service.BaseService;
import com.cqut.livechat.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Augenstern
 * @date 2022/6/12
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl extends BaseService implements FileService {

    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private ChatFileMessageRepository fileMessageRepository;

    @Override
    public Optional<String> saveFileToDist(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String path = null;
        if (originalFilename != null) {
            Long id = super.getLoginUserId();
            // 保存文件的路径
            String dist = fileProperties.getDist();
            // 文件后缀名
            String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
            //TODO: 文件类型合法安全校验


            // uuid全局唯一名称
            String uuid = IdUtil.fastSimpleUUID();
            path = dist + "/" + id + "/";
            File filePath = new File(path);
            // 若不存在该用户的文件保存路径，则创建一个
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            path = "/" + id + "/" + uuid + suffix;
            File newFile = new File(filePath, uuid + suffix);
            try {
                // 保存文件到该用户目录下
                file.transferTo(newFile);
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
                throw new LiveChatException("文件上传失败");
            }
        }
        // 返回文件保存的真实路径
        return Optional.ofNullable(path);
    }

    @Override
    public FileDto downloadFile(Long id) throws FileNotFoundException {
        // 拼接文件真实路径
        Optional<ChatFileMessage> fileMessage = fileMessageRepository.findById(id);
        FileDto fileDto = new FileDto();
        if (fileMessage.isPresent()) {
            ChatFileMessage message = fileMessage.get();
            boolean canDownloadFile = canIDownloadFiles(message);
            if (!canDownloadFile) {
                throw new LiveChatException("非法下载文件");
            }
            String path = fileProperties.getDist() + message.getPath();
            // 获取文件输入流
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamResource resource = new InputStreamResource(fileInputStream);
            // 封装对象
            fileDto.setFileSize(file.length());
            fileDto.setInput(resource);
            // 查询真实文件名
            fileDto.setRealName(message.getOriginalFileName());
        }
        return fileDto;
    }

    private boolean canIDownloadFiles(ChatFileMessage message) {
        Account from = message.getFrom();
        Account target = message.getTarget();
        Long userId = super.getLoginUserId();
        return Objects.equals(userId, from.getId()) || Objects.equals(userId, target.getId());
    }
}
