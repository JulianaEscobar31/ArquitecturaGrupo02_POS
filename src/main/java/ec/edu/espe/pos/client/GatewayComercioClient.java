package ec.edu.espe.pos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ec.edu.espe.pos.controller.dto.FacturacionComercioDTO;

@FeignClient(name = "gateway-comercio", url = "http://3.139.233.22")
public interface GatewayComercioClient {
    
    @GetMapping("/v1/comercios/{codigoComercio}/facturacion")
    FacturacionComercioDTO obtenerFacturacionPorComercio(@PathVariable Integer codigoComercio);
} 