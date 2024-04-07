package bg.tu_varna.sit.data.models.entities;

import bg.tu_varna.sit.data.models.enums.status.StatusType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "statuses")
public class Status implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Status_SEQ")
    @SequenceGenerator(name = "Status_SEQ", allocationSize = 1)
    @Column(name = "id_status", nullable = false)
    private Integer id;

    @Column(name = "status_type" , nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    @Column(name = "additional_information")
    private String additionalInformation;
}