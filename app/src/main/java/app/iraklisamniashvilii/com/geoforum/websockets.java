package app.iraklisamniashvilii.com.geoforum;


import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by Teacher on 02.03.2018.
 */

public class websockets {
    public Socket mSocket = null;

    public websockets() throws URISyntaxException {

        this.mSocket = IO.socket("http://192.168.1.102:3000/");
        this.mSocket.connect();

    }




}
