/*
 *   Copyright 2018 Jasha Joachimsthal
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package eu.jasha.demo.sbtfragments;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class City implements Serializable {

  private static final long serialVersionUID = -7041939576079911914L;
  private String id;
  private String name;
  private int foundedIn;
  private int population;

  public City() {
  }

  public City(String id, String name, int foundedIn, int population) {
    this();
    this.id = id;
    this.name = name;
    this.foundedIn = foundedIn;
    this.population = population;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getFoundedIn() {
    return foundedIn;
  }

  public void setFoundedIn(int foundedIn) {
    this.foundedIn = foundedIn;
  }

  public int getPopulation() {
    return population;
  }

  public void setPopulation(int population) {
    this.population = population;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }
    City rhs = (City) obj;
    return new EqualsBuilder()
        .append(this.id, rhs.id)
        .append(this.name, rhs.name)
        .append(this.foundedIn, rhs.foundedIn)
        .append(this.population, rhs.population)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(id)
        .toHashCode();
  }
}