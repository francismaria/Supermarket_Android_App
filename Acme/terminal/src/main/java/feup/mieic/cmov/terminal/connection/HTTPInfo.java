package feup.mieic.cmov.terminal.connection;

/**
 * This interface contains constants related to the connection to the server.
 *
 * To test the app in Android cellphone, it must be connected to the same network
 * as the server.
 *
 *
 */
public interface HTTPInfo {

    // Emulator ( http://10.0.2.2:8080/ )

    String CHECKOUT_PATH = "http://192.168.1.74:8080/AcmeServer/api/new-order";

    // HTTP response codes

    int SUCCESS_CODE = 200;

    int BAD_REQUEST = 401;

    int UNAUTHORIZED_CODE = 401;

    int INTERNAL_ERROR_CODE = 500;
}
