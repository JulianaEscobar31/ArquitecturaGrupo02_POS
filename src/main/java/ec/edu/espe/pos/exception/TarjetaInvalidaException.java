package ec.edu.espe.pos.exception;

public class TarjetaInvalidaException extends RuntimeException {

    public TarjetaInvalidaException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Error en la validación de la tarjeta: " + super.getMessage();
    }
} 