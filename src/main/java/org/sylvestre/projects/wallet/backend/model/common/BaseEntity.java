package org.sylvestre.projects.wallet.backend.model.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Optional;

/**
 *
 * @author sylvestre
 */
@MappedSuperclass
public abstract class BaseEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEntity.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "active")
    private Boolean active;

    public BaseEntity() {
    }

    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Boolean getActive() {
        return active;
    }

    /**
     *
     * @param active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    protected String toJSON() {
        try {
            final String result = new ObjectMapper().writeValueAsString(this);
            return result.replace("'", "\\\'");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    @PrePersist
    protected void onPrePersist() {
        try {
            Optional.of(
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest()
            );

        } catch (IllegalStateException e) {
            LOGGER.error(String.format("IllegalStateException --> %s", Arrays.toString(e.getStackTrace())));
        }

    }

    @PreUpdate
    protected void onPreUpdate() {
        try {
            Optional.of(
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest()
            );
        } catch (IllegalStateException e) {
            LOGGER.error("IllegalStateException / onPreUpdate", Arrays.toString(e.getStackTrace()));
        }
    }
}
