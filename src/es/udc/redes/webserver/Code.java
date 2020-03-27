package es.udc.redes.webserver;

/**
 * Enumeration to identify each HTTP Code.
 *
 * @author Íker
 */
public enum Code {

    /**
     * HTTP Code: 200 OK
     */
    OK("200 OK"),
    /**
     * HTTP Code: 304 Not Modified
     */
    NOT_MODIFIED("304 Not Modified"),
    /**
     * HTTP Code: 400 Bad Request
     */
    BAD_REQUEST("400 Bad Request"),
    /**
     * HTTP Code: 403 Forbidden
     */
    FORBIDDEN("403 Forbidden"),
    /**
     * HTTP Code: 404 Not Found
     */
    NOT_FOUND("404 Not Found"),
    /**
     * HTTP Code: 501 Not Implemented
     */
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