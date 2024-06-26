package net.lanet.vollmed.infra.utilities;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Objects;

import static java.lang.Integer.parseInt;

@Component
public class TestIpUtil {

    public static Object[] testIp(String ipAddressOrAndPort) {
        String ipAddress = ipAddressOrAndPort;
        String ipPort = "";
        try {
            ipPort = ipAddressOrAndPort.substring(ipAddressOrAndPort.indexOf(":"));
            ipAddress = ipAddressOrAndPort.replace(ipPort, "");
            ipPort = ipPort.replace(":", "");
        } catch (Exception ignored) {
        }

        Object[] result = new Object[]{false, ""};
        if (!Objects.equals(ipPort, "")) {
            boolean isReachable = isReachable(ipAddress, parseInt(ipPort));
            result[0] = isReachable;
            result[1] = "O endereço IP " + ipAddress + " na porta " + ipPort + " foi encontrado? " + isReachable;
        } else {
            boolean isReachable = isReachable(ipAddress);
            result[0] = isReachable;
            result[1] = "O endereço IP " + ipAddress + " foi encontrado? " + isReachable;
        }

        return result;
    }

    public static boolean isReachable(String ipAddress, Integer... ipPort) {
        int port = ipPort.length > 0 ? ipPort[0] : 80;
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            InetSocketAddress socketAddress = new InetSocketAddress(address, port);
            return socketAddress.isUnresolved() ||
                    socketAddress.getAddress().isReachable(10000); // Define um timeout de 10 segundos
        } catch (IOException ignored) {
            return false;
        }
    }

}
