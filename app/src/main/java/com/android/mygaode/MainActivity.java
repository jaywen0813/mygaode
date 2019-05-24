package com.android.mygaode;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.RouteSearch;
import com.android.mygaode.adapter.SearchAdapters;
import com.android.mygaode.weight.StatusBarUtils;
import com.android.mygaode.weight.StatusbarColorUtils;
import com.android.mygaode.weight.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//测试2019年5月18日 21:25:13
public class MainActivity extends AppCompatActivity implements  LocationSource, AMapLocationListener,View.OnClickListener, PoiSearch.OnPoiSearchListener, AMap.OnMarkerClickListener {

    // 抽屉菜单对象
    private ActionBarDrawerToggle drawerbar;
    LinearLayout main_left_drawer_layout;
    DrawerLayout drawerLayout;

    ImageView img_menu;

    MapView mapView;
    AMap aMap;



    private EditText etSearch;
    private ImageView imgSousuo;
    private RelativeLayout bottomSheet;
    private LinearLayout llBottom,llBottom2;
    private TextView tvMyaddress,tvMyaddress2;
    private TextView tvChakan,tvChakan2;
    private ListView listview;

    private TextView tvWeather;
    private TextView tvJisuanqi;



    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条




    //北京市的经纬度   默认进来是北京，后面定位成功以后，中心点始终是自己
    private LatLng centerWHpoint = new LatLng(39.910066, 116.386695);

    private String myCity="北京市";//当前城市  默认北京

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private LocationSource.OnLocationChangedListener mListener;

    //定位蓝点
    MyLocationStyle myLocationStyle;
    private UiSettings mUiSettings;//定义一个UiSettings对象

    //自己的纬度
    double mylat;
    //自己的经度
    double mylng;

    LatLng mylatlng;//自己的经纬度
    LatLonPoint mylatlngpoint;//搜索时，自己的位置为中心点


    private BottomSheetBehavior<RelativeLayout> behavior;//滑动控件


    SearchAdapters adapters;

//搜索相关

    //搜索列表集合
    LatLngBounds.Builder newbounds2;
    List<PoiItem> poiItems;//搜索后赋值的集合
    private PoiSearch.SearchBound searchBound;

    //用来搜索的时候，识别是否是常用字
    List<String> aa=new ArrayList<>();

    //旧的地图标记
    private Marker oldMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//第一次创建项目


        requestPermissions();//动态申请权限

        //初始化
        initView();
        initPageData();//添加搜索数据
        mapView.onCreate(savedInstanceState);

//        StatusBarUtils.with(this)
//                .init();
        StatusBarUtils.setStatusTransparent(this);

    }
    protected void initPageData() {
        aa.add("加油站");
        aa.add("厕所");
        aa.add("洗车");
        aa.add("停车场");
        aa.add("美食");
        aa.add("洗浴");
        aa.add("网吧");
        aa.add("商场");
        aa.add("超市");
        aa.add("医院");
        aa.add("足疗");
        aa.add("快递");
        aa.add("药店");
        aa.add("KTV");
        aa.add("夜店");
        aa.add("美容美发");
        aa.add("电讯营业厅");
        aa.add("电信营业厅");
        aa.add("移动营业厅");
        aa.add("联通营业厅");
        aa.add("快餐");
        aa.add("中餐");
        aa.add("火锅");
        aa.add("咖啡厅");
        aa.add("咖啡店");
        aa.add("小吃");
        aa.add("自助餐");
        aa.add("茶餐厅");
        aa.add("肯德基");
        aa.add("蛋糕店");
        aa.add("周黑鸭");
        aa.add("糕饼店");
        aa.add("绝味鸭脖");
        aa.add("日本料理");
        aa.add("韩国料理");
        aa.add("清真");
        aa.add("麦当劳");
        aa.add("海底捞");
        aa.add("必胜客");
        aa.add("蛋糕房");
        aa.add("甜品店");
        aa.add("冷饮店");
        aa.add("奶茶店");
        aa.add("火锅自助");
        aa.add("星巴克");
        aa.add("德克士");
        aa.add("宾馆");
        aa.add("星级酒店");
        aa.add("酒店");
        aa.add("快捷酒店");
        aa.add("青年旅舍");
        aa.add("度假公寓");
        aa.add("招待所");
        aa.add("钟点房");
        aa.add("如家");
        aa.add("汉庭");
        aa.add("旅馆");
        aa.add("三星级");
        aa.add("四星级");
        aa.add("五星级");
        aa.add("维也纳酒店");
        aa.add("家庭旅馆");
        aa.add("速8");
        aa.add("布丁");
        aa.add("莫泰");
        aa.add("锦江之星");
        aa.add("7天");
        aa.add("地铁站");
        aa.add("公交站");
        aa.add("公园");
        aa.add("广场");
        aa.add("银行");
        aa.add("快递");
        aa.add("诊所");
        aa.add("理发店");
        aa.add("学校");
        aa.add("宠物");
        aa.add("邮局");
        aa.add("中通");
        aa.add("圆通");
        aa.add("申通");
        aa.add("顺丰");
        aa.add("韵达");
        aa.add("民生银行");
        aa.add("交通银行");
        aa.add("招商银行");
        aa.add("中信银行");
        aa.add("浦发银行");
        aa.add("平安银行");
        aa.add("华夏银行");
        aa.add("兴业银行");
        aa.add("图书馆");
        aa.add("步行街");
        aa.add("便利店");
        aa.add("五金店");
        aa.add("万达广场");
        aa.add("书店");
        aa.add("花店");
        aa.add("沃尔玛");
        aa.add("家乐福");
        aa.add("大润发");
        aa.add("物美");
        aa.add("华润万家");
        aa.add("眼镜店");
        aa.add("海澜之家");
        aa.add("文具店");
        aa.add("家具城");
        aa.add("菜市场");
        aa.add("水果批发市场");
        aa.add("批发市场");
        aa.add("邮政储蓄");
        aa.add("服装城");
        aa.add("健身房");
        aa.add("派出所");
        aa.add("中国银行");
        aa.add("旅行社");
        aa.add("工商银行");
        aa.add("农业银行");
        aa.add("建设银行");
        aa.add("动物园");
        aa.add("植物园");
        aa.add("体育馆");
        aa.add("修车");
        aa.add("中石化");
        aa.add("驾校");
        aa.add("加气站");
        aa.add("服务区");
        aa.add("鲁菜");
        aa.add("川菜");
        aa.add("粤菜");
        aa.add("淮扬菜");
        aa.add("闽菜");
        aa.add("浙江菜");
        aa.add("湘菜");
        aa.add("徽菜");
        aa.add("京菜");
        aa.add("鄂菜");
        aa.add("西北菜");
        aa.add("海鲜");
        aa.add("东北菜");
        aa.add("素食");
        aa.add("烤肉");
        aa.add("西餐");
        aa.add("茶艺馆");
        aa.add("全聚德");
        aa.add("创意菜");
        aa.add("新疆菜");
//        aa.add("");
//        aa.add("");
//        aa.add("");
//        aa.add("");
//        aa.add("");
//        aa.add("");
//        aa.add("");
//        aa.add("");
//        aa.add("");
//        aa.add("");
//        aa.add("");
//        aa.add("");
//        aa.add("");
//        aa.add("");
//        aa.add("");

    }
    private void initView() {
        mapView=findViewById(R.id.mapview);
        etSearch = findViewById(R.id.et_search);
        imgSousuo = findViewById(R.id.img_sousuo);
        bottomSheet = findViewById(R.id.bottom_sheet);
        llBottom = findViewById(R.id.ll_bottom);
        llBottom2=findViewById(R.id.ll_bottom2);



        tvMyaddress = findViewById(R.id.tv_myaddress);
        tvMyaddress2 = findViewById(R.id.tv_myaddress2);
        tvChakan = findViewById(R.id.tv_chakan);
        tvChakan2 = findViewById(R.id.tv_chakan2);
        listview = findViewById(R.id.listview);
        tvWeather = findViewById(R.id.tv_weather);
        tvJisuanqi = findViewById(R.id.tv_jisuanqi);


        //上面的菜单按钮
        img_menu=findViewById(R.id.img_menu);


        llBottom.setOnClickListener(this);//
        llBottom2.setOnClickListener(this);//查看更多
        img_menu.setOnClickListener(this);//左上角的菜单按钮
        imgSousuo.setOnClickListener(this);//搜索

        tvWeather.setOnClickListener(this);//天气
        tvJisuanqi.setOnClickListener(this);//计算器


        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        //设置菜单内容之外其他区域的背景色
        drawerLayout.setScrimColor(Color.TRANSPARENT);

        //左边菜单
        main_left_drawer_layout = findViewById(R.id.main_left_drawer_layout);
        //左边菜单
        initEvent();

       mapInit();



        //底部抽屉栏展示地址
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, @BottomSheetBehavior.State int newState) {
                String state = "null";
                switch (newState) {
                    case 1:
                        state = "STATE_DRAGGING";//过渡状态此时用户正在向上或者向下拖动bottom sheet
                        break;
                    case 2:
                        state = "STATE_SETTLING"; // 视图从脱离手指自由滑动到最终停下的这一小段时间
                        break;
                    case 3:
                        state = "STATE_EXPANDED"; //处于完全展开的状态

                        break;
                    case 4:
                        state = "STATE_COLLAPSED"; //默认的折叠状态
                        bottomSheet.setVisibility(View.VISIBLE);
                        llBottom.setVisibility(View.VISIBLE);
                        llBottom2.setVisibility(View.GONE);
                        tvChakan2.setVisibility(View.GONE);

                        break;
                    case 5:
                        state = "STATE_HIDDEN"; //下滑动完全隐藏 bottom sheet
//                        behavior.setPeekHeight(UIUtils.dip2px(100));
//                        T.showToastSafeError("到了最下面了");

                        if (poiItems!=null){
                            llBottom.setVisibility(View.GONE);
                            llBottom2.setEnabled(true);
                            llBottom2.setVisibility(View.VISIBLE);
                            tvChakan2.setVisibility(View.VISIBLE);
                        }else {
                            llBottom2.setEnabled(false);
                            tvChakan2.setVisibility(View.GONE);
                        }


                        break;

                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });



    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", myCity);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        //点附近10000米内的搜索结果
        for (int i = 0; i <aa.size() ; i++) {
            if (keyWord.equals(aa.get(i))){
                if (mylatlngpoint != null) {
                    searchBound = new PoiSearch.SearchBound(mylatlngpoint, 10000);
                    poiSearch.setBound(searchBound);
                    break;
                }
            }
        }
        poiSearch.searchPOIAsyn();
    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        //初始化定位
        init();
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        //解析AMapLocation对象
        //首先，可以判断AMapLocation对象不为空，当定位错误码类型为0时定位成功。
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

                //可在其中解析amapLocation获取相应内容。
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                String address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                String country = amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                amapLocation.getAoiName();//获取当前定位点的AOI信息
                amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                amapLocation.getFloor();//获取当前室内定位的楼层
                //amapLocation.getGpsStatus();//获取GPS的当前状态
                amapLocation.getGpsAccuracyStatus();
                //获取定位时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                String format = df.format(date);

                mylat = amapLocation.getLatitude();//自己的纬度
                mylng = amapLocation.getLongitude();//自己的经度

                 mylatlng=new LatLng(mylat,mylng);
                 mylatlngpoint=new LatLonPoint(mylat, mylng);

                 centerWHpoint=mylatlng;

                aMap.animateCamera(CameraUpdateFactory.changeLatLng(mylatlng));

                myCity=amapLocation.getCity();//当前所在城市

                //显示自己的地址信息
                tvMyaddress.setText(amapLocation.getProvince()+amapLocation.getCity()+amapLocation.getDistrict()+amapLocation.getStreet()+amapLocation.getStreetNum());
                tvMyaddress2.setText(amapLocation.getProvince()+amapLocation.getCity()+amapLocation.getDistrict()+amapLocation.getStreet()+amapLocation.getStreetNum());

                Log.e("qqqq", "地址:" + address + "----国家信息:" + country + "----time--> " + format);

            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("qqqq", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    private void init() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //--------------------------------------------------
        //选择定位模式
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置单次定位
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //--------------------------------------------------
        //自定义连续定位
        //SDK默认采用连续定位模式，时间间隔2000ms。如果您需要自定义调用间隔：
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //--------------------------------------------------
        //其他参数
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟软件Mock位置结果，多为模拟GPS定位结果，默认为true，允许模拟位置。
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        //设置定位请求超时时间，默认为30秒。
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(8000);
        //设置是否开启定位缓存机制
        //缓存机制默认开启，可以通过以下接口进行关闭。
        //当开启定位缓存功能，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存，不区分单次定位还是连续定位。GPS定位结果不会被缓存。
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(true);
        //--------------------------------------------------
        //启动定位
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    //动态申请权限
    private void requestPermissions() {

        List<String> permissionList = new ArrayList<>();
//        允许程序打开网络套接字
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.INTERNET);
        }
//        允许程序设置内置sd卡的写权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
//        允许程序获取网络状态
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
//                允许程序访问WiFi网络信息
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
//        允许程序读写手机状态和身份
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.READ_PHONE_STATE);
//        }
//                允许程序访问CellID或WiFi热点来获取粗略的位置
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        //申请打电话权限
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.CALL_PHONE);
//        }

        if (!permissionList.isEmpty()) {  //申请的集合不为空时，表示有需要申请的权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionList.toArray(new String[permissionList.size()]), 1);
            }
        } else { //所有的权限都已经授权过了

        }
    }

    //动态申请权限部分
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) { //安全写法，如果小于0，肯定会出错了
                    for (int i = 0; i < grantResults.length; i++) {

                        int grantResult = grantResults[i];
                        if (grantResult == PackageManager.PERMISSION_DENIED) { //这个是权限拒绝
                            String s = permissions[i];
//                            Toast.makeText(this, s + "权限被拒绝了", Toast.LENGTH_SHORT).show();
//                            T.showToastSafeError("暂未获取到定位信息");


                            //判断用户是否拒绝了定位权限
                            PackageManager pm = getPackageManager();
                            boolean flag = (PackageManager.PERMISSION_GRANTED ==
                                    pm.checkPermission("android.permission.ACCESS_COARSE_LOCATION", "packageName"));

                            if (flag) {
                                //有这个权限，做相应处理
                            }else { //没有权限



                            }


                        } else { //授权成功了
                            //do Something
                            mapInit();

                        }
                    }
                }
                break;
            default:
                break;
        }
    }
    //地图初始化
    private void mapInit() {
        aMap = mapView.getMap();

//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(centerWHpoint));//默认先定位武汉市经纬度
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerWHpoint, 14));//默认先定位武汉市经纬度
        if (aMap != null) {
            //设置显示定位按钮 并且可以点击
            UiSettings settings = aMap.getUiSettings();
            aMap.setLocationSource(this);//设置了定位的监听,这里要实现LocationSource接口
            // 是否显示定位按钮
            settings.setMyLocationButtonEnabled(false);



            // 设置定位监听
            aMap.setLocationSource(this);
            // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
        }

        aMap.setOnMarkerClickListener(this);//设置Marker的监听


        //蓝点初始化
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.mylocation));// 设置小蓝点的图标

        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
        myLocationStyle.showMyLocation(true);


        //隐藏定位范围的圈圈
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style


        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setZoomControlsEnabled(false);//隐藏放大缩小按钮


    }

    //设置开关监听
    private void initEvent() {
        drawerbar = new ActionBarDrawerToggle(this, drawerLayout, R.mipmap.ic_launcher, R.string.open, R.string.close) {
            //菜单打开
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            // 菜单关闭
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerbar);
    }

    //左边菜单开关事件
    public void openLeftLayout(View view) {
        if (drawerLayout.isDrawerOpen(main_left_drawer_layout)) {
            drawerLayout.closeDrawer(main_left_drawer_layout);
        } else {
            drawerLayout.openDrawer(main_left_drawer_layout);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_menu://左边的菜单按钮
                openLeftLayout(img_menu);
                break;
            case R.id.img_sousuo://搜索按钮
                if (!(poiItems==null||poiItems.equals(""))){
                    poiItems.clear();//先清除上次的
                    aMap.clear();
                    mapInit();
                }


                keyWord=etSearch.getText().toString();
                if (keyWord==null||keyWord.equals("")){
                    Toast.makeText(MainActivity.this,"搜索内容不能为空",Toast.LENGTH_LONG).show();

                    llBottom2.setVisibility(View.VISIBLE);
                    tvChakan2.setVisibility(View.GONE);
                }else {
                    doSearchQuery();//进行搜索
                }
                break;
            case R.id.ll_bottom2://我的位置 底部弹窗按钮
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);//默认的折叠状态 中间
                llBottom.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (!marker.getPosition().equals(mylatlng) ) { //点击的marker不是自己位置的那个marker
            if (oldMarker != null) {
                oldMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.maplittleother));
                oldMarker.hideInfoWindow();
            }
            oldMarker = marker;

            marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.mapbigother));
        } else if (marker.getPosition().equals(mylatlng)) {//如果是自己的Marker
            oldMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.maplittleother));
            oldMarker.hideInfoWindow();

        }
        return false;//false 点击marker marker会移动到地图中心，true则不会
    }


    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                     poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
//                        aMap.clear();// 清理之前的图标

                        adapters=new SearchAdapters(this,poiItems,mylatlng);
                        listview.setAdapter(adapters);



//                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
//                        poiOverlay.removeFromMap();
//                        poiOverlay.addToMap();
//                        poiOverlay.zoomToSpan();


                        //添加集合里的10条数据到地图上标记
                        newbounds2 = new LatLngBounds.Builder();

                        for (int j = 0; j < poiItems.size(); j++) {
                            LatLng latLng = new LatLng(poiItems.get(j).getLatLonPoint().getLatitude(), poiItems.get(j).getLatLonPoint().getLongitude());

                            //在地图上绘制搜索出来的点
                            drawMarkers(latLng, poiItems.get(j).getTitle());

                            newbounds2.include(latLng);

                        }

                        //根据搜索返回的距离，设置不同地图比例
//                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(myLatlng));
//                        aMap.moveCamera(CameraUpdateFactory.zoomTo(distanceUtil.scaleZoom(max)));
                        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds2.build(),200));//第二个参数为四周留空宽度


                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        Toast.makeText(MainActivity.this,"对不起，没有搜索到相关数据！",Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(MainActivity.this,"对不起，没有搜索到相关数据！",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this,"错误码"+rCode,Toast.LENGTH_SHORT).show();
        }
    }

    //绘制搜索出来的点
    public void drawMarkers(LatLng latLng, String title) {

        Marker  marker = aMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.maplittleother))
                        .draggable(true));

    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }

        Toast.makeText(MainActivity.this,infomation,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}