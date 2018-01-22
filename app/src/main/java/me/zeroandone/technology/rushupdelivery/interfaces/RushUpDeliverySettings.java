package me.zeroandone.technology.rushupdelivery.interfaces;


import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.objects.PushType;

public interface RushUpDeliverySettings {
    public void onNotificationRecieved(PushType pushType,Object object);
    public void SaveActiveDelivery(DeliveryRequest deliveryRequest);
    public void PlotPins(DeliveryRequest deliveryRequest);
    public void showOptions();
    public void showBottomMenu(DeliveryRequest deliveryRequest);
    public void CheckPickUpCode(boolean isWrong);
    public void CheckDropoffCode(boolean isWrong);
    public void FillUpBottomMenu(DeliveryRequest deliveryRequest,boolean isPickUp);

}
