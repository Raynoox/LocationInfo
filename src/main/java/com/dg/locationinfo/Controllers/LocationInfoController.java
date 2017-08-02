package com.dg.locationinfo.Controllers;

import com.dg.locationinfo.Models.LocationInfo;
import com.dg.locationinfo.Services.LocationProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class LocationInfoController {

    private final LocationProviderService locationService;
    @Autowired
    public LocationInfoController(LocationProviderService locationService) {
        this.locationService = locationService;
    }
    @RequestMapping(value = "{country}/{city}/{description}", method = RequestMethod.GET)
    @ResponseBody
    public List<List<LocationInfo>> getLocationInformation(@PathVariable String country, @PathVariable String city, @PathVariable String description) {
        return locationService.getLocationInformation(country, city, description);
    }
}
