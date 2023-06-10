package skrm.controller.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skrm.controller.response.StringResponse;
import skrm.controller.request.Error;
import org.springframework.web.multipart.MultipartFile;

//verificarAcceso

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HealthController {

    @PostMapping(value ="/enviarDocumentos", consumes ="multipart/form-data")
    public ResponseEntity<Object> enviarDocumentos( @RequestPart("id") String id,
    @RequestPart("email") String email,
    @RequestPart(value = "files", required = false) List<MultipartFile> files){
            Error res;
            if(files == null){
                return ResponseEntity.ok("Files is null, if you are using swagger-ui, check version.If not,something went wrong ");

            }
            if(files.size() == 3){
                res = new Error("0","ok");
            }else{
                res = new Error("-1","Formato_incorrecto de archivo" +" "+files.get(0).getOriginalFilename());

            }
            
            return ResponseEntity.ok( (Error) res);

    }
    @PostMapping("/postDocumentos")
    public ResponseEntity<StringResponse> postDocumentos(){
        StringResponse response = new StringResponse("Todo Correcto desde OCR");

        return new ResponseEntity<StringResponse>(response,HttpStatus.OK);
   
    }    
    

}
