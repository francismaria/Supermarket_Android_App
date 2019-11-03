package feup.mieic.cmov.acme.connection;

/**
 * This interface contains constants related to the connection to the server.
 */
public interface HTTPInfo {

    String LOGIN_PATH = "http://10.0.2.2:8080/AcmeServer/api/login";
    String REGISTER_PATH = "http://10.0.2.2:8080/AcmeServer/api/register";

    // HTTP response codes
    int SUCCESS_CODE = 200;
    int UNAUTHORIZED_CODE = 401;
}
