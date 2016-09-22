package com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * ����SQLite����ɾ�Ĳ�
 * 
 * @author pengTan
 * 
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	
	private Context mcontext;

	/**
	 * City�������
	 */
	public static final String CREATE_CITY = "create table City ("
			+ "id integer primary key autoincrement," 
			+ "city_name text,"
			+ "province_id int)";
	/**
	 * Province�������
	 */
	public static final String CREATE_PROVINCE = "create table Province (" 
			+ "id integer," 
			+ "province_name text)";

	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		mcontext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CITY);  //������
		db.execSQL(CREATE_PROVINCE);
		Toast.makeText(mcontext, "������ɹ���", Toast.LENGTH_LONG).show();
		Log.d("�����������ݿ⹤�������ʾ��Ϣ", "������ɹ�");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

}
