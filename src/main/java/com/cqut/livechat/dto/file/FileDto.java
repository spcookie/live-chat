package com.cqut.livechat.dto.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.InputStreamResource;

/**
 * @author Augenstern
 * @date 2022/6/15
 */
@Getter
@Setter
public class FileDto {
    private String realName;
    private Long fileSize;
    private InputStreamResource input;
}
