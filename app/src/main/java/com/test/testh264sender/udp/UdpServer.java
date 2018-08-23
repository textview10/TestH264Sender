package com.test.testh264sender.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by xu.wang
 * Date on  2018/8/23 13:24:28.
 *
 * @Desc
 */

public class UdpServer extends Thread {
    private int udpPort = 11111;
    private DatagramSocket datagramSocket;
    private boolean isRunning = true;
    private final int cacheLength = 1024 * 8;
    private OnUdpMsgListener mListener;

    public UdpServer() {
        isRunning = true;
        try {
            datagramSocket = new DatagramSocket(null);
            datagramSocket.setReuseAddress(true);
            datagramSocket.bind(new InetSocketAddress(udpPort));
        } catch (Exception e) {

        }
    }

    @Override
    public void run() {
        super.run();
        byte[] data = new byte[cacheLength];
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
        while (isRunning) {
            try {
                datagramSocket.receive(datagramPacket);
                byte[] m = Arrays.copyOf(datagramPacket.getData(), datagramPacket.getLength());
                receiveMsg(datagramPacket.getAddress().getHostAddress());
            } catch (Exception e) {
                continue;
            }
        }
    }

    private void receiveMsg(String hostAddress) {
        if (mListener != null) mListener.receiveMsg(hostAddress);
    }

    public void shutDown() {
        isRunning = false;
        if (datagramSocket != null) {
            try {
                datagramSocket.close();
            } catch (Exception e) {

            }
        }
    }

    public void setOnUdpMsgListener(OnUdpMsgListener listener) {
        this.mListener = listener;
    }

    public interface OnUdpMsgListener {
        void receiveMsg(String msg);
    }
}
