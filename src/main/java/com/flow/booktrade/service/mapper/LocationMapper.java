package com.flow.booktrade.service.mapper;

import com.flow.booktrade.domain.RLocation;
import com.flow.booktrade.dto.Location;

/**
 * Location mapping helper
 * @author Dayna
 *
 */
public class LocationMapper {
	
	/**
	 * Map to a location object
	 * @param rl
	 * @return
	 */
	public Location toLocation(RLocation rl){
		Location l = null;
		if (rl != null){
			l = new Location();
			l.setCity(rl.getCity());
			l.setCountry(rl.getCountry());
			l.setLatitude(rl.getLatitude());
			l.setLongitude(rl.getLongitude());
			l.setProvince(rl.getProvince());
			l.setAddress(rl.getAddress());
		}
		return l;
	}
	
	/**
	 * Map to an entity location object
	 * @param l
	 * @return
	 */
	public RLocation toRLocation(Location l){
		RLocation rl = null;
		if(l != null){
			rl = new RLocation();
			rl.setCity(l.getCity());
			rl.setCountry(l.getCountry());
			rl.setLatitude(l.getLatitude());
			rl.setLongitude(l.getLongitude());
			rl.setProvince(l.getProvince());
			rl.setAddress(l.getAddress());
		}
		return rl;
	}
}
