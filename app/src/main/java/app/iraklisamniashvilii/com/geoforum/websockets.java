package app.iraklisamniashvilii.com.geoforum;


import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by Teacher on 02.03.2018.
 */

public class websockets {
    Socket mSocket = null;

    public websockets() {
        try {
            mSocket = IO.socket("http://192.168.1.107:3000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();
    }




}
