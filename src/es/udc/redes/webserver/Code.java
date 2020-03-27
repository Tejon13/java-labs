package es.udc.redes.webserver;

/**
 * Enumeration to identify each HTTP Code.
 *
 * @author Íker
 */
public enum Code {

    OK("200 OK"),
    NOT_MODIFIED("304 Not Modified"),
    BAD_REQUEST("400 Bad Request"),
    FORBIDDEN("403 Forbidden"),
    NOT_FOUND("404 Not Found"),
    NOT_IMPLEMENTED("501 Not Implemented");

    private final String estado;

    /**
     * Enumeration's constructor.
     *
     * @param code - HTTP Code's meaning
     */
    private Code(String code) {
        this.estado = code;
    }

    /**
     * HTTP Code's getter.
     *
     * @return estado - HTTP Code's meaning
     */
    public String getEstado() {
        return estado;
    }

}