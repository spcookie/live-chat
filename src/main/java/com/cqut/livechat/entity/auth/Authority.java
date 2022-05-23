package com.cqut.livechat.entity.auth;

import com.cqut.livechat.entity.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "chat_authority")
public class Authority extends BaseEntity {
    @Column(name = "chat_authority_name", nullable = false, unique = true)
    private String authorityName;
    @Column(name = "chat_authority_describe")
    private String describe;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Authority authority = (Authority) o;
        return getId() != null && Objects.equals(getId(), authority.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
