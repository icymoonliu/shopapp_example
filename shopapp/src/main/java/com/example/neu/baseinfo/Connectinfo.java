package com.example.neu.baseinfo;

public class Connectinfo {

	public static String contexturl="http://10.0.3.2:8080/shopserver";//上下文路径
	
	public static String loginurl = contexturl+"/appuser/login";// 登录
	public static String regurl = contexturl+"/appuser/reg";// 登录
	public static String autologinurl = contexturl+"/appuser/autoLogin";//自动登录
	public static String logouturl = contexturl+"/appuser/logout";//退出登录
	public static String cateurl = contexturl+"/appcate/getAllCatesList";//商品分类列表
	public static String newgoodsurl = contexturl+"/appgoods/getNewGoods";//新到商品列表
	public static String salesgoodsurl = contexturl+"/appgoods/getSalesGoods";//热销商品列表
	public static String goodsurl = contexturl+"/appgoods/getGoodsByCate";//商品列表
	public static String goodsdetailurl = contexturl+"/appgoods/getGoodsDetailById";//商品详情
	public static String searchgoodsurl = contexturl+"/appgoods/searchGoods";//搜索商品
	public static String orderurl = contexturl+"/apporder/getMyOrders";//订单列表
	public static String addorderurl = contexturl+"/apporder/addOrder";//生成订单
	
	public static String adImg1 = contexturl+"/images/adver/1.jpg";//轮播广告图片URL
	public static String adImg2 = contexturl+"/images/adver/2.jpg";//轮播广告图片URL
	public static String adImg3 = contexturl+"/images/adver/3.jpg";//轮播广告图片URL
}
