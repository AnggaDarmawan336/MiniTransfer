package Transfer.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double balance;

}
