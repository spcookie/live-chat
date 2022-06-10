package com.cqut.livechat.entity.auth;

import com.cqut.livechat.entity.BaseEntity;
import com.cqut.livechat.entity.user.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "chat_user")
public class User extends BaseEntity implements UserDetails {

    @Column(name = "chat_user_name", length = 11, unique = true, nullable = false)
    private String username;
    @Column(name = "chat_user_password", nullable = false)
    private String password;
    @Column(name = "chat_user_non_expired", length = 1, nullable = false)
    private Boolean accountNonExpired;
    @Column(name = "chat_user_non_locked", length = 1, nullable = false)
    private Boolean accountNonLocked;
    @Column(name = "chat_user_enabled", length = 1, nullable = false)
    private Boolean enabled;
    @Transient
    private Boolean credentialsNonExpired = true;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "chat_user_and_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @JsonIgnore
    private Set<Role> roles;
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "chat_account_id", nullable = false, unique = true)
    private Account account;

    @Transient
    private HashSet<String> auth = new HashSet<>();
    @Transient
    @JsonIgnore
    private HashSet<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (ObjectUtils.isEmpty(authorities)) {
            authorities = new HashSet<>();
            auth.forEach(val -> authorities.add(new SimpleGrantedAuthority(val)));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
