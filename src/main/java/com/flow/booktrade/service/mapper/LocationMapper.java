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
		}
		return rl;
	}
}
