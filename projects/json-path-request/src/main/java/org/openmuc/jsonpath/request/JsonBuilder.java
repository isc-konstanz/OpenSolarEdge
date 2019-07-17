/* 
 * Copyright 2016-19 ISC Konstanz
 * 
 * This file is part of OpenSolarEdge.
 * For more information visit https://github.com/isc-konstanz/OpenSolarEdge
 * 
 * OpenSolarEdge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OpenSolarEdge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with OpenSolarEdge.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openmuc.jsonpath.request;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonBuilder {
	private final Gson gson;
	private final JsonObject jsonObject;

	public JsonBuilder() {
		gson = new Gson();
		jsonObject = new JsonObject();
	}

	public void addBoolean(String propertyName, boolean value) {
		jsonObject.addProperty(propertyName, value);
	}

	public void addInteger(String propertyName, int value) {
		jsonObject.addProperty(propertyName, value);
	}

	public void addDouble(String propertyName, double value) {
		jsonObject.addProperty(propertyName, value);
	}

	public void addString(String propertyName, String value) {
		jsonObject.addProperty(propertyName, value);
	}

	public void addStringList(String propertyName, List<String> stringList) {
		jsonObject.add(propertyName, gson.toJsonTree(stringList).getAsJsonArray());
	}

	public JsonObject getJsonObject() {
		return jsonObject;
	}

	@Override
	public String toString() {
		return jsonObject.toString();
	}

}
