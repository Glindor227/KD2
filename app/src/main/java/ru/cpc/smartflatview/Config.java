package ru.cpc.smartflatview;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import io.reactivex.annotations.Nullable;
import ru.cpc.smartflatview.IndicatorPackage.AlarmSensor.FireSensor;
import ru.cpc.smartflatview.IndicatorPackage.AlarmSensor.LeakageSensor;
import ru.cpc.smartflatview.IndicatorPackage.AlarmSensor.MotionSensor;
import ru.cpc.smartflatview.IndicatorPackage.Climats.ClimatConditioner;
import ru.cpc.smartflatview.IndicatorPackage.Climats.ClimatFan;
import ru.cpc.smartflatview.IndicatorPackage.Climats.ClimatRadiator;
import ru.cpc.smartflatview.IndicatorPackage.Climats.ClimatWarmFloor;
import ru.cpc.smartflatview.IndicatorPackage.Doors.Door;
import ru.cpc.smartflatview.IndicatorPackage.Doors.Door2;
import ru.cpc.smartflatview.IndicatorPackage.Doors.DoorLock;
import ru.cpc.smartflatview.IndicatorPackage.Macros.Macro;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroAlarm;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroCamOnOff;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroDezinfection;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroFilterCleaning;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroFilterFail;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroFireSensor;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroFreePass;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroLowLevel;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroMotionSensor;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroPumpAddWater;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroPumpFail;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroPumpFilter;
import ru.cpc.smartflatview.IndicatorPackage.Macros.MacroPumpWorkMode;
import ru.cpc.smartflatview.IndicatorPackage.Regulators.Battery;
import ru.cpc.smartflatview.IndicatorPackage.Regulators.Battery2;
import ru.cpc.smartflatview.IndicatorPackage.Regulators.Conditioner;
import ru.cpc.smartflatview.IndicatorPackage.Regulators.Conditioner2Temp;
import ru.cpc.smartflatview.IndicatorPackage.Regulators.DimmerFan;
import ru.cpc.smartflatview.IndicatorPackage.Regulators.DimmerLamp;
import ru.cpc.smartflatview.IndicatorPackage.Regulators.WarmFloorDevi;
import ru.cpc.smartflatview.IndicatorPackage.Regulators.WarmFloorDevi2;
import ru.cpc.smartflatview.IndicatorPackage.Relays.Curtains;
import ru.cpc.smartflatview.IndicatorPackage.Relays.Fan;
import ru.cpc.smartflatview.IndicatorPackage.Relays.Fountain;
import ru.cpc.smartflatview.IndicatorPackage.Relays.Lamp;
import ru.cpc.smartflatview.IndicatorPackage.Relays.PowerControl;
import ru.cpc.smartflatview.IndicatorPackage.Relays.Radiator;
import ru.cpc.smartflatview.IndicatorPackage.Relays.Valve;
import ru.cpc.smartflatview.IndicatorPackage.Relays.WarmFloor;
import ru.cpc.smartflatview.IndicatorPackage.Sensor.SensorCO;
import ru.cpc.smartflatview.IndicatorPackage.Sensor.SensorHumidity;
import ru.cpc.smartflatview.IndicatorPackage.Sensor.SensorIlluminance;
import ru.cpc.smartflatview.IndicatorPackage.Sensor.SensorTemperature;

public class Config 
{
	public static Config Instance = null;

	public static boolean DEMO = false;

	ArrayList<Subsystem> m_cSubsystems = new ArrayList<Subsystem>();
	public ArrayList<Room> m_cRooms = new ArrayList<Room>();
	LinkedHashMap<String, List<Room>> m_cGroups = new LinkedHashMap<>();
	ArrayList<Room> m_cFavorites = new ArrayList<Room>();

	String m_sIP = "192.168.80.17";
	String m_sSummaryName = "192.168.80.17";
	String m_sSummaryText = "192.168.80.17";
	
	int m_iPort = 10000;
	int m_iPortExt = 10277;
	
	public int m_iPollPeriod = 2000;
	public String m_sMasterCode = "1234";

	public static Boolean portOrientation = true;

	public Config(Boolean port)
	{

		Boolean engl = ! Locale.getDefault().getLanguage().contains("ru");

		Log.d("Glindor3!3","Портертная ориентация1 ? "+  (port? "да" : "НЕТ"));
		portOrientation = port;
		Log.d("Glindor3!3","Портертная ориентация2 ? "+  (portOrientation? "да" : "НЕТ"));
		Log.d("Glindor2222","Начало Конфига "+  Locale.getDefault().getLanguage());
		DEMO = true;
		
		int subsystemIndex = 0;
		int roomIndex = 0;

		Subsystem subsystem0;
		if(port) {
			subsystem0 = new Subsystem("0-0", 0, 2, 3, "0");
			subsystem0.AddIndicator(new Macro(25, 15, engl?"Output":"Я ушёл", engl?"Output":"Я ушёл", false, false, false, true, 1, 2).setDemo());
			subsystem0.AddIndicator(new Macro(75, 15, engl?"Incom":"Я пришёл", engl?"Incom":"Я пришёл", false, false, false, true, 1, 2).setDemo());
			subsystem0.AddIndicator(new MotionSensor(25, 45, engl?"Security system":"Охрана", true, true, false, true, 1, 2).setDemo());
			subsystem0.AddIndicator(new FireSensor(75, 45, engl?"Fire alarm system":"Пож.система", true, true, false, true, 1, 2).setDemo());
			subsystem0.AddIndicator(new LeakageSensor(50, 81, engl?"Water alarm system":"Протечки", true, false, false, true, 1, 2).setDemo());
		}else {
			subsystem0 = new Subsystem("0-0", 1, 3, 2, "0");
			subsystem0.AddIndicator(new Macro(25, 25, engl?"Output":"Я ушёл", engl?"Output":"Я ушёл", false, false, false, true, 1, 2).setDemo());
			subsystem0.AddIndicator(new Macro(50, 25, engl?"Incom":"Я пришёл", engl?"Incom":"Я пришёл", false, false, false, true, 1, 2).setDemo());
			subsystem0.AddIndicator(new MotionSensor(75, 25, engl?"Security system":"Охрана", true, true, false, true, 1, 2).setDemo());
			subsystem0.AddIndicator(new FireSensor(37, 75, engl?"Fire alarm system":"Пож.система", true, true, false, true, 1, 2).setDemo());
			subsystem0.AddIndicator(new LeakageSensor(63, 75, engl?"Water alarm system":"Протечки", true, false, false, true, 1, 2).setDemo());
		}

		m_cSubsystems.add(subsystem0);

		Room room0 = new Room(roomIndex++, engl?"Script":"Макросы", "room0").AddSubsystem(subsystem0);
		m_cRooms.add(room0);

		subsystemIndex = 0;
		Subsystem subsystem1;
		if(port) {
			subsystem1 = new Subsystem("1-0", subsystemIndex++, 3, 4, "0");
			subsystem1.AddIndicator(new Lamp(25, 12, engl?"wall lamp":"Бра", false, false, false, true, 1, 3,1).setDemo());
			subsystem1.AddIndicator(new Lamp(75, 12, engl?"night-light":"Ночник", false, false, false, true, 1, 3,1).setDemo());
			subsystem1.AddIndicator(new DimmerLamp(50, 37, engl?"ceiling lamp":"Люстра", false, false, false, true, 1, 3).setDemo());
			subsystem1.AddIndicator(new WarmFloor(25, 62, engl?"underfloor heating ":"Тепл.пол", false, false, false, true, 1, 3).setDemo());
			subsystem1.AddIndicator(new Conditioner(75, 62, engl?"Air conditioning":"Кондиционер", false, false, false, true, 1, 3).setDemo());
			subsystem1.AddIndicator(new Curtains(25, 87, engl?"curtains":"Шторы", false, false, false, true, 1, 3).setDemo());
			subsystem1.AddIndicator(new FireSensor(75, 87, engl?"Smoke detector":"Пож.датчик", false, true, false, true, 1, 3).setDemo());
		}else {

			subsystem1 = new Subsystem("1-0", 0, 4, 2, "0");
			subsystem1.AddIndicator(new Lamp(25, 25, engl?"wall lamp":"Бра", false, false, false, true, 1, 3,1).setDemo());
			subsystem1.AddIndicator(new Lamp(50, 25, engl?"night-light":"Ночник", false, false, false, true, 1, 3,1).setDemo());
			subsystem1.AddIndicator(new DimmerLamp(75, 25, engl?"ceiling lamp":"Люстра", false, false, false, true, 1, 3).setDemo());
			subsystem1.AddIndicator(new WarmFloor(13, 75, engl?"underfloor heating ":"Тепл.пол", false, false, false, true, 1, 3).setDemo());
			subsystem1.AddIndicator(new Conditioner(37, 75, engl?"Air conditioning":"Кондиционер", false, false, false, true, 1, 3).setDemo());
			subsystem1.AddIndicator(new Curtains(63, 75, engl?"curtains":"Шторы", false, false, false, true, 1, 3).setDemo());
			subsystem1.AddIndicator(new FireSensor(87, 75, engl?"Smoke detector":"Пож.датчик", false, true, false, true, 1, 3).setDemo());
		}

		m_cSubsystems.add(subsystem1);

		Room room1 = new Room(roomIndex++, engl?"Master bedroom":"Спальня хоз.", "room1").AddSubsystem(subsystem1);
		m_cRooms.add(room1);

		//engl?"":
		subsystemIndex = 0;
		Subsystem subsystem1A;
        if(port){
            subsystem1A = new Subsystem("1A-0", subsystemIndex++, 3, 4, "0");
            subsystem1A.AddIndicator(new DimmerLamp(25, 12, engl?"wall lamp":"Бра", false, false, false, true, 1, 3).setDemo());
            subsystem1A.AddIndicator(new Lamp(75, 12, engl?"night-light":"Ночник", false, false, false, true, 1, 3).setDemo());
            subsystem1A.AddIndicator(new Lamp(25, 37, engl?"local lighting":"Подсветка", false, false, false, true, 1, 3).setDemo());
            subsystem1A.AddIndicator(new DimmerLamp(75, 37, engl?"ceiling lamp":"Люстра", false, false, false, true, 1, 3).setDemo());
            subsystem1A.AddIndicator(new WarmFloor(25, 62, engl?"underfloor heating ":"Тепл.пол", false, false, false, true, 1, 3).setDemo());
            subsystem1A.AddIndicator(new Conditioner(75, 62, engl?"AC (Air conditioning)":"Кондиционер", false, false, false, true, 1, 3).setDemo());
            subsystem1A.AddIndicator(new Curtains(50, 87, engl?"curtains":"Шторы", false, false, false, true, 1, 3).setDemo());
        }else{
            subsystem1A = new Subsystem("1A-0", subsystemIndex++, 4, 2, "0");
            subsystem1A.AddIndicator(new DimmerLamp(12, 25, engl?"wall lamp":"Бра", false, false, false, true, 1, 3).setDemo());
            subsystem1A.AddIndicator(new Lamp(37, 25, engl?"night-light":"Ночник", false, false, false, true, 1, 3).setDemo());
            subsystem1A.AddIndicator(new Lamp(62, 25, engl?"local lighting":"Подсветка", false, false, false, true, 1, 3).setDemo());
            subsystem1A.AddIndicator(new DimmerLamp(87, 25, engl?"ceiling lamp":"Люстра", false, false, false, true, 1, 3).setDemo());
            subsystem1A.AddIndicator(new WarmFloor(25, 75, engl?"underfloor heating ":"Тепл.пол", false, false, false, true, 1, 3).setDemo());
            subsystem1A.AddIndicator(new Conditioner(50, 75, engl?"AC (Air conditioning)":"Кондиционер", false, false, false, true, 1, 3).setDemo());
            subsystem1A.AddIndicator(new Curtains(75, 75, engl?"curtains":"Шторы", false, false, false, true, 1, 3).setDemo());
        }


		m_cSubsystems.add(subsystem1A);

		Room room1A = new Room(roomIndex++, engl?"Living room":"Спальня гост.", "room1A").AddSubsystem(subsystem1A);
		m_cRooms.add(room1A);

		subsystemIndex = 0;
		Subsystem subsystem1B1;
		if(port) {
            subsystem1B1 = new Subsystem("1B1-0", subsystemIndex++, 3, 4, "0");
            subsystem1B1.AddIndicator(new DimmerLamp(25, 25, engl?"ceiling lamp":"Люстра", false, false, false, true, 1, 3).setDemo());
            subsystem1B1.AddIndicator(new Lamp(75, 25, engl?"night-light":"Ночник", false, false, false, true, 1, 3).setDemo());
            subsystem1B1.AddIndicator(new WarmFloor(25, 75, engl?"underfloor heating ":"Тепл.пол", false, false, false, true, 1, 3).setDemo());
            subsystem1B1.AddIndicator(new Curtains(75, 75, engl?"curtains":"Шторы", false, false, false, true, 1, 3).setDemo());
        }else{
            subsystem1B1 = new Subsystem("1B1-0", subsystemIndex++, 3, 2, "0");
            subsystem1B1.AddIndicator(new DimmerLamp(33, 25, engl?"ceiling lamp":"Люстра", false, false, false, true, 1, 3).setDemo());
            subsystem1B1.AddIndicator(new Lamp(66, 25, engl?"night-light":"Ночник", false, false, false, true, 1, 3).setDemo());
            subsystem1B1.AddIndicator(new WarmFloor(33, 75, engl?"underfloor heating ":"Тепл.пол", false, false, false, true, 1, 3).setDemo());
            subsystem1B1.AddIndicator(new Curtains(66, 75, engl?"curtains":"Шторы", false, false, false, true, 1, 3).setDemo());
        }

		m_cSubsystems.add(subsystem1B1);

		Room room1B1 = new Room(roomIndex++, engl?"Eldest son's room":"Комната ст. сына", "room1B1").AddSubsystem(subsystem1B1);
		m_cRooms.add(room1B1);

		subsystemIndex = 0;
		Subsystem subsystem1B2;
        if(port){
            subsystem1B2 = new Subsystem("1B2-0", subsystemIndex++, 3, 4, "0");
            subsystem1B2.AddIndicator(new DimmerLamp(25, 25, engl?"ceiling lamp":"Люстра", false, false, false, true, 1, 3).setDemo());
            subsystem1B2.AddIndicator(new Lamp(75, 25, engl?"night-light":"Ночник", false, false, false, true, 1, 3).setDemo());
            subsystem1B2.AddIndicator(new WarmFloor(25, 75, engl?"underfloor heating ":"Тепл.пол", false, false, false, true, 1, 3).setDemo());
            subsystem1B2.AddIndicator(new Curtains(75, 75, engl?"curtains":"Шторы", false, false, false, true, 1, 3).setDemo());
        }else{
            subsystem1B2 = new Subsystem("1B2-0", subsystemIndex++, 3, 2, "0");
            subsystem1B2.AddIndicator(new DimmerLamp(33, 25, engl?"ceiling lamp":"Люстра", false, false, false, true, 1, 3).setDemo());
            subsystem1B2.AddIndicator(new Lamp(66, 25, engl?"night-light":"Ночник", false, false, false, true, 1, 3).setDemo());
            subsystem1B2.AddIndicator(new WarmFloor(33, 75, engl?"underfloor heating ":"Тепл.пол", false, false, false, true, 1, 3).setDemo());
            subsystem1B2.AddIndicator(new Curtains(66, 75, engl?"curtains":"Шторы", false, false, false, true, 1, 3).setDemo());
        }



		m_cSubsystems.add(subsystem1B2);

		Room room1B2 = new Room(roomIndex++, engl?"younger son's room":"Комната мл. сына", "room1B2").AddSubsystem(subsystem1B2);
		m_cRooms.add(room1B2);

		subsystemIndex = 0;
        Subsystem subsystem1B3;
		if(port){
            subsystem1B3 = new Subsystem("1B3-0", subsystemIndex++, 3, 4, "0");
            subsystem1B3.AddIndicator(new DimmerLamp(25, 25, engl?"ceiling lamp":"Люстра", false, false, false, true, 1, 3).setDemo());
            subsystem1B3.AddIndicator(new Lamp(75, 25, engl?"night-light":"Ночник", false, false, false, true, 1, 3).setDemo());
            subsystem1B3.AddIndicator(new WarmFloor(25, 75, engl?"underfloor heating ":"Тепл.пол", false, false, false, true, 1, 3).setDemo());
            subsystem1B3.AddIndicator(new Curtains(75, 75, engl?"curtains":"Шторы", false, false, false, true, 1, 3).setDemo());
        }else{
            subsystem1B3 = new Subsystem("1B3-0", subsystemIndex++, 3, 2, "0");
            subsystem1B3.AddIndicator(new DimmerLamp(33, 25, engl?"ceiling lamp":"Люстра", false, false, false, true, 1, 3).setDemo());
            subsystem1B3.AddIndicator(new Lamp(66, 25, engl?"night-light":"Ночник", false, false, false, true, 1, 3).setDemo());
            subsystem1B3.AddIndicator(new WarmFloor(33, 75, engl?"underfloor heating":"Тепл.пол", false, false, false, true, 1, 3).setDemo());
            subsystem1B3.AddIndicator(new Curtains(66, 75, engl?"curtains":"Шторы", false, false, false, true, 1, 3).setDemo());
        }

		m_cSubsystems.add(subsystem1B3);

		Room room1B3 = new Room(roomIndex++, engl?"daughter's room":"Комната дочери", "room1B3").AddSubsystem(subsystem1B3);
		m_cRooms.add(room1B3);

		subsystemIndex = 0;
		Subsystem subsystem2;
		if(port) {
			 subsystem2 = new Subsystem("2-0", subsystemIndex++, 3, 4, "Освещение");
			subsystem2.AddIndicator(new DimmerLamp(15, 12, engl?"ceiling lamp 1":"Люстра 1", false, false, false, false, 1, 3).setDemo());
			subsystem2.AddIndicator(new DimmerLamp(50, 12, engl?"ceiling lamp 2":"Люстра 2", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new DimmerLamp(85, 12, engl?"ceiling lamp 3":"Люстра 3", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new Lamp(15, 37, engl?"local lighting":"Подсветка", false, false, false, true, 1, 3)	.setDemo());
			subsystem2.AddIndicator(new Lamp(50, 37, engl?"local lighting":"Подсветка", false, false, false, true, 1, 3)	.setDemo());
			subsystem2.AddIndicator(new Lamp(85, 37, engl?"local lighting":"Подсветка", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new Fan(15, 62, engl?"Fan 1":"Вентиляция 1", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new Conditioner(50, 62, engl?"AC (Air conditioning)":"Кондиционер", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new ClimatFan(85, 62, engl?"Fan 2":"Вентиляция 2", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new Curtains(15, 87, engl?"curtains S":"Шторы юг", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new Curtains(50, 87, engl?"curtains W":"Шторы запад", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new FireSensor(85, 87, engl?"Smoke detector":"Пож.датчик", false, true, false, true, 1, 3).setDemo());
		}else {
			subsystem2 = new Subsystem("2-0", subsystemIndex++, 5, 3, "Освещение");
			subsystem2.AddIndicator(new DimmerLamp(10, 17, engl?"ceiling lamp 1":"Люстра 1", false, false, false, false, 1, 3).setDemo());
			subsystem2.AddIndicator(new DimmerLamp(30, 17, engl?"ceiling lamp 2":"Люстра 2", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new DimmerLamp(50, 17, engl?"ceiling lamp 3":"Люстра 3", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new Lamp(70, 17, engl?"local lighting":"Подсветка", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new Lamp(90, 17, engl?"local lighting":"Подсветка", false, false, false, true, 1, 3).setDemo());

			subsystem2.AddIndicator(new Lamp(50, 50, engl?"local lighting":"Подсветка", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new Fan(10, 50, engl?"Fan 1":"Вентиляция 1", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new Conditioner(90, 50, engl?"AC (Air conditioning)":"Кондиционер", false, false, false, true, 1, 3).setDemo());

			subsystem2.AddIndicator(new ClimatFan(10, 83, engl?"Fan 2":"Вентиляция 2", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new Curtains(30, 83, engl?"curtains S":"Шторы юг", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new Curtains(70, 83, engl?"curtains W":"Шторы запад", false, false, false, true, 1, 3).setDemo());
			subsystem2.AddIndicator(new FireSensor(90, 83, engl?"Smoke detector":"Пож.датчик", false, true, false, true, 1, 3).setDemo());
		}

		Subsystem subsystem2media;

		if(port) {
			subsystem2media = new Subsystem("2-1", subsystemIndex++, 6, 6, "Мультирум");
			subsystem2media.AddIndicator(new ButtonMedia(15, 12, ButtonMedia.EJECT, "", false, 1, 4));
			subsystem2media.AddIndicator(new ButtonMedia(85, 12, ButtonMedia.POWER, "", false, 1, 4).Bind("11", "11"));

			subsystem2media.AddIndicator(new PlusMinus(15, 35, 0, "Гр.", true, 1, 4));
			subsystem2media.AddIndicator(new PlusMinus(85, 35, 1, "Кан.", true, 1, 4));
			subsystem2media.AddIndicator(new SourceSelector(50, 35, "Источник", true, 1, 4));

			subsystem2media.AddIndicator(new ButtonMedia(15, 58, ButtonMedia.MUTE, "", false, 1, 4).Bind("12", "12"));
			subsystem2media.AddIndicator(new ButtonMedia(85, 58, ButtonMedia.STOP, "", false, 1, 4));

			subsystem2media.AddIndicator(new ButtonMedia(10, 85, ButtonMedia.SKIP_B, "", false, 1, 4));
			subsystem2media.AddIndicator(new ButtonMedia(28, 85, ButtonMedia.REW, "", false, 1, 4));
			subsystem2media.AddIndicator(new ButtonMedia(50, 85, ButtonMedia.PAUSE, "", true, 1, 4).Bind("13", "13"));
			subsystem2media.AddIndicator(new ButtonMedia(72, 85, ButtonMedia.FF, "", false, 1, 4));
			subsystem2media.AddIndicator(new ButtonMedia(90, 85, ButtonMedia.SKIP_F, "", false, 1, 4));

			subsystem2media.AddIndicator(new MediaField(55, 73, "", "Seven Nation Army", true, 1, 4));
		}else {
			subsystem2media = new Subsystem("2-1", subsystemIndex++, 8, 4, "Мультирум");
			subsystem2media.AddIndicator(new ButtonMedia(25, 12, ButtonMedia.EJECT, "", false, 1, 4));
			subsystem2media.AddIndicator(new ButtonMedia(75, 12, ButtonMedia.POWER, "", false, 1, 4).Bind("11", "11"));

			subsystem2media.AddIndicator(new PlusMinus(25, 40, 0, "Гр.", true, 1, 4));
			subsystem2media.AddIndicator(new PlusMinus(75, 40, 1, "Кан.", true, 1, 4));
			subsystem2media.AddIndicator(new SourceSelector(50, 40, "Источник", true, 1, 4));

			subsystem2media.AddIndicator(new ButtonMedia(25, 68, ButtonMedia.MUTE, "", false, 1, 4).Bind("12", "12"));
			subsystem2media.AddIndicator(new ButtonMedia(75, 68, ButtonMedia.STOP, "", false, 1, 4));

			subsystem2media.AddIndicator(new ButtonMedia(10, 85, ButtonMedia.SKIP_B, "", false, 1, 4));
			subsystem2media.AddIndicator(new ButtonMedia(30, 85, ButtonMedia.REW, "", false, 1, 4));
			subsystem2media.AddIndicator(new ButtonMedia(50, 85, ButtonMedia.PLAY, "", true, 1, 4));
			subsystem2media.AddIndicator(new ButtonMedia(70, 85, ButtonMedia.FF, "", false, 1, 4));
			subsystem2media.AddIndicator(new ButtonMedia(90, 85, ButtonMedia.SKIP_F, "", false, 1, 4));


		}

		m_cSubsystems.add(subsystem2);

		Room room2 = new Room(roomIndex++, engl?"Living room":"Гостиная", "room2").AddSubsystem(subsystem2).AddSubsystem(subsystem2media);
		m_cRooms.add(room2);

		subsystemIndex = 0;
        Subsystem subsystem4a;
		if(port) {
            subsystem4a = new Subsystem("4A-0", subsystemIndex++, 3, 4, "0");
            subsystem4a.AddIndicator(new LeakageSensor(50, 25, engl?"Water detector":"Протечка", false, false, false, true, 1, 3).setDemo());
            subsystem4a.AddIndicator(new Valve(25, 66, engl?"Cold Water":"Хол. вода", false, false, false, true, 1, 3).setDemo());
            subsystem4a.AddIndicator(new Valve(75, 66, engl?"Hot Water":"Гор. вода", false, false, false, true, 1, 3).setDemo());
        }
		else{
            subsystem4a = new Subsystem("4A-0", subsystemIndex++, 3, 1, "0");
            subsystem4a.AddIndicator(new LeakageSensor(15, 50, engl?"Water detector":"Протечка", false, false, false, true, 1, 3).setDemo());
            subsystem4a.AddIndicator(new Valve(50, 50, engl?"Cold Water":"Хол. вода", false, false, false, true, 1, 3).setDemo());
            subsystem4a.AddIndicator(new Valve(85, 50, engl?"Hot Water":"Гор. вода", false, false, false, true, 1, 3).setDemo());
        }

		m_cSubsystems.add(subsystem4a);

		Room room4a = new Room(roomIndex++, engl?"Bathroom 1st floor":"Ванная 1 этаж", "room4a").AddSubsystem(subsystem4a);
		m_cRooms.add(room4a);

		subsystemIndex = 0;

		Subsystem subsystem4b;
		if(port) {
            subsystem4b = new Subsystem("4B-0", subsystemIndex++, 3, 4, "0");
            subsystem4b.AddIndicator(new LeakageSensor(50, 25, engl?"Water detector":"Протечка", false, false, false, true, 1, 3).setDemo());
            subsystem4b.AddIndicator(new Valve(25, 66, engl?"Cold Water":"Хол. вода", false, false, false, true, 1, 3).setDemo());
            subsystem4b.AddIndicator(new Valve(75, 66, engl?"Hot Water":"Гор. вода", false, false, false, true, 1, 3).setDemo());
        }else{
            subsystem4b = new Subsystem("4B-0", subsystemIndex++, 3, 2, "0");
            subsystem4b.AddIndicator(new LeakageSensor(30, 25, engl?"Water detector":"Протечка", false, false, false, true, 1, 3).setDemo());
            subsystem4b.AddIndicator(new Valve(70, 25, engl?"Cold Water":"Хол. вода", false, false, false, true, 1, 3).setDemo());
            subsystem4b.AddIndicator(new Valve(50, 75, engl?"Hot Water":"Гор. вода", false, false, false, true, 1, 3).setDemo());
        }


		m_cSubsystems.add(subsystem4b);

		Room room4b = new Room(roomIndex++, engl?"Bathroom 2nd floor":"Ванная 2 этаж", "room4b").AddSubsystem(subsystem4b);
		m_cRooms.add(room4b);

		subsystemIndex = 0;
        Subsystem subsystem4c;
		if(port){
            subsystem4c = new Subsystem("4C-0", subsystemIndex++, 3, 4, "0");
            subsystem4c.AddIndicator(new LeakageSensor(50, 25, engl?"Water detector":"Протечка", false, false, false, true, 1, 3).setDemo());
            subsystem4c.AddIndicator(new Valve(25, 66, engl?"Cold Water":"Хол. вода", false, false, false, true, 1, 3).setDemo());
            subsystem4c.AddIndicator(new Valve(75, 66, engl?"Hot Water":"Гор. вода", false, false, false, true, 1, 3).setDemo());
		}
		else{
            subsystem4c = new Subsystem("4C-0", subsystemIndex++, 3, 1, "0");
            subsystem4c.AddIndicator(new LeakageSensor(15, 50, engl?"Water detector":"Протечка", false, false, false, true, 1, 3).setDemo());
            subsystem4c.AddIndicator(new Valve(50, 50, engl?"Cold Water":"Хол. вода", false, false, false, true, 1, 3).setDemo());
            subsystem4c.AddIndicator(new Valve(85, 50, engl?"Hot Water":"Гор. вода", false, false, false, true, 1, 3).setDemo());
        }

		m_cSubsystems.add(subsystem4c);

		Room room4c = new Room(roomIndex++, engl?"WC":"Санузел", "room4c").AddSubsystem(subsystem4c);
		m_cRooms.add(room4c);

		subsystemIndex = 0;
        Subsystem subsystem3;
        if(port){
            subsystem3 = new Subsystem("3-0", subsystemIndex++, 3, 4, "0");
            subsystem3.AddIndicator(new Lamp(25, 15, engl?"Lighting":"Освещение", false, false, false, true, 1, 3).setDemo());
            subsystem3.AddIndicator(new PowerControl(75, 15, engl?"socket":"Розетка", false, false, false, true, 1, 3).setDemo());
            subsystem3.AddIndicator(new FireSensor(25, 45, engl?"Smoke detector":"Пож.датчик", false, true, false, true, 1, 3).setDemo());
            subsystem3.AddIndicator(new MotionSensor(75, 45, engl?"motion sensor":"Дат.движения", false, true, false, true, 1, 3).setDemo());
            subsystem3.AddIndicator(new Door2(25, 81, engl?"Garage doors 1":"Ворота", false, true, false, true, 1, 3).setDemo());
            subsystem3.AddIndicator(new DoorLock(75, 81, engl?"Garage doors 2":"Ворота2", false, true, false, true, 1, 3).setDemo());
        }else{
            subsystem3 = new Subsystem("3-0", subsystemIndex++, 4, 2, "0");
            subsystem3.AddIndicator(new Lamp(17, 25, engl?"Lighting":"Освещение", false, false, false, true, 1, 3).setDemo());
            subsystem3.AddIndicator(new PowerControl(50, 25, engl?"socket":"Розетка", false, false, false, true, 1, 3).setDemo());
            subsystem3.AddIndicator(new FireSensor(83, 25, engl?"Smoke detector":"Пож.датчик", false, true, false, true, 1, 3).setDemo());
            subsystem3.AddIndicator(new MotionSensor(17, 75, engl?"motion sensor":"Дат.движения", false, true, false, true, 1, 3).setDemo());
            subsystem3.AddIndicator(new Door2(50, 75, engl?"Garage doors 1":"Ворота", false, true, false, true, 1, 3).setDemo());
            subsystem3.AddIndicator(new DoorLock(83, 75, engl?"Garage doors 2":"Ворота2", false, true, false, true, 1, 3).setDemo());

        }

		m_cSubsystems.add(subsystem3);

		Room room3 = new Room(roomIndex++, engl?"Garage":"Гараж", "room3").AddSubsystem(subsystem3);
		m_cRooms.add(room3);

		subsystemIndex = 0;
		Subsystem subsystem3a;
		if(port){
            subsystem3a = new Subsystem("3A-0", subsystemIndex++, 3, 4, "0");
            subsystem3a.AddIndicator(new Lamp(25, 15, engl?"Lighting":"Освещение", false, false, false, true, 1, 3).setDemo());
            subsystem3a.AddIndicator(new PowerControl(75, 15, engl?"socket":"Розетка", false, false, false, true, 1, 3).setDemo());
            subsystem3a.AddIndicator(new FireSensor(25, 45, engl?"Smoke detector":"Пож.датчик", false, true, false, true, 1, 3).setDemo());
            subsystem3a.AddIndicator(new MotionSensor(75, 45, engl?"motion sensor":"Дат.движения", false, true, false, true, 1, 3).setDemo());
            subsystem3a.AddIndicator(new Door2(25, 81, engl?"Garage doors 1":"Ворота", false, true, false, true, 1, 3).setDemo());
            subsystem3a.AddIndicator(new DoorLock(75, 81, engl?"Garage doors 2":"Ворота2", false, true, false, true, 1, 3).setDemo());
		}else {
            subsystem3a = new Subsystem("3A-0", subsystemIndex++, 4, 2, "0");
            subsystem3a.AddIndicator(new Lamp(17, 25, engl?"Lighting":"Освещение", false, false, false, true, 1, 3).setDemo());
            subsystem3a.AddIndicator(new PowerControl(50, 25, engl?"socket":"Розетка", false, false, false, true, 1, 3).setDemo());
            subsystem3a.AddIndicator(new FireSensor(83, 25, engl?"Smoke detector":"Пож.датчик", false, true, false, true, 1, 3).setDemo());
            subsystem3a.AddIndicator(new MotionSensor(17, 75,  engl?"motion sensor":"Дат.движения", false, true, false, true, 1, 3).setDemo());
            subsystem3a.AddIndicator(new Door2(50, 75, engl?"Garage doors 1":"Ворота", false, true, false, true, 1, 3).setDemo());
            subsystem3a.AddIndicator(new DoorLock(83, 75, engl?"Garage doors 2":"Ворота2", false, true, false, true, 1, 3).setDemo());
        }

		m_cSubsystems.add(subsystem3a);

		Room room3a = new Room(roomIndex++, engl?"Son's Garage":"Гараж сына", "room3a").AddSubsystem(subsystem3a);
		m_cRooms.add(room3a);


//		Subsystem subsystem5 = new Subsystem(getContext(), index++, 4, 5, "Медиа", m_pMenu).SetListeners(onRoomMenuClickListener, onRoomMenuLongClickListener, onRoomAlarmListener);
//		subsystem5.AddIndicator(new ButtonOn(this, getContext(), 15, 15, "", false, false));
//		subsystem5.AddIndicator(new ButtonOff(this, getContext(), 40, 15, "", false, false));
//		subsystem5.AddIndicator(new ButtonBig(this, getContext(), 75, 15, "MUTE", false, true));
//		subsystem5.AddIndicator(new GaugeH(this, getContext(), 75, 35, "Яркость", false, false));
//		subsystem5.AddIndicator(new GaugeV(this, getContext(), 75, 75, "Громкость", false, true));
//		subsystem5.AddIndicator(new Cursor(this, getContext(), 27, 45, "", false, true));
//		subsystem5.AddIndicator(new ButtonSmall(this, getContext(), 15, 75, "1", false, false));
//		subsystem5.AddIndicator(new ButtonSmall(this, getContext(), 15, 90, "2", false, false));
//		subsystem5.AddIndicator(new ButtonSmall(this, getContext(), 40, 75, "3", false, false));
//		subsystem5.AddIndicator(new ButtonSmall(this, getContext(), 40, 90, "4", false, false));
//
//		m_pSwitcher.addView(subsystem5);

		subsystemIndex = 0;
		Subsystem subsystem5;
		if(port){
			subsystem5 = new Subsystem("56-0", subsystemIndex++, 5, 6, "Фасад");
			subsystem5.AddIndicator(new Lamp(10, 10, engl?"walkway 1":"Дорожка 1", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(30, 10, engl?"walkway 2":"Дорожка 2", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(50, 10, engl?"walkway 3":"Дорожка 3", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(70, 10, engl?"walkway 4":"Дорожка 4", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(90, 10, engl?"walkway 5":"Дорожка 5", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(10, 30, engl?"walkway 6":"Дорожка 6", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(30, 30, engl?"walkway 7":"Дорожка 7", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(50, 30, engl?"walkway 8":"Дорожка 8", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(70, 30, engl?"walkway 9":"Дорожка 9", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(90, 30, engl?"walkway 10":"Дорожка 10", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(10, 50, engl?"arbor 1":"Беседка 1", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(30, 50, engl?"arbor 2":"Беседка 2", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(50, 50, engl?"arbor 3":"Беседка 3", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(70, 50, engl?"arbor 4":"Беседка 4", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(90, 50, engl?"arbor 5":"Беседка 5", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(20, 70, engl?"fountain 1":"Фонтан 1", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(40, 70, engl?"fountain 2":"Фонтан 2", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(60, 70, engl?"fountain 3":"Фонтан 3", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(80, 70, engl?"fountain 4":"Фонтан 4", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(33, 90, engl?"":"Фасад 1", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(66, 90, engl?"":"Фасад 2", false, false, false, true, 1, 5).setDemo());
		}else {
			subsystem5 = new Subsystem("56-0", subsystemIndex++, 8, 4, "Фасад");
			subsystem5.AddIndicator(new Lamp(9, 16, engl?"walkway 1":"Дорожка 1", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(18, 16, engl?"walkway 2":"Дорожка 2", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(27, 16, engl?"walkway 3":"Дорожка 3", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(36, 16, engl?"walkway 4":"Дорожка 4", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(45, 16, engl?"walkway 5":"Дорожка 5", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(54, 16, engl?"walkway 6":"Дорожка 6", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(63, 16, engl?"walkway 7":"Дорожка 7", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(72, 16, engl?"walkway 8":"Дорожка 8", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(81, 16, engl?"walkway 9":"Дорожка 9", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(90, 16, engl?"walkway 10":"Дорожка 10", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(10, 62, engl?"arbor 1":"Беседка 1", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(30, 62, engl?"arbor 2":"Беседка 2", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(50, 62, engl?"arbor 3":"Беседка 3", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(70, 62, engl?"arbor 4":"Беседка 4", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(90, 62, engl?"arbor 5":"Беседка 5", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(20, 86, engl?"fountain 1":"Фонтан 1", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(40, 86, engl?"fountain 2":"Фонтан 2", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(60, 86, engl?"fountain 3":"Фонтан 3", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new DimmerLamp(80, 86, engl?"fountain 4":"Фонтан 4", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(33, 37, engl?"":"Фасад 1", false, false, false, true, 1, 5).setDemo());
			subsystem5.AddIndicator(new Lamp(66, 37, engl?"":"Фасад 2", false, false, false, true, 1, 5).setDemo());

		}


		m_cSubsystems.add(subsystem5);

		Subsystem subsystem6;
		if(port) {
			subsystem6 = new Subsystem("56-1", subsystemIndex++, 4, 5, "Двор");
			subsystem6.AddIndicator(new Lamp(13, 13, engl?"walkway 1":"Дорожка 1", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(37, 13, engl?"walkway 2":"Дорожка 2", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(63, 13, engl?"walkway 3":"Дорожка 3", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(87, 13, engl?"walkway 4":"Дорожка 4", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(13, 37, engl?"walkway 5":"Дорожка 5", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(37, 37, engl?"walkway 6":"Дорожка 6", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(63, 37, engl?"walkway 7":"Дорожка 7", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(87, 37, engl?"walkway 8":"Дорожка 8", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(13, 63, engl?"arbor 1":"Беседка 1", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(37, 63, engl?"arbor 2":"Беседка 2", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(63, 63, engl?"arbor 3":"Беседка 3", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(87, 63, engl?"arbor 4":"Беседка 4", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(25, 87, engl?"fountain 1":"Фонтан 1", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(50, 87, engl?"fountain 2":"Фонтан 2", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(75, 87, engl?"fountain 3":"Фонтан 3", false, false, false, true, 1, 4).setDemo());
		}else {
			subsystem6 = new Subsystem("56-1", subsystemIndex++, 6, 3, "Двор");
			subsystem6.AddIndicator(new Lamp(8, 25, engl?"walkway 1":"Дорожка 1", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(22, 25, engl?"walkway 2":"Дорожка 2", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(36, 25, engl?"walkway 3":"Дорожка 3", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(50, 25, engl?"walkway 4":"Дорожка 4", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(64, 25, engl?"walkway 5":"Дорожка 5", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(78, 25, engl?"walkway 6":"Дорожка 6", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new Lamp(92, 25, engl?"walkway7 ":"Дорожка 7", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(20, 50, engl?"arbor 1":"Беседка 1", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(40, 50, engl?"arbor 2":"Беседка 2", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(60, 50, engl?"arbor 3":"Беседка 3", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(80, 50, engl?"arbor 4":"Беседка 4", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(25, 75, engl?"fountain 1":"Фонтан 1", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(50, 75, engl?"fountain 2":"Фонтан 2", false, false, false, true, 1, 4).setDemo());
			subsystem6.AddIndicator(new DimmerLamp(75, 75, engl?"fountain 3":"Фонтан 3", false, false, false, true, 1, 4).setDemo());
		}

		m_cSubsystems.add(subsystem6);

		Room room56 = new Room(roomIndex++, engl?"Grounds (Landscape)":"Территория", "room56").AddSubsystem(subsystem5).AddSubsystem(subsystem6);
		m_cRooms.add(room56);

		//Room room6 = new Room(roomIndex++, "Территория 2").AddSubsystem(subsystem6);
		//m_cRooms.add(room6);

		m_sSummaryName = "СРС";

		m_sSummaryText = engl?"Integration & Automation System. Demo"
				:"Система интеграции и автоматизации - демонстрационная версия";

		m_cGroups.put(engl?"script":"Макросы", new ArrayList<Room>());
		m_cGroups.get(engl?"script":"Макросы").add(room0);

		m_cGroups.put(engl?"Group 1":"Основные", new ArrayList<Room>());
		m_cGroups.get(engl?"Group 1":"Основные").add(room1);
		m_cGroups.get(engl?"Group 1":"Основные").add(room1A);
		m_cGroups.get(engl?"Group 1":"Основные").add(room1B1);
		m_cGroups.get(engl?"Group 1":"Основные").add(room1B2);
		m_cGroups.get(engl?"Group 1":"Основные").add(room1B3);

		m_cGroups.put(engl?"Group 2":"Дополнительные", new ArrayList<Room>());
		m_cGroups.get(engl?"Group 2":"Дополнительные").add(room2);
		m_cGroups.get(engl?"Group 2":"Дополнительные").add(room3);
		m_cGroups.get(engl?"Group 2":"Дополнительные").add(room3a);
		m_cGroups.get(engl?"Group 2":"Дополнительные").add(room4a);
		m_cGroups.get(engl?"Group 2":"Дополнительные").add(room4b);
		m_cGroups.get(engl?"Group 2":"Дополнительные").add(room4c);
		m_cGroups.get(engl?"Group 2":"Дополнительные").add(room56);
		Log.d("Glindor2","Конец Конфига");

	}

	public Config(InputStream is)
	{
		DEMO = false;
		
        try
        {
        	 /* Get a SAXParser from the SAXPArserFactory. */
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            /* Get the XMLReader of the SAXParser we created. */
            XMLReader xr = sp.getXMLReader();
            /* Create a new ContentHandler and apply it to the XML-Reader*/
            RoomsParser myExampleHandler = new RoomsParser();
            xr.setContentHandler(myExampleHandler);

			Log.d("XML", "Start parsing");
            /* Parse the xml-data from our URL. */
            xr.parse(new InputSource(is));
            /* Parsing has finished. */
            Log.d("XML", "Finish parsing");

            
            m_cRooms = myExampleHandler.getRooms();
			for (Room pRoom : m_cRooms)
			{
				Log.d("XML", "Room[" + pRoom.m_iIndex + "] = " + pRoom.m_sName);
				m_cSubsystems.addAll(pRoom.m_cSubsystems);
			}
			m_cGroups = myExampleHandler.getGroups();
			m_cFavorites = myExampleHandler.getFavorites();

            m_sIP = myExampleHandler.getIP();
            m_iPort = myExampleHandler.getPort();
//            m_iPortExt = myExampleHandler.getPortExt();
            
            m_iPollPeriod = myExampleHandler.getPollPeriod();
            m_sMasterCode = myExampleHandler.getMasterCode();
            
            m_sSummaryName = myExampleHandler.getSummaryName();
            m_sSummaryText = myExampleHandler.getSummaryText();
			Log.d("XML", "Config loaded OK!");
        }
        catch (Exception e) 
        {
        	Log.e("CFG","error occurred while parsing xml file: " + e.getMessage());
        	e.printStackTrace();
        	
        	m_cSubsystems.clear();
        }
	}
	
	public void SaveXml(FileOutputStream fileos)
	{
        //we create a XmlSerializer in order to write xml data
        XmlSerializer serializer = Xml.newSerializer();
        try
        {
            serializer.setOutput(fileos, "UTF-8");
            //Write <?xml declaration with encoding (if encoding not null) and standalone flag (if standalone not null)
            serializer.startDocument(null, Boolean.TRUE);
            //set indentation option
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
           
            serializer.startTag(null, "smartflat");
            final Date dumpDate = new Date(System.currentTimeMillis());
            serializer.attribute(null, "time", new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(dumpDate));
            serializer.attribute(null, "serverip", m_sIP);
            serializer.attribute(null, "serverport", String.valueOf(m_iPort));
            serializer.attribute(null, "pollperiod", String.valueOf(m_iPollPeriod));
            serializer.attribute(null, "mastercode", String.valueOf(m_sMasterCode));
            //serializer.attribute(null, "kdserverportexternal", String.valueOf(m_iPortExt));

            serializer.startTag(null, "summary");
            serializer.attribute(null, "name", m_sSummaryName);
            serializer.attribute(null, "text", m_sSummaryText);
            serializer.endTag(null, "summary");
   
            serializer.startTag(null, "rooms");
            serializer.attribute(null, "count", String.valueOf(m_cRooms.size()));
            for (Room pRoom : m_cRooms) {
				serializer.startTag(null, "room");
				serializer.attribute(null, "name", pRoom.m_sName);
				serializer.attribute(null, "id", pRoom.m_sID);

				serializer.startTag(null, "subsystems");
				serializer.attribute(null, "count", String.valueOf(pRoom.m_cSubsystems.size()));
				Log.d("XML", "subsystems count = " + pRoom.m_cSubsystems.size());

				for (Subsystem pSubsystem : pRoom.m_cSubsystems) {
					serializer.startTag(null, "subsystem");
					serializer.attribute(null, "id", pSubsystem.m_sID);
					serializer.attribute(null, "type", pSubsystem.m_sName);
					serializer.attribute(null, "alarmaddress", pSubsystem.m_sAlarm);
					serializer.attribute(null, "scalex", String.valueOf(pSubsystem.m_iGridWidth));
					serializer.attribute(null, "scaley", String.valueOf(pSubsystem.m_iGridHeight));

					int relays = 0;
					int regulators = 0;
					int alarmsensors = 0;
					int sensors = 0;
					int macros = 0;
					int doors = 0;
					int doors2 = 0;
					int cursors = 0;
					int climat = 0;
					int mediabuttons = 0;
					int swings = 0;
					int mediafields = 0;
					int selectors = 0;
					int idiss = 0;
					int links = 0;

					for (Indicator pIndicator : pSubsystem.m_cIndicators)
					{
						if (pIndicator instanceof Lamp ||
								pIndicator instanceof Curtains ||
								pIndicator instanceof WarmFloor ||
								pIndicator instanceof Fan ||
								pIndicator instanceof Valve ||
                                pIndicator instanceof Radiator ||
                                pIndicator instanceof Fountain ||
//                                pIndicator instanceof Fountain2 ||
//                			pIndicator instanceof ButtonOn ||
//                			pIndicator instanceof ButtonOff ||
								pIndicator instanceof PowerControl)
							relays++;

						if (pIndicator instanceof DimmerLamp ||
								pIndicator instanceof DimmerFan ||
								pIndicator instanceof Conditioner ||
								pIndicator instanceof Conditioner2Temp ||
								pIndicator instanceof Battery ||
								pIndicator instanceof Battery2 ||
								pIndicator instanceof WarmFloorDevi ||
								pIndicator instanceof WarmFloorDevi2 ||
								pIndicator instanceof NodeSeekBar
//                			pIndicator instanceof GaugeH ||
//                			pIndicator instanceof GaugeV
								)
							regulators++;

						if (pIndicator instanceof FireSensor ||
								pIndicator instanceof MotionSensor ||
								pIndicator instanceof LeakageSensor)
							alarmsensors++;

						if (pIndicator instanceof SensorTemperature ||
								pIndicator instanceof SensorHumidity ||
								pIndicator instanceof SensorIlluminance ||
								pIndicator instanceof SensorCO)
							sensors++;

						if (pIndicator instanceof Macro ||
								pIndicator instanceof MacroCamOnOff ||
								pIndicator instanceof MacroFireSensor ||
								pIndicator instanceof MacroMotionSensor ||
								pIndicator instanceof MacroFreePass ||
								pIndicator instanceof MacroLowLevel ||
								pIndicator instanceof MacroDezinfection ||
								pIndicator instanceof MacroPumpFail ||
								pIndicator instanceof MacroFilterFail ||
								pIndicator instanceof MacroPumpFilter ||
								pIndicator instanceof MacroPumpWorkMode ||
								pIndicator instanceof MacroFilterCleaning ||
								pIndicator instanceof MacroPumpAddWater ||
								pIndicator instanceof MacroAlarm
//								||								pIndicator instanceof MacroFlashlight
						)
							macros++;

						if (pIndicator instanceof ClimatConditioner ||
								pIndicator instanceof ClimatFan ||
								pIndicator instanceof ClimatWarmFloor ||
								pIndicator instanceof ClimatRadiator)
							climat++;

						if (pIndicator instanceof Door ||
							pIndicator instanceof DoorLock
								)
							doors++;

						if (pIndicator instanceof Door2)
							doors2++;

						if (pIndicator instanceof ButtonMedia)
							mediabuttons++;

						if (pIndicator instanceof PlusMinus)
							swings++;

						if (pIndicator instanceof SourceSelector)
							selectors++;

						if (pIndicator instanceof MediaField)
							mediafields++;

						if (pIndicator instanceof CamVideo)
							idiss++;

						if (pIndicator instanceof NodeAlarm)
							links++;

//                	if(pIndicator instanceof Cursor)
//                		cursors++;
					}

					serializer.startTag(null, "relays");
					serializer.attribute(null, "count", String.valueOf(relays));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof Lamp ||
								pIndicator instanceof Curtains ||
								pIndicator instanceof WarmFloor ||
								pIndicator instanceof Fan ||
								pIndicator instanceof Valve ||
								pIndicator instanceof Radiator ||
                                pIndicator instanceof Fountain ||
//                                pIndicator instanceof Fountain2 ||
//                			pIndicator instanceof ButtonOn ||
//                			pIndicator instanceof ButtonOff ||
								pIndicator instanceof PowerControl)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "relays");

					serializer.startTag(null, "regulators");
					serializer.attribute(null, "count", String.valueOf(regulators));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof DimmerLamp ||
								pIndicator instanceof DimmerFan ||
								pIndicator instanceof Conditioner ||
								pIndicator instanceof Conditioner2Temp ||
								pIndicator instanceof Battery ||
								pIndicator instanceof Battery2 ||
								pIndicator instanceof WarmFloorDevi ||
								pIndicator instanceof WarmFloorDevi2 ||
								pIndicator instanceof NodeSeekBar
//                			pIndicator instanceof GaugeH ||
//                			pIndicator instanceof GaugeV
								)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "regulators");

					serializer.startTag(null, "alarmsensors");
					serializer.attribute(null, "count", String.valueOf(alarmsensors));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof FireSensor ||
								pIndicator instanceof MotionSensor ||
								pIndicator instanceof LeakageSensor)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "alarmsensors");

					serializer.startTag(null, "sensors");
					serializer.attribute(null, "count", String.valueOf(sensors));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof SensorTemperature ||
								pIndicator instanceof SensorIlluminance ||
								pIndicator instanceof SensorHumidity ||
								pIndicator instanceof SensorCO)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "sensors");

					serializer.startTag(null, "macros");
					serializer.attribute(null, "count", String.valueOf(macros));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof Macro ||
							pIndicator instanceof MacroCamOnOff ||
								pIndicator instanceof MacroMotionSensor ||
								pIndicator instanceof MacroFireSensor ||
								pIndicator instanceof MacroFreePass ||
								pIndicator instanceof MacroLowLevel ||
								pIndicator instanceof MacroDezinfection ||
								pIndicator instanceof MacroPumpFail ||
								pIndicator instanceof MacroFilterFail ||
								pIndicator instanceof MacroPumpFilter ||
								pIndicator instanceof MacroPumpWorkMode ||
								pIndicator instanceof MacroFilterCleaning ||
								pIndicator instanceof MacroPumpAddWater ||
								pIndicator instanceof MacroAlarm
//								||pIndicator instanceof MacroFlashlight
						)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "macros");

					serializer.startTag(null, "climatics");
					serializer.attribute(null, "count", String.valueOf(climat));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
					if (pIndicator instanceof ClimatConditioner ||
							pIndicator instanceof ClimatFan ||
							pIndicator instanceof ClimatWarmFloor ||
							pIndicator instanceof ClimatRadiator)
						pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "climatics");

					serializer.startTag(null, "doors");
					serializer.attribute(null, "count", String.valueOf(doors));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof Door ||
							pIndicator instanceof DoorLock)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "doors");

					serializer.startTag(null, "doors2");
					serializer.attribute(null, "count", String.valueOf(doors2));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof Door2)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "doors2");

					serializer.startTag(null, "mediabuttons");
					serializer.attribute(null, "count", String.valueOf(mediabuttons));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof ButtonMedia)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "mediabuttons");

					serializer.startTag(null, "swings");
					serializer.attribute(null, "count", String.valueOf(swings));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof PlusMinus)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "swings");

					serializer.startTag(null, "selectors");
					serializer.attribute(null, "count", String.valueOf(selectors));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof SourceSelector)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "selectors");

					serializer.startTag(null, "mediafields");
					serializer.attribute(null, "count", String.valueOf(mediafields));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof MediaField)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "mediafields");

					serializer.startTag(null, "cursors");
					serializer.attribute(null, "count", String.valueOf(cursors));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
//                	if(pIndicator instanceof Cursor)
//                		pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "cursors");

					serializer.startTag(null, "IDISs");
					serializer.attribute(null, "count", String.valueOf(idiss));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof CamVideo)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "IDISs");

					serializer.startTag(null, "links");
					serializer.attribute(null, "count", String.valueOf(links));

					for (Indicator pIndicator : pSubsystem.m_cIndicators) {
						if (pIndicator instanceof NodeAlarm)
							pIndicator.SaveXML(serializer);
					}

					serializer.endTag(null, "links");

					serializer.endTag(null, "subsystem");
				}
				serializer.endTag(null, "subsystems");
				serializer.endTag(null, "room");
			}
            
            serializer.endTag(null, "rooms");

			serializer.startTag(null, "groups");
			serializer.attribute(null, "count", String.valueOf(m_cGroups.size()));
			for (String key : m_cGroups.keySet()) {
				serializer.startTag(null, "group");
				serializer.attribute(null, "name", key);

				serializer.startTag(null, "rooms");
				serializer.attribute(null, "count", String.valueOf(m_cGroups.get(key).size()));
				for(Room pRoom : m_cGroups.get(key))
				{
					if(pRoom!=null)
					{
						serializer.startTag(null, "room");
						serializer.attribute(null, "id", pRoom.m_sID);
						serializer.endTag(null, "room");
					}
				}
				serializer.endTag(null, "rooms");
				serializer.endTag(null, "group");
			}
			serializer.endTag(null, "groups");
			serializer.startTag(null, "favorites");
			serializer.attribute(null, "count", String.valueOf(m_cFavorites.size()));
			for(Room pRoom : m_cFavorites)
			{
				serializer.startTag(null, "room");
				serializer.attribute(null, "id", pRoom.m_sID);
				serializer.attribute(null, "locked", "1");
				serializer.endTag(null, "room");
			}
			serializer.endTag(null, "favorites");
            serializer.endTag(null, "smartflat");
            serializer.endDocument();
            //write xml data into the FileOutputStream
            serializer.flush();
            //finally we close the file stream
            fileos.close();
        } 
        catch (Exception e)
        {
        	Log.e("CFG","error occurred while creating xml file: " + e);
			e.printStackTrace();
        }
	}

	@Nullable
	public static Config LoadXml(boolean port,Uri importData, Context context)
	{
		System.out.println("Glindor227 Тестовый вывод информации");
		portOrientation = port;
		String sFilename = Environment.getExternalStorageDirectory() +  String.format("/Android/data/%s/files/config.xml", context.getPackageName());
		System.out.println("Glindor227 Выбрали файл"+sFilename);

		InputStream is = null;

		if(importData != Uri.EMPTY)
		{
			String scheme = importData.getScheme();

			if(ContentResolver.SCHEME_CONTENT.equals(scheme))
			{
				try
				{
					ContentResolver cr = context.getContentResolver();
					AssetFileDescriptor afd = cr.openAssetFileDescriptor(importData, "r");
					is = cr.openInputStream(importData);
				}
				catch(FileNotFoundException e)
				{
					Log.e("FileNotFoundException", "can't create FileInputStream");
					return null;
				}
			}
			else
			{
				sFilename = importData.getEncodedPath();
			}
		}

		File newxmlfile = null;
		if(is == null)
		{
			newxmlfile = new File(sFilename);
			if(!newxmlfile.exists())
			{
				Log.e("CFG", "Can't find file '" + newxmlfile.getAbsolutePath() + "'");
				return null;
			}

			try
			{
				is = new FileInputStream(newxmlfile);
			}
			catch(FileNotFoundException e)
			{
				Log.e("FileNotFoundException", "can't create FileInputStream");
			}
		}

		return new Config(is);
	}

	public static void SaveXml(Config pConfig, Context context)
	{
		String sFilename = String.format("/Android/data/%s/files/config.xml", context.getPackageName());
		SaveXml(pConfig, Environment.getExternalStorageDirectory() + sFilename);
	}

	private static void SaveXml(Config pConfig, String sFilename)
	{
		File newxmlfile = new File(sFilename);
		try
		{
			File dumpdir = newxmlfile.getParentFile();
			boolean dirReady = dumpdir.isDirectory() || dumpdir.mkdirs();
			//noinspection ResultOfMethodCallIgnored
			newxmlfile.createNewFile();
		}
		catch(IOException e)
		{
			Log.e("IOException", "exception in createNewFile() method " + e.getMessage());
		}

		//we have to bind the new file with a FileOutputStream
		FileOutputStream fileos = null;
		try
		{
			fileos = new FileOutputStream(newxmlfile);
		}
		catch(FileNotFoundException e)
		{
			Log.e("FileNotFoundException", "can't create FileOutputStream");
		}

		pConfig.SaveXml(fileos);
		Log.e("XML", "saved config to file '" + sFilename +"'");
	}

}
