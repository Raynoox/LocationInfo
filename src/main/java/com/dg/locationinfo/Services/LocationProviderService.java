package com.dg.locationinfo.Services;

import com.dg.locationinfo.Models.LocationInfo;

import java.util.List;

public interface LocationProviderService {
    List<List<LocationInfo>> getLocationInformation(String country, String city, String description);
}
