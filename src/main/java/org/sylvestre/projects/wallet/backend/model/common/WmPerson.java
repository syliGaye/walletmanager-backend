package org.sylvestre.projects.wallet.backend.model.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 *
 * @author sylvestre
 */
@MappedSuperclass
public abstract class WmPerson extends BaseEntity {
    /**
     * Le prénom
     */
    @Column(name = "firstname")
    private String firstname;

    /**
     * Le nom
     */
    @Column(name = "lastname")
    private String lastname;

    /**
     * La date de naissance
     */
    @Column(name = "birth_date")
    private Date birthDate;

    /**
     * Le numéro de téléphone
     */
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    /**
     * L'adresse email
     */
    @Column(name = "email", unique = true)
    private String email;

    /**
     *
     */
    public WmPerson() {
    }

    /**
     *
     * @return
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     *
     * @param firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     *
     * @return
     */
    public String getLastname() {
        return lastname;
    }

    /**
     *
     * @param lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     *
     * @return
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     *
     * @param birthDate
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     *
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
