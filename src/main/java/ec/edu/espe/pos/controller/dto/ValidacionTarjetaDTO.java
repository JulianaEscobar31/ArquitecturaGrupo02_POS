package ec.edu.espe.pos.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Información para validar una tarjeta")
public class ValidacionTarjetaDTO {

    @NotBlank(message = "El número de tarjeta es requerido")
    @Pattern(regexp = "^[0-9]{6}$", message = "El número de tarjeta debe tener 6 dígitos")
    @Schema(description = "Número de tarjeta", example = "431411")
    private String numero;

    @NotBlank(message = "La fecha de caducidad es requerida")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "La fecha de caducidad debe tener el formato MM/YY")
    @Schema(description = "Fecha de caducidad de la tarjeta", example = "01/30")
    private String fechaCaducidad;

    @NotBlank(message = "El CVV es requerido")
    @Pattern(regexp = "^[0-9]{3}$", message = "El CVV debe tener 3 dígitos")
    @Schema(description = "Código de seguridad de la tarjeta", example = "123")
    private String cvv;
} 