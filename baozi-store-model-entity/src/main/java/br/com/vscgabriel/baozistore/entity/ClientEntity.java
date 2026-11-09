package br.com.vscgabriel.baozistore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
public class ClientEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "documentnumber", nullable = false, unique = true, length = 30)
    private String documentNumber;

    @Column(name = "areaphonecode", nullable = false)
    private Integer areaPhoneCode;

    @Column(name = "phonenumber", nullable = false, length = 9)
    private String phoneNumber;

}
