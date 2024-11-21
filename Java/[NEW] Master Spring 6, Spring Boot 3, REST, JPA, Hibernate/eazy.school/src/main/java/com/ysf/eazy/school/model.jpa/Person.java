package com.ysf.eazy.school.model.jpa;

import com.ysf.eazy.school.annotation.FieldsValueMatch;
import com.ysf.eazy.school.annotation.StrongPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "person")
@NoArgsConstructor
@Getter
@Setter
@FieldsValueMatch.FieldGroup({
        @FieldsValueMatch(
                message = "Password and confirm password do not match",
                field = "password",
                matchField = "confirmPassword"
        ),
        @FieldsValueMatch(
                message = "Email and confirm email do not match",
                field = "email",
                matchField = "confirmEmail"
        )
})
public class Person extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Integer id;

    @NotBlank(message = "Name must not be blank")
    @Size(message = "Name must be at least 3 characters long")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Provided email is not valid")
    private String email;
    @Transient
    private String confirmEmail;

    @NotBlank(message = "Mobile number must not be blank")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    @Column(name = "mobile_number")
    private String mobileNumber;

    @NotBlank(message = "Password must not be blank")
    @Size(message = "Password must be at least 5 characters long")
    @StrongPassword // custom validation annotation
    @Column(name = "pwd")
    private String password;
    @Transient
    private String confirmPassword;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, targetEntity = Role.class)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Address.class)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    private Address address;
}
