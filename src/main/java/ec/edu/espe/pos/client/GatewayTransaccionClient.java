package ec.edu.espe.pos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import ec.edu.espe.pos.controller.dto.GatewayTransaccionDTO;

@FeignClient(name = "gateway-transaccion", url = "http://3.139.233.22")
public interface GatewayTransaccionClient {

    @PostMapping("/v1/transacciones/sincronizar")
    @ResponseBody
    ResponseEntity<String> sincronizarTransaccion(@RequestBody GatewayTransaccionDTO transaccion);
}
