package com.cqut.livechat.entity.message;

import com.cqut.livechat.constant.MessageStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Entity
@Table(name = "chat_text_message")
public class ChatTextMessage extends CommonMessage {

    @Column(name = "chat_text", nullable = false, columnDefinition = "text")
    private String text;
    @Column(name = "chat_status", nullable = false)
    private MessageStatus messageStatus = MessageStatus.UNREAD;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        ChatTextMessage that = (ChatTextMessage) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
