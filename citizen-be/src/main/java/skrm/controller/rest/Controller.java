package skrm.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.models.Response;
import skrm.controller.request.*;
import skrm.controller.request.Error;
import skrm.service.APICaller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Controller {

    

    @Autowired
    APICaller apiCaller = new APICaller() ;
   


    @GetMapping("/getUsuarioBF")
    public ResponseEntity<Object> getUsuarioBF(@RequestParam String email) throws IOException {

        if (email.isEmpty()) {
            System.out.println("Error: Se ha recibido un email vacio");
        }

        Object respuesta = apiCaller.getUserInfo(email);
        if (respuesta == null) {
            return ResponseEntity.ok(respuesta);
        }
        if (!(respuesta instanceof ciudadano)) {
            int code = (int) respuesta;
            return ResponseEntity.status(code).build();
        }
        return ResponseEntity.ok((ciudadano) respuesta);
    }

    @GetMapping("/getAyudasBF")
    public ResponseEntity<Object> getAyudasBF() throws IOException {

        Object respuesta = apiCaller.getAyudas(); 
        if (respuesta == null) {
            return ResponseEntity.ok(respuesta);
        }    
        if (!(respuesta instanceof ArrayList<?>)) {
            int code = (int) respuesta;
            return ResponseEntity.status(code).build();
        }
        return ResponseEntity.ok((ArrayList<?>)respuesta);
    }
    @GetMapping("/getSolicitudesUserBF")
    public ResponseEntity<Object> getSolicitudesUserBF(@RequestParam String email) throws IOException {
        Object res = apiCaller.getSolicitudesUser(email);
        if (res == null){
            return ResponseEntity.ok(res);
        }
        if ( ! (res instanceof ArrayList<?>)) {
            int code  = (int) res;
            return ResponseEntity.status(code).build();
        }
        return ResponseEntity.ok((ArrayList<?>) res);

    }
    @GetMapping("/getSolicitudIdBF")
    public ResponseEntity<Object> getSolicitudIdBf(@RequestParam String idSolicitud) throws IOException {
        Object res = apiCaller.getSolicitudId(idSolicitud);
        if (res == null){
            return ResponseEntity.ok(res);
        }
        if ( ! (res instanceof Solicitud)) {
            int code  = (int) res;
            return ResponseEntity.status(code).build();
        }
        return ResponseEntity.ok((Solicitud) res);
        //despues

    }
    @GetMapping("/getAyudaIdBF")
    public ResponseEntity<Object> getAyudaIdBF(@RequestParam String idAyuda) throws IOException {
        Object res = apiCaller.getAyudaId(idAyuda);
        if (res == null){
            return ResponseEntity.ok(res);
        }
        if ( ! (res instanceof Ayuda)) {
            int code  = (int) res;
            return ResponseEntity.status(code).build();
        }
        return ResponseEntity.ok((Ayuda) res);
        //despues

    }


     @PostMapping(value ="/validarDocumentosBF", consumes ="multipart/form-data" )
            public ResponseEntity<Object> validarDocumentosBF(
            @RequestPart("solicitudId") String idSolicitud,
            @RequestPart("ayudaId") String idAyuda,
            @RequestPart("usuarioId") String email,
            @RequestPart("coordenadas") String coordenadas,
            @RequestPart("dimension") String area,
            @RequestPart("subvencionSol") String subvencionSol, 
            @RequestPart("ubicacionInstalacion") String ubicacionInstalacion,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
            ) throws IOException
    {   if(files == null){
          return ResponseEntity.ok("Files is null, if you are using swagger-ui, check version.If not,something went wrong ");
    }
        //Object mensaje = apiCaller.validarDocumentos(idAyuda,idSolicitud, email, coordenadas, area, subvencionSol,ubicacionInstalacion ,files);
        Object mensaje = apiCaller.validarDocumentos(idAyuda,idSolicitud, email, coordenadas, area, subvencionSol,ubicacionInstalacion ,files);

        if (mensaje == null) {
            return ResponseEntity.ok(mensaje);
        }
        if(mensaje instanceof String ){
            return ResponseEntity.ok(mensaje);
        }
        if (!(mensaje instanceof Error)) {
            int code  = (int) mensaje;
            return ResponseEntity.status(code).build();
        }
        return ResponseEntity.ok( (Error) mensaje);
    }
     @PostMapping(value ="/crearSolicitud",consumes ="multipart/form-data"  )
              public ResponseEntity<Object> crearSolicitud( 
                @RequestPart("ayudaId")String idAyuda, 
              @RequestPart("coordenadas")String coordenadas, 
              @RequestPart("dimension")String area, 
              @RequestPart("usuarioId")String email, 
              @RequestPart("subvencionSol")String subvencionSol, 
              @RequestPart("ubicacionInstalacion")String ubicacionInstalacion){
                
        Object mensaje = apiCaller.crearSolicitud(idAyuda, coordenadas, area, email, subvencionSol, ubicacionInstalacion);
        
        if (mensaje == null) {
            return ResponseEntity.ok(mensaje);
        }
        if (!(mensaje instanceof String)) {
            int code  = (int) mensaje;
            return ResponseEntity.status(code).build();
        }
        return ResponseEntity.ok( (String) mensaje);



    }

        
    @GetMapping("/validarTokenBF")
    public ResponseEntity<Object> validarTokenBF(@RequestHeader ("Authorization") String bearerToken, @RequestParam String email) throws IOException {

        if (bearerToken.isEmpty() || email.isEmpty()) {
            System.out.println("Error: Se ha recibido un parametro vacio");
        }
        String token = bearerToken.substring(7);
        Object respuesta = apiCaller.validarToken(token, email); 
        if (respuesta == null) {
            return ResponseEntity.ok(respuesta);
        }
        if (!(respuesta instanceof Boolean)) {
            int code = (int) respuesta;
            return ResponseEntity.status(code).build();
        }
        return ResponseEntity.ok((boolean) respuesta);
    }

    @GetMapping("/setVistoBF")
    public ResponseEntity<Object> setVistoBF(@RequestParam String solicitudId) throws IOException {
        Object res = apiCaller.setVisto(solicitudId);
        Boolean visto = null;

        if (res == null){
            return ResponseEntity.ok(res);
        }
            
        if (res instanceof Integer) {
            int intValue = (int) res;
            
            if (intValue == 0) {
                visto = true;
            } else if (intValue == -1) {
                visto = false;
            }
        }
        
        // Si no se cumple ninguna condición, se devuelve un código de respuesta no válido (por ejemplo, 400 Bad Request)
        return ResponseEntity.ok((boolean) visto);
    }

        
}

