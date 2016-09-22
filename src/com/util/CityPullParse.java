package com.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.db.CoolWeatherDB;
import com.pojo.City;
import com.pojo.Province;

import android.R.bool;
import android.content.res.XmlResourceParser;
import android.util.Log;

/**
 * ����XML����ȡ����
 * 
 */
public class CityPullParse {

	public static boolean Parse(String CityString, CoolWeatherDB coolWeatherDB) {
		// ArrayList<City> CityArray = new ArrayList<City>();
		boolean x = false;
		try {
			// ���幤�� XmlPullParserFactory
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

			// ��������� XmlPullParser
			XmlPullParser parser = factory.newPullParser();

			// ��ȡxml��������
			parser.setInput(new StringReader(CityString));

			x = ParseXml(parser, coolWeatherDB);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return x;
	}

	public static boolean Parse(InputStream cityIS, CoolWeatherDB coolWeatherDB) {
		// ArrayList<City> cityArray = new ArrayList<City>();
		boolean x = false;
		try {
			// ���幤�� XmlPullParserFactory
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

			// ��������� XmlPullParser(Ҫת��ΪXmlResourceParser,��������������)
			XmlResourceParser parser = (XmlResourceParser) factory
					.newPullParser();

			// ��ȡxml��������
			parser.setInput(cityIS, "utf-8");

			x = ParseXml(parser, coolWeatherDB);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		return x;
	}

	/**
	 * @param parser
	 * @return
	 */
	public static boolean ParseXml(XmlPullParser parser,
			CoolWeatherDB coolWeatherDB) {
		// ArrayList<City> cityArray = new ArrayList<City>();
		City CityTemp = null;
		Province province = null;
		String cityName;
		String provinceName = "";
		int provinceId = 1;
		boolean x = false;
		boolean y = false;
		try {
			// ��ʼ�����¼�
			int eventType = parser.getEventType();

			// �����¼����������ĵ�������һֱ����
			while (eventType != XmlPullParser.END_DOCUMENT) {
				// ��Ϊ������һ�Ѿ�̬�������������������switch
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;

				case XmlPullParser.START_TAG:
					// ����ǰ��ǩ�������
					String tagName = parser.getName();
					if (tagName.equals("province")) {
						// provinceId =
						// Integer.parseInt(parser.getAttributeValue(null,
						// "id"));// ��ȡprovince�ڵ�����Ϊid��ֵ
						// Integer.parseInt(parser.getAttributeValue(0));//�ڶ��ַ�ʽ����ȡprovince�ڵ�����Ϊid��ֵ
						provinceName = String.valueOf(parser.getAttributeValue(
								null, "name"));// ��ȡprovince�ڵ�����Ϊname��ֵ
						Log.d("������xml��ȡ������ʡ�ݵ�����", provinceName);
						Log.d("������ʡ�ݵ�id", provinceId+"");
						province = new Province();
						province.setId(provinceId);
						province.setProvinceName(provinceName);
						provinceId++;
						coolWeatherDB.saveProvince(province);
						x = true;
					} else if (tagName.equals("item")) {
						CityTemp = new City();
						cityName = parser.nextText(); // ��ȡ�ĳ�����
						CityTemp.setCityName(cityName);
						CityTemp.setProvinceId(provinceId-1);
						coolWeatherDB.saveCity(CityTemp);
						// cityArray.add(CityTemp);
						y = true;
					}
					break;

				case XmlPullParser.END_TAG:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				}

				// ��������next����������һ���¼������˵Ľ���ͳ���ѭ��#_#
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (x && y) {
			return true;
		}
		return false;
	}
}