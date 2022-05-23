package com.cqut.livechat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

/**
 * @author Augenstern
 * @date 2022/5/21
 */
@Controller
public class DemoController {

    public String greeting(String message) {
        return message + " from server";
    }
}
