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

import static eu.jasha.demo.sbtfragments.HomeController.VIEW_INDEX;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class HomeControllerTest {

  private HomeController controller;

  @Before
  public void setup() {
    controller = new HomeController();
  }

  @Test
  public void should_show_index() {
    String home = controller.getHome();

    assertThat(home).isEqualTo(VIEW_INDEX);
  }
}