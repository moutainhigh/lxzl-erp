package com.lxzl.erp.core.service.getIpAndMac.impl;

import com.lxzl.erp.common.domain.getIpAndMac.UdpGetClientMacAddr;
import com.lxzl.erp.common.domain.getIpAndMac.pojo.IpAndMac;
import com.lxzl.erp.core.service.getIpAndMac.IpAndMacService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/19
 * @Time : Created in 19:11
 */
@Service
public class IpAndMacServiceImpl extends HttpServlet implements IpAndMacService {
    /**
    * 获取ip地址和mac地址
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/19 19:11
    * @param : request
    * @Return : com.lxzl.erp.common.domain.getIpAndMac.pojo.IpAndMac
    */
    @Override
    public IpAndMac getIpAndMacService(HttpServletRequest request) throws Exception {
        IpAndMac ipAndMac = new IpAndMac();

        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }

//        List<String> realIps = UdpGetClientMacAddr.getRealIps();


//        String macAddress = getMACAddress(ip);
//        IpAndMacServiceImpl ipAndMacService = new IpAndMacServiceImpl();
//        UdpGetClientMacAddr udpGetClientMacAddr = ipAndMacService.new UdpGetClientMacAddr("113.91.210.0");
//        ipAndMac.setMac(udpGetClientMacAddr.GetRemoteMacAddr());
        return ipAndMac;
    }

//    public IpAndMac getIpAndMacService1(HttpServletRequest request) {
//        IpAndMac ipAndMac = new IpAndMac();
//        String ip = request.getHeader("X-Forwarded-For");
//        if (ip != null) {
//            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
//                int index = ip.indexOf(",");
//                if (index != -1) {
//                    ipAndMac.setIp(ip.substring(0, index));
//                    return ipAndMac;
//                } else {
//                    ipAndMac.setIp(ip);
//                    return ipAndMac;
//                }
//            }
//        }
//        ip = request.getHeader("X-Real-IP");
//        if (ip != null) {
//            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
//                ipAndMac.setIp(ip);
//                return ipAndMac;
//            }
//        }
//        return ipAndMac;
//    }
//
//
//    /**
//     * 通过IP地址获取MAC地址
//     * @param ip String,127.0.0.1格式
//     * @return mac String
//     * @throws Exception
//     */
//    public String getMACAddress(String ip) throws Exception {
//        String line = "";
//        String macAddress = "";
//        final String MAC_ADDRESS_PREFIX = "MAC Address = ";
//        final String LOOPBACK_ADDRESS = "127.0.0.1";
//        //如果为127.0.0.1,则获取本地MAC地址。
//        if (LOOPBACK_ADDRESS.equals(ip)) {
//            InetAddress inetAddress = InetAddress.getLocalHost();
//            //貌似此方法需要JDK1.6。
//            byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
//            //下面代码是把mac地址拼装成String
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < mac.length; i++) {
//                if (i != 0) {
//                    sb.append("-");
//                }
//                //mac[i] & 0xFF 是为了把byte转化为正整数
//                String s = Integer.toHexString(mac[i] & 0xFF);
//                sb.append(s.length() == 1 ? 0 + s : s);
//            }
//            //把字符串所有小写字母改为大写成为正规的mac地址并返回
//            macAddress = sb.toString().trim().toUpperCase();
//            return macAddress;
//        }
//        //获取非本地IP的MAC地址
//        try {
//            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
//            InputStreamReader isr = new InputStreamReader(p.getInputStream());
//            BufferedReader br = new BufferedReader(isr);
//            while ((line = br.readLine()) != null) {
//                if (line != null) {
//                    int index = line.indexOf(MAC_ADDRESS_PREFIX);
//                    if (index != -1) {
//                        macAddress = line.substring(index + MAC_ADDRESS_PREFIX.length()).trim().toUpperCase();
//                    }
//                }
//            }
//            br.close();
//        } catch (IOException e) {
//            e.printStackTrace(System.out);
//        }
//        return macAddress;
//    }

    //--------------------------------------------------


    public class UdpGetClientMacAddr {
        private String sRemoteAddr;
        private int iRemotePort = 137;
        private byte[] buffer = new byte[1024];
        private DatagramSocket ds = null;

        public UdpGetClientMacAddr(String strAddr) throws Exception {
            sRemoteAddr = strAddr;
            ds = new DatagramSocket();
        }

        protected final DatagramPacket send(final byte[] bytes) throws IOException {
            DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(sRemoteAddr), iRemotePort);
            ds.send(dp);
            return dp;
        }

        protected final DatagramPacket receive() throws Exception {
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            ds.receive(dp);
            return dp;
        }

        protected byte[] GetQueryCmd() throws Exception {
            byte[] t_ns = new byte[50];
            t_ns[0] = 0x00;
            t_ns[1] = 0x00;
            t_ns[2] = 0x00;
            t_ns[3] = 0x10;
            t_ns[4] = 0x00;
            t_ns[5] = 0x01;
            t_ns[6] = 0x00;
            t_ns[7] = 0x00;
            t_ns[8] = 0x00;
            t_ns[9] = 0x00;
            t_ns[10] = 0x00;
            t_ns[11] = 0x00;
            t_ns[12] = 0x20;
            t_ns[13] = 0x43;
            t_ns[14] = 0x4B;

            for (int i = 15; i < 45; i++) {
                t_ns[i] = 0x41;
            }

            t_ns[45] = 0x00;
            t_ns[46] = 0x00;
            t_ns[47] = 0x21;
            t_ns[48] = 0x00;
            t_ns[49] = 0x01;
            return t_ns;
        }

        protected final String GetMacAddr(byte[] brevdata) throws Exception {

            int i = brevdata[56] * 18 + 56;
            String sAddr = "";
            StringBuffer sb = new StringBuffer(17);

            for (int j = 1; j < 7; j++) {
                sAddr = Integer.toHexString(0xFF & brevdata[i + j]);
                if (sAddr.length() < 2) {
                    sb.append(0);
                }
                sb.append(sAddr.toUpperCase());
                if (j < 6) sb.append(':');
            }
            return sb.toString();
        }

        public final void close() {
            try {
                ds.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public final String GetRemoteMacAddr() throws Exception {
            byte[] bqcmd = GetQueryCmd();
            send(bqcmd);
            DatagramPacket dp = receive();
            String smac = GetMacAddr(dp.getData());
            close();

            return smac;
        }
    }


}
