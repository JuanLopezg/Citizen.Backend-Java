package skrm.controller.request;
public class ciudadano {
    private String dni;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String municipio;
    private String zona;
    public ciudadano(){
        
    }
    public ciudadano(String dni,String nombre,String apellidos,String direccion, String municipio, String zona){
        this.dni = dni;
        this.nombre = toUpper(nombre);
        this.apellidos = toUpperA(apellidos);//Ponemos el apellido en mayúsculas para enviarlo al front
        this.direccion = direccion;
        this.municipio = municipio;
        this.zona = zona;

    }
    public String getDni(){
        return dni;
    }
    public String getNombre(){
        return nombre;
    }
    public String getApellidos(){
        return apellidos;
    }
    public String getDireccion(){
        return direccion;
    }
    public String getMunicipio() { return municipio; }
    public String getZona() { return zona; }
    public void setDni(String dni){
        this.dni = dni;
    }
    public void setNombre(String nombre){
        this.nombre = toUpper(nombre);
    }
    public void setDireccion(String direccion){
        this.direccion = direccion;
    }
    public void setApellidos(String apellidos){
        
        this.apellidos = toUpperA(apellidos);//Ponemos el apellido en mayúsculas para enviarlo al front
    }

    public void setMunicipio(String municipio) { this.municipio = municipio; }
    public void setZona(String zona) { this.zona = zona; }

    @Override
    public String toString(){
        return "[" + this.dni+ ","+ this.nombre + ","+this.apellidos + "," + this.direccion + "," + this.municipio + "," + this.zona+"]";
    }
    private String toUpper(String str){
        if(str.length() > 0){
            char c = str.charAt(0);
            if( (c >= 'a') && (c <= 'z') ){
                c = (char) ('A' + ( c - 'a'));
            }
            str = c + str.substring(1, str.length());
        }
        
        return str;

    }
    private String toUpperA(String str){
        if(str.length() > 0){
            str=toUpper(str);
            int i=str.indexOf(" ");
            while(i>0 && i<str.length()-1){
                str=str.substring(0, i+1) + toUpper(str.substring(i+1, str.length()));
                i=str.indexOf(" ",i+1);
            }
        }
        
        return str;

    }



    
}
