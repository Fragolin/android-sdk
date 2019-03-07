package com.tranzzo.android.sdk;

public class Telemetry {

public String platform;
public String sdkVersion;
public String osVersion;
public String osBuildVersion;
public String osBuildNumber;
public String deviceId;
public String deviceIp;
public String deviceManufacturer;
public String deviceBrand;
public String deviceModel;
public String deviceTags;
public String deviceScreenRes;
public String deviceLocale;
public String deviceTimeZone;
public String appName;
public String appPackage;
    
    public Telemetry(String platform, String sdkVersion, String osVersion, String osBuildVersion, String osBuildNumber, String deviceId, String deviceIp, String deviceManufacturer, String deviceBrand, String deviceModel, String deviceTags, String deviceScreenRes, String deviceLocale, String deviceTimeZone, String appName, String appPackage) {
        this.platform = platform;
        this.sdkVersion = sdkVersion;
        this.osVersion = osVersion;
        this.osBuildVersion = osBuildVersion;
        this.osBuildNumber = osBuildNumber;
        this.deviceId = deviceId;
        this.deviceIp = deviceIp;
        this.deviceManufacturer = deviceManufacturer;
        this.deviceBrand = deviceBrand;
        this.deviceModel = deviceModel;
        this.deviceTags = deviceTags;
        this.deviceScreenRes = deviceScreenRes;
        this.deviceLocale = deviceLocale;
        this.deviceTimeZone = deviceTimeZone;
        this.appName = appName;
        this.appPackage = appPackage;
    }
}
