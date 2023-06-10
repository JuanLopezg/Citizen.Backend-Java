package skrm.service;


import java.io.IOException;

import org.json.JSONObject;





public class main {

	public static void main(String[] args) throws IOException {
      
        APICaller apiCaller = new APICaller() ;
		JSONObject jsonUsuario = new JSONObject();
		jsonUsuario.put("apellidos", "Gomez Perez");
		jsonUsuario.put("nombre", "Ana");
		jsonUsuario.put("dni", "12345678Q");
		
		System.out.println("El json es: " + jsonUsuario);
        String s = apiCaller.filterUserInfo(jsonUsuario).toString();
        System.out.println("Devuelve "  + s);
          /*
          
                
                s=s.replace("[", "");
                s=s.replace("]", "");
                JSONArray jarr = new JSONArray();

                JSONObject jsonAyudas = new JSONObject();
                jsonAyudas.put("id",ayuda1.getId());
                jsonAyudas.put("nombre",ayuda1.getNombre());
                jsonAyudas.put("descripcion",ayuda1.getDescripcion());
                jsonAyudas.put("zona",ayuda1.getZona());
                JSONObject jsonAyudas2 = new JSONObject();
                jsonAyudas2.put("id",ayuda2.getId());
                jsonAyudas2.put("nombre",ayuda2.getNombre());
                jsonAyudas2.put("descripcion",ayuda2.getDescripcion());
                jsonAyudas2.put("zona",ayuda2.getZona());
                JSONObject jsonAyudas3 = new JSONObject();
                jsonAyudas2.put("id",ayuda3.getId());
                jsonAyudas2.put("nombre",ayuda3.getNombre());
                jsonAyudas2.put("descripcion",ayuda3.getDescripcion());
                jsonAyudas2.put("zona",ayuda3.getZona());
                jarr.put(jsonAyudas);
                jarr.put(jsonAyudas2);
                jarr.put(jsonAyudas3);




                


         * 
         
         */
         /*
         Ayuda ayuda1 = new Ayuda("1","nombre1","descp1","zona1");
                
                Ayuda ayuda2 = new Ayuda("2","nombre2","descp2","zona2");
                
                Ayuda ayuda3 = new Ayuda("3","nombre3","descp3","zona3");
                
                Ayuda [] arr = {ayuda1,ayuda2,ayuda3};
                ObjectMapper mapper = new ObjectMapper();
                String s = mapper.writeValueAsString(arr);
                System.out.println("La cadena es: ");
                System.out.println(s);
                System.out.println("La funcion devuele ");
                System.out.println(res.toString());
         
         
         
         */  
                
                
    }
    public static int contarCaracterEnString(char caracter, String cadena) {
        int contador = 0;
        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) == caracter) {
                contador++;
            }
        }
        return contador;
    }
   
    public static String [][] filterAyudas(String infoRecibida) throws IOException {
        JSONObject jadd ;
        int filas = contarCaracterEnString('{', infoRecibida);
        String sadd;
        String[][] response = new String[filas][4];
        int indexApertura = infoRecibida.indexOf("{");
        int indexCierre = infoRecibida.indexOf("}");
        for (int i = 0; i < filas; i++) {
            sadd = infoRecibida.substring(indexApertura, indexCierre+1);
            indexApertura = infoRecibida.indexOf("{", indexApertura+1);
            indexCierre = infoRecibida.indexOf("}", indexCierre+1);
            System.out.println(sadd);
            jadd = new JSONObject(sadd);

            response[i][0] = jadd.get("id").toString();
            response[i][1] = jadd.get("nombre").toString();
            response[i][2] = jadd.get("descripcion").toString();
            response[i][3] = jadd.get("zona").toString();
        }
        return response;
    }



}

