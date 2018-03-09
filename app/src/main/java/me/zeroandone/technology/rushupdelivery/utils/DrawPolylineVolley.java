package me.zeroandone.technology.rushupdelivery.utils;


import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.objects.DirectionObject;
import me.zeroandone.technology.rushupdelivery.objects.LegsObject;
import me.zeroandone.technology.rushupdelivery.objects.PolylineObject;
import me.zeroandone.technology.rushupdelivery.objects.RouteObject;
import me.zeroandone.technology.rushupdelivery.objects.StepsObject;


public class DrawPolylineVolley {
    GoogleMap map;
    Context context;
    Polyline polyline;

public DrawPolylineVolley(Context context) {
        this.context=context;
    }

    public  void getDirectionFromDirectionApiServer(String url,GoogleMap map) {
        this.map=map;
        GsonRequest<DirectionObject> serverRequest = new GsonRequest<DirectionObject>(
                Request.Method.GET,
                url,
                DirectionObject.class,
                createRequestSuccessListener(),
                createRequestErrorListener());
        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Utils.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(serverRequest);
    }

    private Response.Listener<DirectionObject> createRequestSuccessListener() {
        return new Response.Listener<DirectionObject>() {
            @Override
            public void onResponse(DirectionObject response) {
                try {
                    if (response.getStatus().equals("OK")) {
                        List<LatLng> mDirections = getDirectionPolylines(response.getRoutes());
                        drawRouteOnMap(map, mDirections);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        };
    }

    private List<LatLng> getDirectionPolylines(List<RouteObject> routes) {
        List<LatLng> directionList = new ArrayList<LatLng>();
        for (RouteObject route : routes) {
            List<LegsObject> legs = route.getLegs();
            for (LegsObject leg : legs) {
                List<StepsObject> steps = leg.getSteps();
                for (StepsObject step : steps) {
                    PolylineObject polyline = step.getPolyline();
                    String points = polyline.getPoints();
                    List<LatLng> singlePolyline = decodePoly(points);
                    directionList.addAll(singlePolyline);
                }
            }
        }
        return directionList;
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }

    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions) {
        if (polyline != null) {
            polyline.remove();
        }
        PolylineOptions options = new PolylineOptions().width(6).color(context.getResources().getColor(R.color.dark_red)).geodesic(true);
        options.addAll(positions);
        polyline = map.addPolyline(options);
    }

    public void RemovePolyline(){
        if(polyline!=null){
            polyline.remove();
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}
