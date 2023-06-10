package skrm.service;

import okhttp3.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skrm.controller.request.*;
import skrm.controller.request.Error;

//import java.io.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;


@Service
public class APICaller {

    @Autowired
    private OkHttpClient client = new OkHttpClient() ;

    public Object getUserInfo(String mail) throws IOException {

        String url;
        Request request;
        try {
            url = "http://ismi.fi.upm.es:8086/getUserInfo/" + mail;
            request = new Request.Builder().url(url).build();
        } catch (Exception e) {
            return null;
        }

        Response response = client.newCall(request).execute();

    //Si no se obtiene una respuesta valida
        if (!response.isSuccessful())
            return response.code();  

    //else -> tengo un json con la respuesta obtenida
        JSONObject json = new JSONObject(response.body().string());
         //Llamada a filterUserInfo
         ciudadano ciu = filterUserInfo(json);
         String url2;
         Request request2;
         try {
             url2 = "http://ismi.fi.upm.es:8086/getUsuario/" + mail;
             request2 = new Request.Builder().url(url2).build();
         } catch (Exception e) {
             return null;
         }
         Response response2 = client.newCall(request2).execute();

         //Si no se obtiene una respuesta valida
         if (!response2.isSuccessful())
             return response2.code();

         //else -> tengo un json con la respuesta obtenida
         JSONObject json2 = new JSONObject(response2.body().string());
         //System.out.println(json2.get("municipio"));

        JSONObject municipio = json2.getJSONObject("municipio");
        ciu.setMunicipio(municipio.getString("nombre"));
        JSONObject zona = municipio.getJSONObject("zona");
        String nombreZona = zona.getString("nombre");
        ciu.setZona(nombreZona);
         response2.close();
         response.close();
         return ciu;

    }

    public Object getAyudas() throws IOException {

        String url;
        Request request;
        try {
            url = "http://ismi.fi.upm.es:8086/getAyudas";
            request = new Request.Builder().url(url).build();
        } catch (Exception e) {
            return null;
        }
        
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful())
            return response.code();
        

        ArrayList<Ayuda> res = filterAyudas(response.body().string());
        response.close();
        return res;

    }
    public Object validarDocumentos(String idAyuda,String idSolicitud,String email,String coordenadas,String area, String subvencionSol, String ubicacionInstalacion,List<MultipartFile> files) throws IOException {
        try{
        if( files == null ||  files.get(0).isEmpty())
           return 400;
        String fase ;
        int faseInt;
        Error mensaje = new Error();

        if ( Integer.parseInt(idSolicitud)< 0 ){
            fase = "1" ;
        }else{
            fase = "2";
        } 
        faseInt = Integer.parseInt(fase); 
        
        MultipartBody.Builder requestBodyOCR = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", idAyuda)
                .addFormDataPart("fase", fase)
                .addFormDataPart("email", email);
        
        for (int i =0 ; i< files.size();i++) {
            String nombreArchivo = files.get(i).getOriginalFilename();
            MultipartFile multFile = files.get(i);
            File fichero = new File(multFile.getOriginalFilename());
            fichero.createNewFile();
            FileOutputStream fos = new FileOutputStream(fichero);
            fos.write(multFile.getBytes());
            fos.close();
            RequestBody archivoRequestBody = RequestBody.create(fichero,okhttp3.MediaType.parse(multFile.getContentType()));
            requestBodyOCR.addFormDataPart("files", nombreArchivo, archivoRequestBody);
        }

        

        String url = "http://ismi.fi.upm.es:5000/validar-documentos";
        

        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("multipart/form-data");
        RequestBody requestBody = requestBodyOCR.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Content-Type", mediaType.toString())
                .build();

        Response response = client.newCall(request).execute();
        //System.out.println("noLlega");   

        boolean esError = true;
        JSONObject json = null;
        JSONArray data=null;
        Object campos=null;

        

        if (!response.isSuccessful()) {
            response.close();
            return response.code();

        } else {
           
            json = new JSONObject(response.body().string());
            int codeI = (int)json.get("codigoError");
            String code = String.valueOf(codeI);
           
            
            if( code.equals("0")){
                esError=false;
                data = (JSONArray)json.get("datos");
                campos = json.get("campos");
                


            }else{
                
                mensaje.setMensaje(json.getString("mensaje"));
            }
            mensaje.setCodigoError(code);


        }
        response.close();

        if (esError) {
            return mensaje;
        }
        if( faseInt == 1){

            Object o = crearSolicitud(idAyuda, coordenadas, area,email, subvencionSol, ubicacionInstalacion);
            if ( o instanceof String){
                idSolicitud = (String ) o;
            }else{
                return o;
            }
            mensaje.setMensaje(idSolicitud);  
            
        }else{
            mensaje.setMensaje("Ok");  
        }
        
           
       
        String urlPost = "http://ismi.fi.upm.es:8086/crearDocumento";
        JSONObject jsonPost = new JSONObject();
        jsonPost.put("idSolicitud", Long.parseLong(idSolicitud));
        jsonPost.put("fase", Long.parseLong(fase));
        jsonPost.put("email", email);
        jsonPost.put("datos", data);
        jsonPost.put("campos", campos);

        
        
        RequestBody  requestBodyPost = RequestBody.create(jsonPost.toString(), MediaType.parse("application/json"));
        Request requestPost = new Request.Builder()
        .url(urlPost)
        .post(requestBodyPost)
        .build();

        Response responsePost = client.newCall(requestPost).execute();

        
        if (!responsePost.isSuccessful()) {
            responsePost.close();
            

            return responsePost.code();
        }
        System.out.println( responsePost.body().string());
        responsePost.close();
        
        return mensaje ;
    }catch(Exception e ){
        return new Error("-1001", "Error en el proceso ");
    }
    }
    public Object getSolicitudesUser(String email) throws IOException {
        String url = null ;
        Request request = null ;
        Response response=null;
        try {
            url = "http://ismi.fi.upm.es:8086/getSolicitudes?userId=" + email;
            request = new Request.Builder().url(url).build();
        } catch (Exception e) {
            return null;
        }
        response = client.newCall(request).execute();
        if (!response.isSuccessful()){
            return response.code();
        }
        Object res = filterSolicitudes(response.body().string());
        response.close();

        return res;
    }
    public Object getSolicitudId(String id) throws IOException {
        String url = null ;
        Request request = null ;
        Response response=null;

        try {
            url = "http://ismi.fi.upm.es:8086/getSolicitud/" + id;

            request = new Request.Builder().url(url).build();
        } catch (Exception e) {
            System.out.println("algovamal");
            return null;
        }
        response = client.newCall(request).execute();
        
        if (!response.isSuccessful()){
            return response.code();
        }
        ArrayList<Solicitud> solicitud = filterSolicitudes(response.body().string());
        Solicitud res = null;
        if (!solicitud.isEmpty()){
            res = solicitud.get(0);
        }
        response.close();

        return res;
    }


    public Object getAyudaId(String id) throws IOException {
        String url = null ;
        Request request = null ;
        Response response=null;

        try {
            url = "http://ismi.fi.upm.es:8086/getAyudas/?ayudaId=" + id;

            request = new Request.Builder().url(url).build();
        } catch (Exception e) {
            return null;
        }
        response = client.newCall(request).execute();
        
        if (!response.isSuccessful()){
            return response.code();
        }
        ArrayList<Ayuda> ayuda = filterAyudas(response.body().string());
        Ayuda res = null;
        if (!ayuda.isEmpty()){
            res = ayuda.get(0);
        }
        response.close();

        return res;
    }
    public Object validarDocumentosMoc(String idAyuda,String idSolicitud,String email,String coordenadas,String area, String subvencionSol, 
    String ubicacionInstalacion,List<MultipartFile> files) throws IOException {
        String msg , code;
        Error res = new Error();
        if(Integer.parseInt(idAyuda)%2==0){
            code = "0";
            msg = idAyuda + "," + idSolicitud + "," + email + ","+ coordenadas+ "," + area + "," + subvencionSol + "," + ubicacionInstalacion;

        }else{
            switch(files.size()){
                case 0 : code = "-1";
                msg = "Numero de ficheros insuficientes ";
                break;
                case 1 :
                code = "-2";
                msg = "Campos Incorrectos     ";
                break;
                case 2 :
                code = "-3";
                msg = "Campos no coinciden ";
                break;
                default:
                code = "-4";
                msg = "Formato incorrecto ";
                break;

            }

        }
        res.setCodigoError(code);
        res.setMensaje(msg);
        return res;

    }
    public Object crearSolicitud(String idAyuda, String coordenadas, String area, String email, String subvencionSol, String ubicacionInstalacion){
        
        String url="http://ismi.fi.upm.es:8086/crearSolicitud/",idSolicitud="";
        

        JSONObject jsonCreate = new JSONObject();
        jsonCreate.put("ayudaId", idAyuda);
        jsonCreate.put("coordenadas", coordenadas);
        jsonCreate.put("usuarioId", email);
        jsonCreate.put("subvencionSol", subvencionSol);
        jsonCreate.put("dimension",area);
        jsonCreate.put("ubicacionInstalacion", ubicacionInstalacion);

        try{
        RequestBody  requestBodyCreate = RequestBody.create(jsonCreate.toString(), MediaType.parse("application/json"));
        Request requestCreate = new Request.Builder()
        .url(url)
        .post(requestBodyCreate)
        .build();

        Response responseCreate = client.newCall(requestCreate).execute();
        if (!responseCreate.isSuccessful()) {
            responseCreate.close();
            int n = responseCreate.code();
            System.out.println(n);
            return n;
        }


    
    
        idSolicitud = responseCreate.body().string();
        responseCreate.close();
    }catch(Exception e){}
    return idSolicitud;

}
    public ArrayList<Solicitud> filterSolicitudes(String infoRecibida) throws IOException {
        //obtener un Arraylist de todas las solicitudes
        JSONObject jadd ;
        int n = contarCaracterEnString('{', infoRecibida);
        String sadd;
        Solicitud add ;
        Ayuda ayuda;
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        int indexApertura = infoRecibida.indexOf("{");
        int indexCierre = infoRecibida.indexOf("}");
        for (int i = 0; i < n; i++) {
            sadd = infoRecibida.substring(indexApertura, indexCierre+1);
            indexApertura = infoRecibida.indexOf("{", indexApertura+1);
            indexCierre = infoRecibida.indexOf("}", indexCierre+1);
            jadd = new JSONObject(sadd);
            add = new Solicitud();

            // Obtencion de datos de solicitud
            add.setApellidosSolicitante(jadd.get("apellidoSolicitante").toString());
            add.setAyudaId(jadd.get("ayudaId").toString());
            String coordenadas = jadd.get("coordenadas").toString();
            add.setCoordenadas(coordenadas);
            add.setDimension(jadd.get("dimension").toString());
            add.setEstado(jadd.get("estado").toString());
            add.setFondos(jadd.get("fondos").toString());
            add.setNombreSolicitante(jadd.get("nombreSolicitante").toString());
            add.setSolicitudId(jadd.get("solicitudId").toString());
            add.setSubvencionSol(jadd.get("subvencionSol").toString());
            add.setSupervisorId(jadd.get("supervisorId").toString());
            add.setTramitadorId(jadd.get("tramitadorId").toString());
            add.setUbicacionInstalacion(jadd.get("ubicacionInstalacion").toString());
            add.setUsuarioId(jadd.get("ubicacionInstalacion").toString());
            add.setMensaje(jadd.get("mensaje").toString());


            //obetncion datos de ayuda
            ayuda = (Ayuda) getAyudaId(add.getAyudaId()+"");
            add.setNombre(ayuda.getNombre());
            add.setDescripcion(ayuda.getDescripcion());
            add.setZonaNombre(ayuda.getZonaNombre());
            add.setPresupuestoTotal(ayuda.getPresupuestoTotal());
            add.setTipo(ayuda.getTipo());
            add.setDuracion(ayuda.getDuracion());
            add.setEsAutonomica(ayuda.getEsAutonomica());
            add.setDocsSol(ayuda.getDocsSol());
            add.setMunicipioNombre(ayuda.getMunicipioNombre());
            add.setPresupuestoConcedido(ayuda.getPresupuestoConcedido());
            add.setPresupuestoEmitido(ayuda.getPresupuestoEmitido());
            add.setNumeroSolicitudes(ayuda.getNumeroSolicitudes());
            add.setDocsObra(ayuda.getDocsObra());

            //Object objeto = imagen(centroGeo(coordenadas));
            //if (objeto != null){
            //   add.setUrlImagen((String) objeto);
            //}


            solicitudes.add(add);
        }
        ArrayList<String> estados = new ArrayList<>();
        estados.add("Subida de documentos");
        estados.add("Subida de evidencias");
        return ordenarXestados(solicitudes,estados);
    }
    
    public ciudadano filterUserInfo (JSONObject json){
        ciudadano res = new ciudadano();
        try {
            res.setApellidos(json.getString("apellidos"));
            res.setNombre(json.getString("nombre"));
            res.setDni(json.getString("dni"));
            res.setDireccion(json.getString("direccion"));


        } catch (Exception a) {
            
        }
        return res;
       
    }
    public ArrayList<Ayuda> filterAyudas(String infoRecibida) throws IOException {
        JSONObject jadd ;
        int n = contarCaracterEnString('{', infoRecibida);
        String sadd;
        Ayuda add ;
        ArrayList<Ayuda> listaAyudas = new ArrayList<>();
        int indexApertura = infoRecibida.indexOf("{");
        int indexCierre = infoRecibida.indexOf("}");
        for (int i = 0; i < n; i++) {
            sadd = infoRecibida.substring(indexApertura, indexCierre+1);
            indexApertura = infoRecibida.indexOf("{", indexApertura+1);
            indexCierre = infoRecibida.indexOf("}", indexCierre+1);
            jadd = new JSONObject(sadd);
            add = new Ayuda();
            add.setAyudaId(jadd.get("ayudaId").toString());
            add.setNombre(jadd.get("nombre").toString());
            add.setEsAutonomica(jadd.getBoolean("esAutonomica"));
            add.setTipo(jadd.get("tipo").toString());
            add.setPresupuestoTotal(jadd.get("presupuestoTotal").toString());
            add.setPresupuestoConcedido(jadd.get("presupuestoConcedido").toString());
            add.setPresupuestoEmitido(jadd.get("presupuestoEmitido").toString());
            add.setDuracion(jadd.get("duracion").toString());
            add.setDocsSol(jadd.get("documentosSol").toString());
            add.setDocsObra(jadd.get("documentosObra").toString());
            add.setNumeroSolicitudes(jadd.get("numeroSolicitudes").toString());
            add.setDescripcion(jadd.get("descripcion").toString());
            add.setSupervisorId(jadd.get("supervisorId").toString());

            String zonaId = jadd.get("zonaId").toString();
            String municipioId = jadd.get("municipioId").toString();
            Request request;
            Request request2;
            String url;
            String url2;

            try {
                url = "http://ismi.fi.upm.es:8086/getZona/" + zonaId;
                url2 = "http://ismi.fi.upm.es:8086/getMunicipios/" + zonaId;
                request = new Request.Builder().url(url).build();
                request2 = new Request.Builder().url(url2).build();
            } catch (Exception e) {
                return null;
            }
            Response response = client.newCall(request).execute();
            Response response2 = client.newCall(request2).execute();

            if (!response.isSuccessful() || !response2.isSuccessful()) 
                System.out.println("Hay error al intentar obtener la informacion");
            

            //Para el nombre de la zona !!!
            JSONArray jsonArray = new JSONArray(response.body().string()); // Crear un JSONArray a partir de la respuesta
            if( jsonArray.length() > 0){
            JSONObject jsonObject = jsonArray.getJSONObject(0); // Obtener el primer objeto JSON del arreglo
            String nombreZona = jsonObject.getString("nombre"); // Obtener el valor del atributo "nombre" como una cadena de texto

            //Para el nombre del municipio !!!
            JSONArray jsonArray2 = new JSONArray(response2.body().string());
            String nombreMunicipio= "";

            for (int j=0; j<jsonArray2.length(); j++) {
                JSONObject municipio = jsonArray2.getJSONObject(j);
                String munId = municipio.get("municipioId").toString();

                if (munId.equals(municipioId)) {
                    nombreMunicipio = municipio.getString("nombre");
                }
            }

    
            add.setZonaNombre(nombreZona);
            add.setMunicipioNombre(nombreMunicipio);
        }





            listaAyudas.add(add);
        }
        return listaAyudas;
    }
    private static int contarCaracterEnString(char caracter, String cadena) {
        int contador = 0;
        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) == caracter) {
                contador++;
            }
        }
        return contador;
    }
    public Object validarToken(String token, String email) throws IOException {
        boolean res = false;
        String url;
        Request request;
        try {
            url = "http://ismi.fi.upm.es:8086/getToken/" + email ;
            request = new Request.Builder().url(url).build();
        } catch (Exception e) {
            return null;
        }

        Response response = client.newCall(request).execute();

    //Si no se obtiene una respuesta valida
        if (!response.isSuccessful())
            return response.code();  

    //else -> tengo que comparar los tokens para ver si son iguales
        res = token.equals(response.body().string());
         response.close();
         return res;

    }
    public ArrayList<Solicitud> ordenarXestados(ArrayList<Solicitud> solicitudes,ArrayList<String> estadosFiltrar ){
        ArrayList<ArrayList<Solicitud>> lists = new ArrayList<ArrayList<Solicitud>>();
        for(int j =0; j < estadosFiltrar.size(); j++)
           lists.add(new ArrayList<Solicitud>());

        ArrayList<Solicitud> noSorted= new ArrayList<Solicitud>();
        for(int i=0;i<solicitudes.size();i++){
            boolean match=false;
            Solicitud current = solicitudes.get(i);
            for(int j=0;j<estadosFiltrar.size() && !match;j++){
                if(current.getEstado().equals(estadosFiltrar.get(j))){
                    lists.get(j).add(current);
                    match = true;
                }
            }
            if(!match){
                noSorted.add(current);
            }
            

        }
        if(!noSorted.isEmpty()){
            lists.add(noSorted);
        }
        ArrayList<Solicitud> res = aplana(lists);
        return res;
    }
    private ArrayList<Solicitud> aplana(ArrayList<ArrayList<Solicitud>> lists){
        ArrayList<Solicitud> res =  new ArrayList<Solicitud>();
        for(int i=0;i<lists.size();i++){
            for(int j=0;j<lists.get(i).size();j++)
                res.add(lists.get(i).get(j));
        }
        return res;
    }
    public String sendInfo(Datos datos) throws IOException {
        JSONObject json = new JSONObject(datos);

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder().url("http://ismi.fi.upm.es:5000/post-data").post(body).build();

        Call call = client.newCall(request);
        Response response = call.execute();

        if(!response.isSuccessful()){
            return "ERROR";
        }
        return response.body().string();
	}
    public Object imagen(float[] centro) throws IOException {
        String url1;
        Request request1;
        try {
            url1 = "https://image.maps.ls.hereapi.com/mia/1.6/mapview?c="+centro[0]+"%2C"+centro[1]+"&z=16&apiKey=EOVNQ0iH41945DVGryvcOPEB-5p0arE2N_z7RLRb98E&nodot";
            request1 = new Request.Builder().url(url1).build();
        } catch (Exception e) {
            return null;
        }

        Response response1 = client.newCall(request1).execute();
        if (!response1.isSuccessful()){
            return response1.code();
        }

        InputStream inputStream = response1.body().byteStream();
        byte[] fileContent = inputStream.readAllBytes();
        
        String base64EncodedImage = Base64.getEncoder().encodeToString(fileContent);

        String apiKey = "8992de8939281e3c40816c63297e3789";
        String uploadUrl = "https://api.imgbb.com/1/upload";
        String fileName = "imagen.png";
        String expiration = "900";

        RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("key", apiKey)
        .addFormDataPart("image", base64EncodedImage)
        .addFormDataPart("name", fileName)
        .addFormDataPart("expiration", expiration)
        .build();

        Request request = new Request.Builder()
        .url(uploadUrl)
        .post(requestBody)
        .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        if (!response.isSuccessful()){
            return response.code();
        }

        String responseBody = response.body().string();
        JSONObject jsonObj = new JSONObject(responseBody);

        // Obtener el objeto 'data'
        JSONObject dataObj = jsonObj.getJSONObject("data");

        // Obtener la URL de la imagen
        String imageUrl = dataObj.getString("url");
        response.close();
        return imageUrl;
    }
    public float[] centroGeo(String cadena) {
        // EJEMPLO DE CADENA "[[1.00,0.00],[0.00,3.00],[2.00,1.00],[2.00,3.00],[1.00,0.00]]";
        int indiceUltimaComa = cadena.lastIndexOf(",");
        String puntos = cadena.substring(0, indiceUltimaComa);
        indiceUltimaComa = puntos.lastIndexOf(",");
        puntos = puntos.substring(0, indiceUltimaComa) + "]";
        System.out.println(puntos);

        String[] coordenadas = puntos.split("\\],\\[");
        float[] latitudes = new float[coordenadas.length];
        float[] longitudes = new float[coordenadas.length];

        for (int i = 0; i < coordenadas.length; i++) {
            String[] punto = coordenadas[i].split(",");
            latitudes[i] = Float.parseFloat(punto[1].replace("]", ""));
            longitudes[i] = Float.parseFloat(punto[0].replace("[", ""));
        }
        // Calcular la media ponderada de las coordenadas
        float latitudMedia = 0.0f;
        float longitudMedia = 0.0f;
        float pesoTotal = 0.0f;
        for (int i = 0; i < latitudes.length; i++) {
            float peso = 1.0f;
            latitudMedia += peso * latitudes[i];
            longitudMedia += peso * longitudes[i];
            pesoTotal += peso;
        }
        latitudMedia /= pesoTotal;
        longitudMedia /= pesoTotal;

        // Retornar el centro geográfico como un arreglo de dos elementos
        float[] centro = { longitudMedia, latitudMedia };
        return centro;
    } 

    public Object setVisto(String idSolicitud) throws IOException {
        String url = null ;
        Request request = null ;
        Response response=null;

        try {
            url = "http://ismi.fi.upm.es:8086/setVisto/" + idSolicitud;

            request = new Request.Builder().url(url).build();
        } catch (Exception e) {
            System.out.println("algovamal");
            return null;
        }
        response = client.newCall(request).execute();
        
        if (!response.isSuccessful()){
            return response.code();
        }
        String responseBody = response.body().string();
        int visto = Integer.parseInt(responseBody); 
        
        response.close();

        return visto;
    }


}

