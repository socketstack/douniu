package com.rxqp.utils;

import java.io.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.rxqp.common.constants.CommonConstants;
import com.rxqp.common.constants.WeixinConstants;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;

public class CommonUtils {

	public static String getLocalIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonConstants.DEFAULT_IP;
		}
	}

	/**
	 * 计算采用utf-8编码方式时字符串所占字节数
	 *
	 * @param content
	 * @return
	 */
	public static int getByteSize(String content) {
		int size = 0;
		if (null != content) {
			try {
				// 汉字采用utf-8编码时占3个字节
				size = content.getBytes("utf-8").length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return size;
	}

	/**
	 * 将long类型的时间转换成标准格式（yyyy-MM-dd HH:mm:ss）
	 *
	 * @param longTime
	 * @return
	 */
	public static String formatTime(long longTime) {
		DateFormat format = new SimpleDateFormat(WeixinConstants.DateFormate);
		return format.format(new Date(longTime));
	}

	/**
	 * 将微信消息中的CreateTime转换成标准格式的时间（yyyy-MM-dd HH:mm:ss）
	 *
	 * @param createTime
	 *            消息创建时间
	 * @return
	 */
	public static String formatTime(String createTime) {
		// 将微信传入的CreateTime转换成long类型，再乘以1000
		long msgCreateTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat(WeixinConstants.DateFormate);
		return format.format(new Date(msgCreateTime));
	}

	/**
	 * emoji表情转换(hex -> utf-16)
	 *
	 * @param hexEmoji
	 * @return
	 */
	public static String emoji(int hexEmoji) {
		return String.valueOf(Character.toChars(hexEmoji));
	}

	/**
	 * 生产随机数字符串
	 *
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String mapToXml(Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");

		for (Map.Entry<String, String> entry : map.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();
			sb.append("<" + name + ">");
			sb.append("<![CDATA[" + value + "]]");
			sb.append("</" + name + ">");
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 *
	 * @date 2013-05-19
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@SuppressWarnings("unchecked")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});

	public static String objToXml(Object obj) {
		xstream.alias("xml", obj.getClass());
		return xstream.toXML(obj).replace("__", "_");
	}

	public static String urlEnodeUTF8(String str) {
		String result = str;
		try {
			result = URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Map<String, String> parseXml(String xml) {
		Map<String, String> map = new HashMap<String, String>();
		Document document = null;
		try {
			// 读取并解析XML文档
			// SAXReader就是一个管道，用一个流的方式，把xml文件读出来
			// SAXReader reader = new SAXReader(); //User.hbm.xml表示你要解析的xml文档
			// Document document = reader.read(new File("User.hbm.xml"));
			// 下面的是通过解析xml字符串的
			document = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElt = document.getRootElement(); // 获取根节点
			List<Element> elements = rootElt.elements();
			for (Iterator<Element> it = elements.iterator(); it.hasNext();) {
				Element element = it.next();
				// System.out.println(element.getName() + " : "
				// + element.getTextTrim());
				map.put(element.getName(), element.getTextTrim());
			}
		} catch (Exception e) {

		}
		return map;
	}

	// 获取ip地址
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取mac地址
	 *
	 * @param ip
	 * @return
	 */
	public static String getMACAddress(String ip) {
		String str = "";
		String macAddress = "";
		try {
			Process p = Runtime.getRuntime().exec(
					"C:\\Windows\\sysnative\\nbtstat.exe -A " + ip);
			InputStreamReader ir = new InputStreamReader(p.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					if (str.indexOf("MAC Address") > 1) {
						macAddress = str.substring(
								str.indexOf("MAC Address") + 14, str.length());
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return macAddress;
	}

	public static void main(String[] args) {
		// System.out.println(getRandomString(32));
		// PrePayReq prePayReq = new PrePayReq();
		// prePayReq.setAppid(Constants.appId);
		// prePayReq.setMch_id(Constants.mch_id);
		// prePayReq.setDevice_info("WEB");
		// prePayReq.setNonce_str(WeixinUtil.getRandomString(32));
		// String outTradeNo = WeixinUtil.getOutTradeNo();
		// prePayReq.setOut_trade_no(outTradeNo);
		// prePayReq.setNotify_url(Constants.notify_url);
		// prePayReq.setTrade_type(Constants.JSAPI);
		//
		// String xml = objToXml(prePayReq);
		// System.out.println(xml);
		// String xml =
		// "<xml><appid><![CDATA[wx95040da38fc72d8e]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><device_info><![CDATA[WEB]]></device_info><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1259469201]]></mch_id><nonce_str><![CDATA[vhsz05j0640gppf3d41zlovtgm31amdf]]></nonce_str><openid><![CDATA[oChsHuEtQM6FRVRJOkQZvdN6V3T4]]></openid><out_trade_no><![CDATA[20160728233216teua7]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[A3FB8D9ECEC2315B2F80F7218F2DB83A]]></sign><time_end><![CDATA[20160728233224]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4009202001201607280005240826]]></transaction_id></xml>";
		// parseXml(xml);
		Map<String, String> map = new HashMap<String, String>();
		map.put("return_code", "SUCCESS");
		map.put("return_msg", "OK");
		String xml = mapToXml(map);
		System.out.println(xml);
	}
}
