package com.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.db.CoolWeatherDB;
import com.db.CoolWeatherOpenHelper;
import com.pojo.City;
import com.pojo.Province;
import com.util.CityPullParse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseAreaActivity extends Activity {

	public static final int LEAVEL_PROVINCE = 0;
	public static final int LEAVEL_CITY = 1;

	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();

	// ʡ�б�
	private List<Province> provinceList;
	// �����б�
	private List<City> cityList;
	// ѡ�е�ʡ��
	private Province selectedProvince;
	// ѡ�еĳ���
	private City selectedCity;
	// ��ǰѡ�еļ���
	private int currentLevel;

	private boolean flag;
	private TextView textview;
	private String fileName = "city.xml";
	private XmlResourceParser provinceandcityParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				if (currentLevel == LEAVEL_PROVINCE) {
					selectedProvince = provinceList.get(index);
					queryCities();
				} else if (currentLevel == LEAVEL_CITY) {
					selectedCity = cityList.get(index);
				}
			}
		});
		queryProvince();
	}

	/**
	 * ��ѯȫ�����е�ʡ�ݣ����ȴ����ݿ�飬û����ȥXML�ļ������ȡ
	 */
	private void queryProvince() {
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			// ��ʱ��������Ҫ�޸��Ѿ����ɵ��б���ӻ����޸����ݣ�
			// notifyDataSetChanged()�������޸��������󶨵�����󣬲�������ˢ��Activity��֪ͨActivity����ListView
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEAVEL_PROVINCE;
		} else {
			queryFromXml();
		}
	}

	private void queryFromXml() {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(this,
				"CoolWeather8.db", null, 2);
		dbHelper.getWritableDatabase();
		getcity();
	}

	/**
	 * ��ѯѡ��ʡ�������е��С��϶��Ѿ��������ݿ������ˣ����Բ������½���xml�ļ�
	 */
	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEAVEL_CITY;
		}
	}

	private void getcity() {
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		provinceandcityParser = getXMLFromResXml(fileName);
		flag = CityPullParse.ParseXml(provinceandcityParser, coolWeatherDB);
		// cityArray = CityPullParse.Parse(getInputStreamFromAssets(fileName));
		/*
		 * for (City city : cityArray) { String provinceName = ""; int
		 * provinceid = city.getProvinceId(); cityStr += "ʡ��ID[" +
		 * city.getProvinceId() + "],ʡ��name[" + city.getProvinceName() +
		 * "],����ID[" + city.getId() + "], " + city.getCityName() + "\n"; }
		 */
		textview.setText(flag + "");
		Log.d("������getcity", "��ȡ��Ϣ�ɹ���");
	}

	public XmlResourceParser getXMLFromResXml(String fileName) {
		XmlResourceParser xmlParser = null;
		try {
			xmlParser = this.getResources().getXml(R.xml.city);
			return xmlParser;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlParser;
	}

	/**
	 * ��assets�ж�ȡ�ļ���InputStream��
	 */
	public InputStream getInputStreamFromAssets(String fileName) {
		try {
			InputStream inputStream = getResources().getAssets().open(fileName);
			return inputStream;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("����ƴ�����ļ���..");
			progressDialog.setCanceledOnTouchOutside(false);

		}
		progressDialog.show();
	}

	/**
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/**
	 * ��ȡback�������ݵ�ǰ�������жϣ���ʱӦ�÷��ص����б�ʡ�б���ֱ���˳�
	 */
	@Override
	public void onBackPressed() {
		if (currentLevel == LEAVEL_CITY) {
			queryProvince();
		} else {
			finish();
		}
	}

}
