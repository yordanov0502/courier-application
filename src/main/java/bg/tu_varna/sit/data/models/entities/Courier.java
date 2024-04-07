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
@Table(name = "couriers")
public class Courier implements UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Courier_SEQ")
    @SequenceGenerator(name = "Courier_SEQ", allocationSize = 1)
    @Column(name = "id_courier", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "years_experience", nullable = false)
    private Integer yearsExperience;

    //? "optional=true" (office association is optional for a Courier, meaning a Courier can exist without being linked to an Office)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "office_id",referencedColumnName = "id_office", nullable = true)
    private Office office;

}