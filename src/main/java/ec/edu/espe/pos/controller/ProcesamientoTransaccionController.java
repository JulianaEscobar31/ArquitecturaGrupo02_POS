package ec.edu.espe.pos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import ec.edu.espe.pos.service.TransaccionService;
import ec.edu.espe.pos.controller.dto.GatewayTransaccionDTO;
import ec.edu.espe.pos.controller.dto.TransaccionRespuestaDTO;
import ec.edu.espe.pos.controller.mapper.TransaccionMapper;
import ec.edu.espe.pos.model.Transaccion;
import ec.edu.espe.pos.exception.InvalidDataException;
import ec.edu.espe.pos.exception.TarjetaInvalidaException;

import jakarta.validation.Valid;

@CrossOrigin(origins = "https://arquitectura-grupo02-pos.vercel.app/", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
@RestController
@RequestMapping("/v1/procesamiento-transaccion")
@Tag(name = "Procesamiento de Transacciones", description = "API para procesar transacciones de pago en el POS")
public class ProcesamientoTransaccionController {

    private static final Logger log = LoggerFactory.getLogger(ProcesamientoTransaccionController.class);
    private final TransaccionService transaccionService;
    private final TransaccionMapper transaccionMapper;

    public ProcesamientoTransaccionController(TransaccionService transaccionService,
            TransaccionMapper transaccionMapper) {
        this.transaccionService = transaccionService;
        this.transaccionMapper = transaccionMapper;
    }

    @Operation(summary = "Procesar una nueva transacción de pago", description = "Procesa una transacción de pago con los datos de la tarjeta y opciones de diferido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción procesada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransaccionRespuestaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransaccionRespuestaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransaccionRespuestaDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransaccionRespuestaDTO.class)))
    })
    @PostMapping("/procesar")
    public ResponseEntity<TransaccionRespuestaDTO> procesarPago(@Valid @RequestBody GatewayTransaccionDTO request) {
        log.info("Recibiendo petición para procesar pago: {}", request);

        try {
            Transaccion transaccion = new Transaccion();
            transaccion.setMonto(request.getMonto());
            transaccion.setMarca(request.getMarca());

            Transaccion transaccionProcesada = transaccionService.crear(
                transaccion,
                request.getDatosTarjeta(),
                request.getInteresDiferido(),
                request.getCuotas()
            );

            return ResponseEntity.status(201).body(TransaccionRespuestaDTO.builder()
                    .mensaje("Transacción procesada exitosamente")
                    .estado(transaccionProcesada.getEstado())
                    .codigoUnicoTransaccion(transaccionProcesada.getCodigoUnicoTransaccion())
                    .build());

        } catch (TarjetaInvalidaException e) {
            log.error("Error en validación de tarjeta: {}", e.getMessage());
            return ResponseEntity.status(405).body(TransaccionRespuestaDTO.builder()
                    .mensaje(e.getMessage())
                    .estado("RECHAZADA")
                    .build());
        } catch (InvalidDataException e) {
            log.error("Error en datos de entrada: {}", e.getMessage());
            return ResponseEntity.badRequest().body(TransaccionRespuestaDTO.builder()
                    .mensaje(e.getMessage())
                    .estado("ERROR")
                    .build());
        } catch (Exception e) {
            log.error("Error inesperado al procesar pago: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(TransaccionRespuestaDTO.builder()
                    .mensaje("Error interno del servidor")
                    .estado("ERROR")
                    .build());
        }
    }
}