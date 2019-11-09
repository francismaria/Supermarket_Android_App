package feup.mieic.cmov.acme.connection;

/**
 * This interface contains constants related to the connection to the server.
 */
public interface HTTPInfo {

    String LOGIN_PATH = "http://10.0.2.2:8080/AcmeServer/api/login";

    String REGISTER_PATH = "http://10.0.2.2:8080/AcmeServer/api/register";

    String PROFILE_PATH = "http://10.0.2.2:8080/AcmeServer/api/profile";

    String CONTACT_PATH = "http://10.0.2.2:8080/AcmeServer/api/contact";

    String HISTORY_PATH = "http://10.0.2.2:8080/AcmeServer/api/history";

    String GET_ORDER_INFO_PATH = "http://10.0.2.2:8080/AcmeServer/api/order";

    // HTTP response codes

    int SUCCESS_CODE = 200;

    int BAD_REQUEST = 401;

    int UNAUTHORIZED_CODE = 401;

    int INTERNAL_ERROR_CODE = 500;
}
