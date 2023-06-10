package skrm.controller.request;

public class Ayuda {
    private String ayudaId;
    private String nombre;
    private boolean esAutonomica;
    private String tipo;
    private String municipioNombre;
    private String zonaNombre;
    private String presupuestoTotal;
    private String presupuestoConcedido;
    private String presupuestoEmitido;
    private String duracion;
    private String docsSol;
    private String docsObra;
    private String numeroSolicitudes;
    private String descripcion;
    private String supervisorId;
    private String tramitadorId;

    public Ayuda(Ayuda param){
        this.ayudaId = param.getAyudaId();
        this.nombre = param.getNombre();
        this.esAutonomica = param.getEsAutonomica();
        this.tipo = param.getTipo();
        this.municipioNombre = param.getMunicipioNombre();
        this.zonaNombre = param.getZonaNombre();
        this.presupuestoTotal = param.getPresupuestoTotal();
        this.presupuestoConcedido = param.getPresupuestoConcedido();
        this.presupuestoEmitido = param.getPresupuestoEmitido();
        this.duracion = param.getDuracion();
        this.docsSol = param.getDocsSol();
        this.docsObra = param.getDocsObra();
        this.numeroSolicitudes = param.getNumeroSolicitudes();
        this.descripcion = param.getDescripcion();
        this.supervisorId = param.getSupervisorId();
        this.tramitadorId = param.getTramitadorId();

    }
    public Ayuda(){

    }
    public Ayuda(String ayudaId,String nombre, boolean esAutonomica, String tipo, String municipioNombre, String zonaNombre,
                 String presupuestoTotal, String presupuestoConcedido, String presupuestoEmitido, String duracion,
                 String docsSol, String docsObra, String numeroSolicitudes, String descripcion, String supervisorId, String tramitadorId){
        this.ayudaId = ayudaId;
        this.nombre = nombre;
        this.esAutonomica = esAutonomica;
        this.tipo= tipo;
        this.municipioNombre = municipioNombre;
        this.zonaNombre = zonaNombre;
        this.presupuestoTotal = presupuestoTotal;
        this.presupuestoConcedido = presupuestoConcedido;
        this.presupuestoEmitido = presupuestoEmitido;
        this.duracion = duracion;
        this.docsSol = docsSol;
        this.docsObra = docsObra;
        this.numeroSolicitudes = numeroSolicitudes;
        this.descripcion = descripcion;
        this.supervisorId = supervisorId;
        this.tramitadorId = tramitadorId;

    }
    public String getAyudaId(){
        return ayudaId;
    }
    public String getNombre(){
        return nombre;
    }
    public boolean getEsAutonomica() { return esAutonomica; }
    public String getTipo(){ return tipo; }
    public String getMunicipioNombre() { return municipioNombre; }
    public String getZonaNombre() { return zonaNombre; }
    public String getDescripcion(){ return descripcion; }
    public String getDocsSol(){ return docsSol; }
    public String getDocsObra(){ return docsObra; }
    public String getDuracion() { return duracion;}
    public String getPresupuestoTotal(){ return presupuestoTotal; }
    public String getPresupuestoConcedido(){ return presupuestoConcedido; }
    public String getPresupuestoEmitido(){ return presupuestoEmitido; }
    public String getNumeroSolicitudes() { return numeroSolicitudes; }
    public String getSupervisorId() { return supervisorId; }
    public String getTramitadorId() { return tramitadorId; }


    public void setAyudaId(String ayudaId){ this.ayudaId = ayudaId; }
    public void setNombre(String nombre){ this.nombre = nombre; }
    public void setEsAutonomica(boolean esAutonomica) { this.esAutonomica = esAutonomica; }
    public void setDescripcion(String descripcion){ this.descripcion = descripcion; }
    public void setZonaNombre (String zonaNombre) { this.zonaNombre = zonaNombre; }
    public void setMunicipioNombre(String municipioNombre) { this.municipioNombre = municipioNombre; }
    public void setPresupuestoTotal(String presupuestoTotal){ this.presupuestoTotal = presupuestoTotal; }
    public void setPresupuestoConcedido(String presupuestoConcedido){ this.presupuestoConcedido = presupuestoConcedido; }
    public void setPresupuestoEmitido(String presupuestoEmitido){ this.presupuestoEmitido = presupuestoEmitido; }
    public void setDocsSol(String docsSol){ this.docsSol = docsSol; }
    public void setDocsObra(String docsObra){ this.docsObra = docsObra; }
    public void setTipo(String tipo){ this.tipo = tipo; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    public void setNumeroSolicitudes(String numeroSolicitudes) { this.numeroSolicitudes = numeroSolicitudes; }
    public void setSupervisorId(String supervisorId) { this.supervisorId = supervisorId; }
    public void setTramitadorId(String tramitadorId) { this.tramitadorId = tramitadorId; }

    @Override
    public String toString(){
        return "{" + this.ayudaId + "," + this.nombre + "," + this.esAutonomica + ',' + this.tipo + ',' +
                this.municipioNombre + ',' + this.zonaNombre + ',' + this.presupuestoTotal + "," + this.presupuestoConcedido +
                "," + this.presupuestoEmitido + ","  + this.duracion + ',' + this.docsSol + "," + this.docsObra + ',' +
                this.numeroSolicitudes + ',' + this.descripcion + "," + this.supervisorId + "," + this.tramitadorId + "}";
    }

}

