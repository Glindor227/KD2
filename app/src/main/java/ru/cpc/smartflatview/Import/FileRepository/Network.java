package ru.cpc.smartflatview.Import.FileRepository;

import android.util.Log;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.cpc.smartflatview.Import.Common.AbstractMessage;
import ru.cpc.smartflatview.Import.ImportActivity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

class Network {
    private static Socket socket;
    private static ObjectEncoderOutputStream out;
    private static ObjectDecoderInputStream in;

    static Boolean start(String ip,int port) {
        try {
            Log.d(ImportActivity.TAG, "Network start 1");
            socket = new Socket();
            Log.d(ImportActivity.TAG, "Network start 1.1");
            InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName(ip), port);
            Log.d(ImportActivity.TAG, "Network start 1.2");
            socket.connect(addr, 2000);
            Log.d(ImportActivity.TAG, "Network start 2");
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            Log.d(ImportActivity.TAG, "Network start 3");
            in = new ObjectDecoderInputStream(socket.getInputStream(),10 * 1024 * 1024);
            Log.d(ImportActivity.TAG, "Network start ok");
        } catch (IOException e) {
            Log.d(ImportActivity.TAG, "Network IOException "+e.getMessage());
            e.printStackTrace();
            return false;

        }
        return true;

    }

    static void stop() {
        try {
            Log.d(ImportActivity.TAG, "Network stop");

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void sendMsg(AbstractMessage msg) {
        try {
            Log.d(ImportActivity.TAG, "Уходит сообщение "+msg.getClass().toString());
            out.writeObject(msg);
            Log.d(ImportActivity.TAG, "Ушло сообщение "+msg.getClass().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static AbstractMessage readObject() throws ClassNotFoundException, IOException {
        Object obj = in.readObject();
        return (AbstractMessage) obj;
    }
}
