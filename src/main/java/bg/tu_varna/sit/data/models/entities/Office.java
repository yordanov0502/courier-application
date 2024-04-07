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
@Table(name = "offices")
public class Office{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Office_SEQ")
    @SequenceGenerator(name = "Office_SEQ", allocationSize = 1)
    @Column(name = "id_office", nullable = false)
    private Integer id;

    @Column(name = "city")
    private String city;

    @Column(name = "office_name")
    private String officeName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "employee_count")
    private Integer employeeCount;

}