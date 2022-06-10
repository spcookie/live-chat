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
import javax.persistence.Transient;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
@Entity
@Table(name = "chat_image_message")
@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
public class ChatImageMessage extends CommonMessage {

    @Transient
    private String imageBase64;
    @Column(name = "chat_image", columnDefinition = "mediumblob", nullable = false)
    private byte[] image;
    @Column(name = "chat_status", nullable = false)
    private MessageStatus messageStatus = MessageStatus.UNREAD;

    public byte[] getImage() {
        if (image == null) {
            this.setImage(this.getImageBase64().getBytes(StandardCharsets.UTF_8));
        }
        return image;
    }

    public String getImageBase64() {
        if (imageBase64 == null) {
            if (image != null) {
                imageBase64 = new String(this.getImage(), StandardCharsets.UTF_8);
            }
        }
        return imageBase64;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        ChatImageMessage that = (ChatImageMessage) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
