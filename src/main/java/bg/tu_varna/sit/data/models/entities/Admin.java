package bg.tu_varna.sit.data.models.entities;

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
@Table(name = "admins")
public class Admin implements UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Admin_SEQ")
    @SequenceGenerator(name = "Admin_SEQ", allocationSize = 1)
    @Column(name = "id_admin", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

}