package skrm.controller.request;



public class Error {
    private String codigoError;

    private String mensaje;

    public Error() {

    }

    public Error (String codigoError, String mensaje) {
        this.codigoError = codigoError;
        this.mensaje = mensaje;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String toString(){
        return "{" + this.codigoError+ ","+ this.mensaje + "}";
    }
}