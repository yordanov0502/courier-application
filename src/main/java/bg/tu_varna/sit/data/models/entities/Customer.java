package bg.tu_varna.sit.data.models.entities;

import bg.tu_varna.sit.data.models.enums.user.Role;
import lombok.*;

import javax.persistence.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "customers")
public class Customer implements UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Customer_SEQ")
    @SequenceGenerator(name = "Customer_SEQ", allocationSize = 1)
    @Column(name = "id_customer", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

}