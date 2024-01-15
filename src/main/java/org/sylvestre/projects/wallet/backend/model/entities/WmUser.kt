package org.sylvestre.projects.wallet.backend.model.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.ColumnDefault
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.sylvestre.projects.wallet.backend.model.common.WmPerson
import org.sylvestre.projects.wallet.backend.utils.EncryptionUtility
import java.util.Objects
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Index
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.PrePersist
import javax.persistence.Table
import javax.persistence.Transient

/**
 *
 * @author sylvestre
 */
@Entity
@Table(
    name = "wm_user",
    indexes = [Index(
        name = "i_wm_user_username",
        columnList = "username",
        unique = true
    ), Index(name = "i_wm_user_api_key", columnList = "apiKey", unique = true)]
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
data class WmUser(
    /**
     *
     */
    @Column(name = "username", nullable = false) private var username: String = "",

    /**
     *
     */
    @Column(name = "password", nullable = false) private var password: String = "",

    /**
     *
     */
    @Column(name = "locked", nullable = false) var locked: Boolean = false,

    /**
     *
     */
    @Column(name = "enabled", nullable = false) var enabled: Boolean = false,

    /**
     *
     */
    @Column(name = "expired", nullable = false) var expired: Boolean = false,

    /**
     *
     */
    @Column(name = "credentialsExpired", nullable = false) var credentialsExpired: Boolean = false,

    /**
     *
     */
    @Column(name = "apiKey", nullable = false, updatable = false) var apiKey: String = "",

    /**
     *
     */
    @Column(name = "lang", nullable = false) var lang: String = "",

    /**
     *
     */
    @Transient
    @JsonIgnore
    private var authorities: MutableCollection<GrantedAuthority>? = null,

    /**
     *
     */
    @ManyToMany(targetEntity = WmRole::class, fetch = FetchType.EAGER)
    @JoinTable(
        name = "wm_user_role",
        joinColumns = [JoinColumn(name = "wm_user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "wm_role_id", referencedColumnName = "id")]
    )
    var roles: MutableCollection<WmRole> = mutableListOf(),

    /**
     *
     */
    @ColumnDefault("true") var firstConnection: Boolean = true
): WmPerson(), UserDetails{
    /**
     *
     */
    @PrePersist
    override fun onPrePersist() {
        super.onPrePersist()
        this.lang = "fr"
        this.enabled = true
        this.password = BCryptPasswordEncoder().encode(this.password)
        this.apiKey = Objects.requireNonNull(EncryptionUtility.sha256From(UUID.randomUUID().toString()))!!.orElse("")
    }

    /**
     *
     * @return
     */
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        if (this.authorities == null || this.authorities?.isEmpty() == true) {
            this.authorities = HashSet()
            for (role in this.roles) {
                this.authorities?.add(SimpleGrantedAuthority(java.lang.String.format("ROLE_%s", role.name)))
            }
        }
        return this.authorities as MutableCollection<GrantedAuthority>
    }

    /**
     *
     * @return
     */
    override fun getPassword(): String {
        return password
    }

    /**
     *
     * @param password
     */
    fun setPassword(password: String){
        this.password = password
    }

    /**
     *
     * @return
     */
    override fun getUsername(): String {
        return username
    }

    /**
     *
     * @param username
     */
    fun setUsername(username: String){
        this.username = username
    }

    /**
     *
     * @return
     */
    override fun isAccountNonExpired(): Boolean {
        return !expired
    }

    /**
     *
     * @return
     */
    override fun isAccountNonLocked(): Boolean {
        return !locked
    }

    /**
     *
     * @return
     */
    override fun isCredentialsNonExpired(): Boolean {
        return !credentialsExpired
    }

    /**
     *
     * @return
     */
    override fun isEnabled(): Boolean {
        return enabled
    }
}
