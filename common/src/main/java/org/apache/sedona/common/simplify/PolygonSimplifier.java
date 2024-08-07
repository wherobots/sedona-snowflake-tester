/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sedona.common.simplify;

import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

public class PolygonSimplifier {
  public static Geometry simplify(Polygon geom, boolean preserveCollapsed, double epsilon) {
    LinearRing exteriorRing = geom.getExteriorRing();
    int minPointsExternal = preserveCollapsed ? 4 : 0;

    GeometryFactory geometryFactory = geom.getFactory();
    LinearRing simplifiedExterior =
        geometryFactory.createLinearRing(
            CoordinatesSimplifier.simplifyInPlace(
                exteriorRing.getCoordinates(), epsilon, minPointsExternal));

    if (simplifiedExterior.getNumPoints() < 4) {
      return simplifiedExterior;
    } else {
      List<LinearRing> interiorRings = new ArrayList<>();
      for (int i = 0; i < geom.getNumInteriorRing(); i++) {
        LinearRing interiorRing = geom.getInteriorRingN(i);
        Coordinate[] simplifiedInterior =
            CoordinatesSimplifier.simplifyInPlace(
                interiorRing.getCoordinates(), epsilon, minPointsExternal);
        if (simplifiedInterior.length >= 4) {
          interiorRings.add(geometryFactory.createLinearRing(simplifiedInterior));
        }
      }
      return geometryFactory.createPolygon(
          simplifiedExterior, interiorRings.toArray(new LinearRing[0]));
    }
  }
}
