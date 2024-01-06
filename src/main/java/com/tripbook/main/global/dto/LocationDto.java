package com.tripbook.main.global.dto;

import com.tripbook.main.global.entity.Location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {
	@Getter
	@NoArgsConstructor
	public static class LocationSimpleDto {

		private String name;
		private String locationX;
		private String locationY;

		public LocationSimpleDto(Location location) {
			this.name = location.getName();
			this.locationX = location.getX();
			this.locationY = location.getY();
		}
	}
}
