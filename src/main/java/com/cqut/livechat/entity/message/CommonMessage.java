package com.cqut.livechat.entity.message;

import com.cqut.livechat.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "chat_common_message")
@Inheritance(strategy = InheritanceType.JOINED)
public class CommonMessage extends BaseEntity implements Serializable {
    @Column(name = "chat_from", nullable = false)
    private Long from;
    @Column(name = "chat_target", nullable = false)
    private Long target;
    @Column(name = "chat_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        CommonMessage that = (CommonMessage) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
