package ma.enset.mvc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Le nom est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom doit contenir entre 3 et 50 caractères")
    private String name;

    @Min(value = 0, message = "Le prix doit être positif")
    private double price;

    @Min(value = 1, message = "La quantité doit être au moins 1")
    private double quantity;
}
