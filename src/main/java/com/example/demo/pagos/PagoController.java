package com.example.demo.pagos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.pagos.dto.CompraDTO;
import com.example.demo.pagos.entity.Compra;
import com.example.demo.pagos.service.PagoService;
import com.example.demo.producto.entity.Producto;
import com.example.demo.producto.service.ProductoService;
import com.example.demo.security.dto.Mensaje;
import com.example.demo.security.service.UsuarioService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;

import jakarta.validation.Valid;

import org.springframework.http.HttpMethod;

@RestController
@CrossOrigin
public class PagoController {

        Producto producto;

        @Autowired
        ProductoService productoService;

        @Autowired
        PagoService pagoService;

        @Autowired
        UsuarioService usuario;

        @PostMapping("/create_preference")
        @ResponseBody
        @Transactional
        public ResponseEntity<String> ordenPago(@Valid @RequestBody CompraDTO compraDTO, BindingResult bindingResult)
                        throws MPException, MPApiException {
                if (bindingResult.hasErrors()) {
                        return new ResponseEntity(new Mensaje("Orden de compra mal generada"),
                                        HttpStatus.BAD_REQUEST);
                }
                int total = 0;
                for (Producto producto : compraDTO.getProductos()) {
                        Producto productoTemp = productoService.getOne(producto.getId()).get();
                        if (productoTemp != null && producto.getCantidad() <= 0)
                                return new ResponseEntity(new Mensaje("Producto no existe o no se encuentra en stock"),
                                                HttpStatus.BAD_REQUEST);
                        productoService.save(productoTemp);
                        total += productoTemp.getPrecio() * producto.getCantidad();
                }
                if (total != compraDTO.getPrecio())
                        return new ResponseEntity(new Mensaje("Los precios están alterados"), HttpStatus.BAD_REQUEST);

                int idUsuario = usuario.getByUsername(compraDTO.getIdUsuario()).get().getId();
                compraDTO.setIdUsuario(idUsuario + "");
                long id = pagoService.cantidadCompras() + 1;
                MercadoPagoConfig.setAccessToken(
                                "APP_USR-1235689710593848-111119-05edcff86d8db490f874116e651cf4d9-1546120194");
                compraDTO.setIdCompra((int) id);
                compraDTO.setEstadoCompra("Pendiente");
                Compra compra = new Compra(compraDTO.getIdCompra(), idUsuario, compraDTO.getPrecio(),
                                compraDTO.getEstadoCompra(), compraDTO.getProductos());
                pagoService.save(compra);
                PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                                .id(id + "")
                                .title("Pago GameTradeCol")
                                .quantity(1)
                                .pictureUrl("")
                                .currencyId("COP")
                                .unitPrice(new BigDecimal(compraDTO.getPrecio()))
                                .build();
                PreferenceBackUrlsRequest urls = PreferenceBackUrlsRequest.builder()
                                .success("http://localhost:8080/feedback")
                                .failure("http://localhost:8080/feedback")
                                .pending("http://localhost:8080/feedback").build();
                List<PreferenceItemRequest> items = new ArrayList<>();
                items.add(itemRequest);
                PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                                .items(items)
                                .backUrls(urls)
                                .autoReturn("approved")
                                .notificationUrl("https://c004-200-115-181-2.ngrok.io/notification?source_news=ipn")
                                .build();
                PreferenceClient client = new PreferenceClient();
                Preference preference = client.create(preferenceRequest);
                System.out.println(preference.getInitPoint());
                String url = preference.getInitPoint();

                return ResponseEntity.ok(url);
        }

        @PostMapping("/notification")
        public ResponseEntity<String> feedback(@RequestParam("topic") String topic, @RequestParam("id") String id) {
                String url;
                HttpEntity<?> entity = null;
                ResponseEntity<?> response = null;
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + MercadoPagoConfig.getAccessToken());
                entity = new HttpEntity<>(headers);
                List<List<String>> payments = new ArrayList<>();
                String[] jsonPayments;
                switch (topic) {
                        case "payment":
                                url = "https://api.mercadopago.com/v1/payments/" + id;
                                response = new RestTemplate().exchange(url, HttpMethod.GET,
                                                entity, Payment.class);
                                url = "https://api.mercadopago.com/merchant_orders/"
                                                + (((Payment) response.getBody()).getOrder().getId());
                                response = new RestTemplate().exchange(url, HttpMethod.GET,
                                                entity, String.class);
                                break;
                        case "merchant_order":
                                url = "https://api.mercadopago.com/merchant_orders/" + id;
                                response = new RestTemplate().exchange(url, HttpMethod.GET,
                                                entity, String.class);
                                break;
                }
                jsonPayments = response.getBody().toString().split("\\[");
                System.out.println(response.getBody().toString());
                System.out.println("PAYMENTS" + jsonPayments[1]);
                List<String> temp = Arrays.asList(jsonPayments[1].split("\\{"));
                payments.add(temp);
                temp = Arrays.asList(jsonPayments[4].split("\\{"));
                payments.add(temp);
                String idMerchant = jsonPayments[0].split("\\{")[1].split("\\,")[0];
                payments.add(temp);
                long paidAmount = 0;
                String[] val = temp.get(1).split("\\,");
                int idCompra = Integer.parseInt(val[0].substring(val[0].indexOf(":") + 2, val[0].length() - 1));
                /*
                 * for (String payment : payments.get(1)) {
                 * String[] vals = payment.split("\\,");
                 * idCompra = Integer.parseInt(vals[1].substring(vals[0].indexOf(":") + 1,
                 * vals[1].length() - 1));
                 * }
                 */
                Compra tempCompra = pagoService.findByIdCompra(idCompra).get();

                if (jsonPayments[0].split("\\{")[0].split("\\,").length > 1
                                && jsonPayments[0].split("\\{")[0].split("\\,")[1].contains("opened")) {
                        String merchant = idMerchant.substring(idMerchant.indexOf(":") + 1, idMerchant.length() - 1);
                        tempCompra.setEstadoCompra("Pendiente: " + merchant);
                } else {

                        if (!payments.get(0).isEmpty()) {
                                for (String payment : payments.get(0)) {
                                        String[] vals = payment.split("\\,");
                                        if (vals.length >= 5 && vals[5].contains("approved")) {
                                                paidAmount += Long.valueOf(
                                                                vals[1].substring(vals[1].indexOf(":") + 1,
                                                                                vals[1].length() - 1));
                                        }
                                }
                        }
                        System.out.println(
                                        "PaidAmount: " + paidAmount + " PrecioCompra:" + tempCompra.getPrecioCompra());
                        if (paidAmount >= tempCompra.getPrecioCompra()) {
                                String merchant = idMerchant.substring(idMerchant.indexOf(":") + 1,
                                                idMerchant.length() - 1);
                                tempCompra.setEstadoCompra("Aprovado: " + merchant);
                        } else {
                                String merchant = idMerchant.substring(idMerchant.indexOf(":") + 1,
                                                idMerchant.length() - 1);
                                tempCompra.setEstadoCompra("Rechazado: " + merchant);
                                for (Producto producto : tempCompra.getProductos()) {
                                        producto.setCantidad(producto.getCantidad() + 1);
                                        productoService.save(producto);
                                }

                        }
                }
                pagoService.save(tempCompra);
                return new ResponseEntity<>("Respuesta exitosa", HttpStatus.OK);
        }

        @GetMapping("/feedback")
        public ResponseEntity<String> feedbackGet() {
                System.out.println("feedback GET");
                String responseMessage = "Datos recibidos correctamente. GET Puedes mostrar un mensaje de confirmación aquí.";

                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }

        @GetMapping("/compras/{usuario}")
        public ResponseEntity<?> obtenerCompras(@PathVariable("usuario") String usuario) {
                int idUsuario = this.usuario.getByUsername(usuario).get().getId();
                List<Compra> list = pagoService.findByIdUsuario(idUsuario);
                return new ResponseEntity(list, HttpStatus.OK);
        }
}
