package skrm.controller.request;

public class Solicitud extends Ayuda{
    private String solicitudId;
    private String nombreSolicitante;
    private String apellidosSolicitante;
    private String estado;
    private String ubicacionInstalacion;
    private String coordenadas;
    private String dimension;
    private String subvencionSol;
    private String fondos;
    private String supervisorId;
    private String tramitadorId;
    private String usuarioId;
    private String urlImagen;
    private String mensaje;
    private boolean visto;

    public Solicitud() {

    }

    public Solicitud(String solicitudId, String nombreSolicitante, String apellidosSolicitante, String estado,
                     String ubicacionInstalacion, String coordenadas, String dimension, String  subvencionSol, String fondos,
                     String supervisorId, String tramitadorId, String usuarioId, String urlImagen, String mensaje, boolean visto) {
        this.solicitudId = solicitudId;
        this.nombreSolicitante = nombreSolicitante;
        this.apellidosSolicitante= apellidosSolicitante;
        this.estado = estado;
        this. ubicacionInstalacion = ubicacionInstalacion;
        this.coordenadas = coordenadas;
        this.dimension = dimension;
        this.subvencionSol = subvencionSol;
        this.fondos = fondos;
        this.supervisorId = supervisorId;
        this.tramitadorId = tramitadorId;
        this.usuarioId = usuarioId;
        this.urlImagen = urlImagen;
        this.mensaje = mensaje;
        this.visto = visto;

    }

    public String getSolicitudId() { return solicitudId; }
    public String getNombreSolicitante() { return nombreSolicitante; }
    public String getApellidosSolicitante() { return apellidosSolicitante; }
    public String getEstado() { return  estado; }
    public String getUbicacionInstalacion() { return ubicacionInstalacion; }
    public String getCoordenadas() { return coordenadas; }
    public String getDimension() { return dimension; }
    public String getSubvencionSol() { return subvencionSol; }
    public String getFondos() { return fondos; }
    public String getSupervisorId() { return supervisorId; }
    public String getTramitadorId() { return tramitadorId; }
    public String getUsuarioId() { return usuarioId; }
    public String getUrlImagen() { return urlImagen; }
    public String getMensaje() { return mensaje; }
    public boolean getVisto() { return visto; }


    public void setSolicitudId(String solicitudId) { this.solicitudId = solicitudId; }
    public void setNombreSolicitante (String nombreSolicitante) { this.nombreSolicitante = nombreSolicitante; }
    public void setApellidosSolicitante (String apellidosSolicitante) { this.apellidosSolicitante = apellidosSolicitante; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setUbicacionInstalacion(String ubicacionInstalacion) { this.ubicacionInstalacion = ubicacionInstalacion; }
    public void setCoordenadas(String coordenadas) { this.coordenadas = coordenadas; }
    public void setDimension(String dimension) { this.dimension = dimension; }
    public void setSubvencionSol(String subvencionSol) { this.subvencionSol = subvencionSol; }
    public void setFondos(String fondos) { this.fondos = fondos; }
    public void setSupervisorId(String supervisorId) { this.supervisorId = supervisorId; }
    public void setTramitadorId(String tramitadorId) { this.tramitadorId = tramitadorId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public void setVisto(boolean visto) { this.visto = visto; }


    @Override
    public String toString(){
        return "{" + this.solicitudId + "," + this.nombreSolicitante + ',' + this.apellidosSolicitante + ',' +
                this.estado + "," + this.ubicacionInstalacion + "," + this.coordenadas + "," +
                this.dimension + ',' + this.subvencionSol + "," + this.fondos + ',' + this.supervisorId + + ',' +
                this.tramitadorId + ',' + this.usuarioId + ',' + this.urlImagen + ',' + this.mensaje + ',' + this.visto + "}";
    }

}

