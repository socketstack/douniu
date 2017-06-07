package com.rxqp.utils;

import com.rxqp.common.constants.CommonConstants;
import com.rxqp.common.constants.WeixinConstants;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
//	public static String getIpAddr(HttpServletRequest request) {
//		String ip = request.getHeader("x-forwarded-for");
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("WL-Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getRemoteAddr();
//		}
//		return ip;
//	}

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

	public static JSONObject sendGet(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		if (StringUtils.isBlank(result)){
			return null;
		}
		JSONObject jsonObject;
		try {
			jsonObject = JSONObject.fromObject(result);
		} catch (Exception e) {
			String jStr = XmlConverUtil.xmltoJson(result);
			jsonObject = JSONObject.fromObject(jStr);
		}
		return jsonObject;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static JSONObject sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		if (StringUtils.isBlank(result)){
			return null;
		}
		JSONObject jsonObject;
		try {
			jsonObject = JSONObject.fromObject(result);
		} catch (Exception e) {
			String jStr = XmlConverUtil.xmltoJson(result);
			jsonObject = JSONObject.fromObject(jStr);
		}
		return jsonObject;
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
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("return_code", "SUCCESS");
//		map.put("return_msg", "OK");
//		String xml = mapToXml(map);
//		System.out.println(xml);
//		String url = "http://localhost:8081/addUser.do?openid=OPENID2&name=mff&imgUrl=http://www.xxxx.com/fyj/rtjk/xxx.gif";
		String url = "http://139.129.98.110:8090/game_service/getPlayerByOpenid.do?openid=oEJQm1ZaRtilEGFWqPDwo6HnanV8";
		JSONObject re = sendGet(url);
		System.out.println(re);
	}
}
