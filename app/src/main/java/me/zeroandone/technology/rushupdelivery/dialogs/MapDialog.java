package me.zeroandone.technology.rushupdelivery.dialogs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import me.zeroandone.technology.rushupdelivery.InsideApp;
import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.utils.DrawPolylineVolley;
import me.zeroandone.technology.rushupdelivery.utils.Utils;

public class MapDialog extends DialogFragment implements OnMapReadyCallback,View.OnClickListener {

    GoogleMap _map;
    private SupportMapFragment mapFragment;
    ImageView imageView;
    DeliveryRequest deliveryRequest;
    DrawPolylineVolley drawPolyline;
    Marker PickupMarker, DropOffMarker;

    public MapDialog() {
    }

    public static MapDialog newInstance(String title) {
        MapDialog frag = new MapDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.setCancelable(false);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_dialog, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         mapFragment=((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_dialog_map));
         imageView=(ImageView) view.findViewById(R.id.close);
         drawPolyline=new DrawPolylineVolley(getActivity());
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        imageView.setOnClickListener(this);
    }

    public void setDelivery(DeliveryRequest delivery){
        this.deliveryRequest=delivery;
    }

    public void PlotPins(){
        if(_map==null){
            Log.d("HeroJongi"," map is null");
        }
            if (_map!=null && deliveryRequest != null && deliveryRequest.getPickupLocation()!=null && deliveryRequest.getDropoffLocation()!=null && deliveryRequest.getDropoffLocation().getLatitude() != null && deliveryRequest.getDropoffLocation().getLongitude() != null && deliveryRequest.getPickupLocation().getLatitude()
                    != null && deliveryRequest.getPickupLocation().getLongitude() != null) {
                LatLng pickup = new LatLng(Double.parseDouble(deliveryRequest.getPickupLocation().getLatitude()), Double.parseDouble(deliveryRequest.getPickupLocation().getLongitude()));
                LatLng dropoff = new LatLng(Double.parseDouble(deliveryRequest.getDropoffLocation().getLatitude()), Double.parseDouble(deliveryRequest.getDropoffLocation().getLongitude()));
                DropPickUpPin(pickup);
                DropDroOffPin(dropoff);
                ZoomCameraToBothPins();
                APICallToDrawPolyline();
            }

    }

    public String getDirectionUrl() {
        double pickupLatitude = PickupMarker.getPosition().latitude;
        double pickupLongitude = PickupMarker.getPosition().longitude;
        double dropOffLatitude = DropOffMarker.getPosition().latitude;
        double dropOffLongitude = DropOffMarker.getPosition().longitude;
        return Utils.getUrl(getActivity(), String.valueOf(pickupLatitude), String.valueOf(pickupLongitude), String.valueOf(dropOffLatitude), String.valueOf(dropOffLongitude));
    }


    public void APICallToDrawPolyline(){
        if(_map!=null && DropOffMarker!=null && PickupMarker!=null) {
            String directionAPI = getDirectionUrl();
            drawPolyline.getDirectionFromDirectionApiServer(directionAPI, _map);
        }
    }

    public void DropPickUpPin(LatLng location){
        if(location!=null && location.latitude!=0 && location.longitude!=0 && _map!=null) {
            MarkerOptions markerOpts = new MarkerOptions().position(location);
            markerOpts.icon(BitmapDescriptorFactory.fromResource(R.mipmap.greenpin));
            PickupMarker = _map.addMarker(markerOpts);
        }
    }

    public void DropDroOffPin(LatLng location){
        if(location!=null && location.latitude!=0 && location.longitude!=0 && _map!=null) {
            MarkerOptions markerOpts = new MarkerOptions().position(location);
            markerOpts.icon(BitmapDescriptorFactory.fromResource(R.mipmap.orangepin));
            DropOffMarker = _map.addMarker(markerOpts);
        }
    }

    private void ZoomCameraToBothPins() {
        if(_map!=null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            if(PickupMarker!=null) {
                builder.include(PickupMarker.getPosition());
            }
            if(DropOffMarker!=null) {
                builder.include(DropOffMarker.getPosition());
            }
            _map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this._map=googleMap;
        if(deliveryRequest!=null){
            Log.d("HeroJongi"," delivery request ");
            PlotPins();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("HeroJongi"," on stop");
        if(_map!=null){
            mapFragment.onDestroyView();
            getFragmentManager().beginTransaction()
                    .remove(getFragmentManager().findFragmentById(R.id.map_dialog_map))
                    .commit();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.close:
                if(_map!=null){
                    _map.clear();
            }
                getDialog().dismiss();
                break;
        }
    }
}
