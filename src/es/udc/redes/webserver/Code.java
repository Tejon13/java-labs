package es.udc.redes.webserver;

public enum Code {

    OK("200 OK"),
    NOT_MODIFIED("304 Not Modified"),
    BAD_REQUEST("400 Bad Request"),
    FORBIDDEN("403 Forbidden"),
    NOT_FOUND("404 Not Found"),
    NOT_IMPLEMENTED("501 Not Implemented");

    private final String estado;

    private Code(String code) {
        this.estado = code;
    }

    public String getEstado() {
        return estado;
    }

}