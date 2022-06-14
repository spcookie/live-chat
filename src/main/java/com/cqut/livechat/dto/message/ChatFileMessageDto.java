package com.cqut.livechat.dto.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Augenstern
 * @date 2022/6/12
 */
@Getter
@Setter
@ToString
public class ChatFileMessageDto extends CommonMessageDto {
    private String originalFileName;
    private Long size;
    private String path;
}
