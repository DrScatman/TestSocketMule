
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@ScriptMeta(developer = "DrScatmen", desc = "Test", name = "TestSocketMule")
public class Main extends Script {

    private static final String MULE_IP = "10.181.66.95";

    @Override
    public void onStop() {
        try {
            logoutMule(MULE_IP);
        } catch (IOException e) {
            Log.severe(e);
            e.printStackTrace();
        }
    }

    @Override
    public int loop() {
        if (socket == null || !socket.isConnected()) {
            send(MULE_IP, "Trade:");
        } else {

        }
        return 500;
    }

    /**
     * Send method
     *
     * @param message - TRADE = Activate login , DONE - Turn off login
     */
    private static void send(String ip, String message) {
        try {
            sendTradeRequest(ip, message);
        } catch (Exception e) {
            Log.severe(e);
            e.printStackTrace();
        }
    }

    /**
     * Sends message to server from client (Slave)
     *
     * @param message - TRADE = Activate login , DONE - Turn off login
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    private static void sendTradeRequest(String ip, String message) throws IOException, InterruptedException, ClassNotFoundException {
        //get the localhost IP address, if server is running on some other IP, you need to use that
        //InetAddress host = InetAddress.getLocalHost();
        //establish socket connection to server
        if (socket == null || socket.isClosed()) {
            socket = new Socket(ip, 9876);
            socket.setReuseAddress(true);
            socket.setKeepAlive(true);
        }
        out = new DataOutputStream(socket.getOutputStream());
        //write to socket using ObjectOutputStream
        Log.fine("Sending: " + message);
        out.writeChars(message);
        out.flush();
        //read the server response message
        //close resources
        out.close();
        Thread.sleep(500);
    }

    private static Socket socket;
    private static DataOutputStream out;

    public static void logoutMule(String ip) throws IOException {
        send(ip, "Done:");
        if (out != null) {
            out.close();
        }
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
