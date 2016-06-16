package com.choucheng.dengdao2.tools;


public class GD_MapUtil {

	/**
	 * 把LatLng对象转化为LatLonPoint对象
	 *//*
	public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
		return new LatLonPoint(latlon.latitude, latlon.longitude);
	}

	*//**
	 * 把LatLonPoint对象转化为LatLon对象
	 *//*
	public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
		return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
	}

	*//**
	 * 把集合体的LatLonPoint转化为集合体的LatLng
	 *//*
	public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
		ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
		for (LatLonPoint point : shapes) {
			LatLng latLngTemp = GD_MapUtil.convertToLatLng(point);
			lineShapes.add(latLngTemp);
		}
		return lineShapes;
	}

	
	*//**
	 * 根据定位的数据得到详细地址
	 * @param location
	 * @return
	 *//*
	public static String getaddress(AMapLocation Location){
		  String[] address=new String[3];
			address[0]=Location.getCity();
			if(address[0].equals("")){
				address[0]=Location.getProvince();
			}
			address[1]=Location.getDistrict();
			address[2]=Location.getStreet();
			String myaddress="";
			for(int i=0;i<address.length;i++){
				if(!address[i].equals("")){
					myaddress=myaddress+address[i]+"-";
				}
			}
			return myaddress;
	}
	
	*//****
	 * 地址编码找到的地址信息
	 * @param reAddress
	 * @return
	 *//*
	public static  String getaddressGeo(RegeocodeAddress reAddress){
		String myaddress="";
		String[] address=new String[4];
		address[0]=reAddress.getCity();
		if(address[0].equals("")){
			address[0]=reAddress.getProvince();
		}
		address[1]=reAddress.getDistrict();
		address[2]=reAddress.getTownship();
		address[3]=reAddress.getRoads().get(0).getName();
		
		for(int i=0;i<address.length;i++){
			if(!address[i].equals("")){
				myaddress=myaddress+address[i]+"-";
			}
		}
		myaddress=myaddress.substring(0, myaddress.length()-1);
		
		return myaddress;
	}*/
	
}
