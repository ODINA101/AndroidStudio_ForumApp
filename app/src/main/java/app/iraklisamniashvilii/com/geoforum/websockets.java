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

        this.mSocket = IO.socket("http://forum-api.herokuapp.com/");
        this.mSocket.connect();

    }




}
