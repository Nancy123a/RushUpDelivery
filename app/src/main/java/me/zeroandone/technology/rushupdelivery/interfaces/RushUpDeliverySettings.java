package me.zeroandone.technology.rushupdelivery.interfaces;


import android.app.Dialog;

import java.io.File;

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
    public void displayPicture(File file);
    public  void openVerifyPhoneEmail(Dialog dialog,String attribute);
    public void UpdateAttribute(String attribute_name, String attribute_value, Dialog dialog);
    public void Verify_Email_Password(String attribute, String code, Dialog dialog);
    public void ChangePassword(String newpassword, String oldPassword, Dialog dialog);
    public void Signout();
    public void setRatingofDriver();
    public void onBalanceHistoryRowClicked(DeliveryRequest deliveryRequest,boolean isHistory);
}
