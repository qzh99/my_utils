package me.qin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpUtils {
    private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);
    private final static String UNKNOWN = "unknown";

	/**
	 * 获取IP地址
	 * 
	 * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
    	String ip = null;
        try {
            ip = request.getHeader("X-Forwarded-For");
            if (isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            logger.error("IPUtils ERROR ", e);
        }

        logger.info("获取客户端 ip：{} ", ip);
        // 使用代理，则获取第一个IP地址
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                //ip = ip.substring(0, ip.indexOf(","));
                String[] ipAddresses = ip.split(",");
                for (String ipAddress : ipAddresses) {
                    ipAddress = ipAddress.trim();
                    if (isValidIP(ipAddress)) {
                        return ipAddress;
                    }
                }
            }
        }
        
        return ip;
    }


    /**
     * 判断是否是IP格式
     * @param ipAddress ip地址
     * @return boolean
     */
    public static boolean isValidIpAddress(String ipAddress) {
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Pattern pattern = Pattern.compile(ipPattern);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }
    // 验证是否为有效的 IP 地址（包括 IPv4 和 IPv6）
    public static boolean isValidIP(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return (address instanceof java.net.Inet4Address || address instanceof java.net.Inet6Address);
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 获取服务器上的ip
     * @return serverIp
     */
    public static String getServerIp(){
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            //System.out.println("IP地址: " + ipAddress);
            return ipAddress;
        } catch (UnknownHostException e) {
            logger.error("获取ip地址失败", e);
        }
        return "";
    }

    public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}
}
