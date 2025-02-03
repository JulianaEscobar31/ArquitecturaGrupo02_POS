package ec.edu.espe.pos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ec.edu.espe.pos.controller.dto.ValidacionTarjetaDTO;

@FeignClient(name = "validacionTarjeta", url = "http://18.118.126.105")
public interface ValidacionTarjetaClient {

    @PostMapping("/v1/tarjetas/validar")
    ResponseEntity<Void> validarTarjeta(@RequestBody ValidacionTarjetaDTO validacionTarjetaDTO);
} 