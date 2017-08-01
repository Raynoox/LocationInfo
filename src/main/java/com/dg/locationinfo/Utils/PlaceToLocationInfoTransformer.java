package com.dg.locationinfo.Utils;

import com.dg.locationinfo.Models.LocationInfo;
import com.restfb.types.Place;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlaceToLocationInfoTransformer implements ObjectTransformer<Place, LocationInfo> {

    @Override
    public List<LocationInfo> transform(List<Place> places) {
        return places.stream().map(transformFunction).collect(Collectors.toList());
    }

    private final Function<Place, LocationInfo> transformFunction =
            LocationInfo::new;
}
