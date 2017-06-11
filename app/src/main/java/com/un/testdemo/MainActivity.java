package com.un.testdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AMap.OnMapClickListener, AMapLocationListener, RouteSearch.OnRouteSearchListener {


    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.0001;

    private MapView mMapView;
    private AMap aMap;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    /**
     * 当前定位Marker点
     */
    private Marker currentMarker;

    /**
     * 目的地Marker点
     */
    private Marker targetMarker;


    /**
     * 当前定位的经纬度
     */
    private LatLng currentlatLng;


    /**
     * 虚拟路线
     */
    Polyline mVirtureRoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        initMap();
        initLocation();

    }


    /**
     * 地图初始化
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        aMap.setOnMapClickListener(this);

        /**
         * 初始化定位小蓝点
         */
        /*MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style*/
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);
        //设置地图的默认缩放比例
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }

    /**
     * 初始化定位服务，打开定位
     */
    private void initLocation() {

        //初始化定位

        mLocationClient = new AMapLocationClient(getApplication());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象

        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);


//设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(false);
//单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

//启动定位
        mLocationClient.startLocation();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    /**
     * 地图点击监听
     *
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        /**
         * 如果存在目的地点则先移除
         */
        if (targetMarker != null) {
            targetMarker.remove();
        }
        String msg = "经度：" + String.valueOf(latLng.longitude) + "纬度：" + latLng.latitude;
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        MarkerOptions options = new MarkerOptions();
        targetMarker = aMap.addMarker(options.position(latLng).title("target").snippet("TargetLocation"));
        //搜寻驾车路线
        searchRouteLine(new LatLonPoint(latLng.latitude, latLng.longitude));

    }

    /**
     * 查询路线
     *
     * @param latLonPoint
     */
    private void searchRouteLine(LatLonPoint latLonPoint) {

        //初始化路线搜索
        RouteSearch routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        //设定起始位置
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(currentlatLng.latitude, currentlatLng.longitude), latLonPoint);
        //构造驾车路径查询参数
        RouteSearch.DriveRouteQuery driveRouteQuery = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.RIDING_DEFAULT, null, null, "");
        //发送请求
        routeSearch.calculateDriveRouteAsyn(driveRouteQuery);
    }


    /**
     * 定位监听回调
     *
     * @param aMapLocation
     */

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {


        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                /**
                 * 如果已经设置定位点，那么不再设置
                 */
                if (currentMarker != null) {
                    return;
                }

                //可在其中解析amapLocation获取相应内容。
                MarkerOptions options = new MarkerOptions();
                options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));

                currentlatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                currentMarker = aMap.addMarker(options.position(currentlatLng).title("current").snippet("CurrentLocation"));
                //把当前的定位位置设置为中心点
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(currentlatLng));

            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }


    /**
     * 公交路径结果回调
     *
     * @param busRouteResult
     * @param i
     */
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    /**
     * 驾车路径结果回调
     *
     * @param driveRouteResult
     * @param i
     */
    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

        /**
         * 途经点经纬度集合
         */
        List<LatLng> latLngs;
        if (i == 1000) {

            /**
             * 如果上次存在路线，则清除上一次的路线
             */
            if (mVirtureRoad != null) {
                mVirtureRoad.remove();
            }
            /**
             * 途经路段列表
             */
            latLngs = new ArrayList<>();
            /**
             组装途经点经纬度集合
             */
            List<DriveStep> driveStepList = driveRouteResult.getPaths().get(0).getSteps();
            for (int m = 0; m < driveStepList.size(); m++) {
                List<LatLonPoint> latLonPointList = driveStepList.get(m).getPolyline();
                for (int n = 0; n < latLonPointList.size(); n++) {
                    LatLonPoint latLonPoint = latLonPointList.get(n);
                    latLngs.add(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));

                }
            }
            /**
             * 将途经点在地图上用线绘制出来
             */

            mVirtureRoad = aMap.addPolyline(new PolylineOptions().
                    addAll(latLngs).width(10).color(Color.BLUE));

            /**
             * 移动轨迹
             */
            pointMove(latLngs);


        }
    }

    /**
     * 步行路径结果回调
     *
     * @param walkRouteResult
     * @param i
     */
    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }


    /**
     * 骑行路径结果回调
     *
     * @param rideRouteResult
     * @param i
     */
    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    /**
     * 根据点获取图标转的角度
     */

    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= mVirtureRoad.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mVirtureRoad.getPoints().get(startIndex);
        LatLng endPoint = mVirtureRoad.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }

    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }


    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {

        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 算取斜率
     */
    private double getSlope(int startIndex) {
        if ((startIndex + 1) >= mVirtureRoad.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mVirtureRoad.getPoints().get(startIndex);
        LatLng endPoint = mVirtureRoad.getPoints().get(startIndex + 1);
        return getSlope(startPoint, endPoint);
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }

    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }


    /**
     * Marker点模拟移动
     *
     * @param latLngs
     */
    private void pointMove(List<LatLng> latLngs) {

        for (int i = 0; i < latLngs.size() - 1; i++) {
            LatLng startPoint = mVirtureRoad.getPoints().get(i);
            LatLng endPoint = mVirtureRoad.getPoints().get(i + 1);
            currentMarker
                    .setPosition(startPoint);

            currentMarker.setRotateAngle((float) getAngle(startPoint,
                    endPoint));

            double slope = getSlope(startPoint, endPoint);
            //是不是正向的标示（向上设为正向）
            boolean isReverse = (startPoint.latitude > endPoint.latitude);

            double intercept = getInterception(slope, startPoint);

            double xMoveDistance = isReverse ? getXMoveDistance(slope)
                    : -1 * getXMoveDistance(slope);


            for (double j = startPoint.latitude; !((j > endPoint.latitude)^ isReverse); j = j - xMoveDistance) {
                LatLng latLng = null;
                if (slope != Double.MAX_VALUE) {
                    latLng = new LatLng(j, (j - intercept) / slope);
                } else {
                    latLng = new LatLng(j, startPoint.longitude);
                }
                currentMarker.setPosition(latLng);
                try {
                    Thread.sleep(TIME_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}




