package com.jasper.myandroidtest._thirdparty.jpush;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * JPush详细信息
 */
public class JPushDetail implements Parcelable {
    private String actionType;
    private String extraMsgId;
    private String extraTitle;
    private String extraMessage;
    private String extraContentType;
    private String extraExtra;

    public JPushDetail() {
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getExtraMsgId() {
        return extraMsgId;
    }

    public void setExtraMsgId(String extraMsgId) {
        this.extraMsgId = extraMsgId;
    }

    public String getExtraTitle() {
        return extraTitle;
    }

    public void setExtraTitle(String extraTitle) {
        this.extraTitle = extraTitle;
    }

    public String getExtraMessage() {
        return extraMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

    public String getExtraContentType() {
        return extraContentType;
    }

    public void setExtraContentType(String extraContentType) {
        this.extraContentType = extraContentType;
    }

    public String getExtraExtra() {
        return extraExtra;
    }

    public void setExtraExtra(String extraExtra) {
        this.extraExtra = extraExtra;
    }

    @Override
    public String toString() {
        return "JPushDetail{" +
                "actionType='" + actionType + '\'' +
                ", extraMsgId='" + extraMsgId + '\'' +
                ", extraTitle='" + extraTitle + '\'' +
                ", extraMessage='" + extraMessage + '\'' +
                ", extraContentType='" + extraContentType + '\'' +
                ", extraExtra='" + extraExtra + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.actionType);
        dest.writeString(this.extraMsgId);
        dest.writeString(this.extraTitle);
        dest.writeString(this.extraMessage);
        dest.writeString(this.extraContentType);
        dest.writeString(this.extraExtra);
    }

    protected JPushDetail(Parcel in) {
        this.actionType = in.readString();
        this.extraMsgId = in.readString();
        this.extraTitle = in.readString();
        this.extraMessage = in.readString();

        this.extraContentType = in.readString();
        this.extraExtra = in.readString();
    }

    public static final Creator<JPushDetail> CREATOR = new Creator<JPushDetail>() {
        public JPushDetail createFromParcel(Parcel source) {
            return new JPushDetail(source);
        }

        public JPushDetail[] newArray(int size) {
            return new JPushDetail[size];
        }
    };
}