package bg.tu_varna.sit.data.models.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "orders")
public class Order{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Order_SEQ")
    @SequenceGenerator(name = "Order_SEQ",allocationSize = 1)
    @Column(name = "id_order", nullable = false)
    private Integer id;

    @Column(name = "created_at",updatable = false, nullable = false)
    @CreationTimestamp
    //@UpdateTimestamp //? this annotation is used to ensure that every time the entity is updated and saved, the updatedAt field will automatically be set to the current timestamp.
    private Date createdAt;

    //? "optional=true" (customer association is optional for an Order, meaning an Order can exist without being linked to a Customer)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id",referencedColumnName = "id_customer", nullable = true)
    private Customer customer;

    //? "optional=true" (courier association is optional for an Order, meaning an Order can exist without being linked to a Courier)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "courier_id",referencedColumnName = "id_courier", nullable = true)
    private Courier courier;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn(name = "status_id", referencedColumnName = "id_status")
    private Status status;

}