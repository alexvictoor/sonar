/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2013 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.core.issue;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class FieldDiffsTest {

  FieldDiffs diffs = new FieldDiffs();

  @Test
  public void diffs_should_be_empty_by_default() throws Exception {
    assertThat(diffs.diffs()).isEmpty();
  }

  @Test
  public void test_diff() throws Exception {
    diffs.setDiff("severity", "BLOCKER", "INFO");
    diffs.setDiff("resolution", "OPEN", "FIXED");

    assertThat(diffs.diffs()).hasSize(2);

    FieldDiffs.Diff diff = diffs.diffs().get("severity");
    assertThat(diff.oldValue()).isEqualTo("BLOCKER");
    assertThat(diff.newValue()).isEqualTo("INFO");

    diff = diffs.diffs().get("resolution");
    assertThat(diff.oldValue()).isEqualTo("OPEN");
    assertThat(diff.newValue()).isEqualTo("FIXED");
  }

  @Test
  public void should_keep_old_value() throws Exception {
    diffs.setDiff("severity", "BLOCKER", "INFO");
    diffs.setDiff("severity", null, "MAJOR");
    FieldDiffs.Diff diff = diffs.diffs().get("severity");
    assertThat(diff.oldValue()).isEqualTo("BLOCKER");
    assertThat(diff.newValue()).isEqualTo("MAJOR");
  }

  @Test
  public void test_toString() throws Exception {
    diffs.setDiff("severity", "BLOCKER", "INFO");
    diffs.setDiff("resolution", "OPEN", "FIXED");

    assertThat(diffs.toString()).isEqualTo("severity=BLOCKER|INFO,resolution=OPEN|FIXED");
  }

  @Test
  public void test_toString_with_null_values() throws Exception {
    diffs.setDiff("severity", null, "INFO");
    diffs.setDiff("resolution", "OPEN", null);

    assertThat(diffs.toString()).isEqualTo("severity=|INFO,resolution=OPEN|");
  }

  @Test
  public void test_parse() throws Exception {
    diffs = FieldDiffs.parse("severity=BLOCKER|INFO,resolution=OPEN|FIXED");
    assertThat(diffs.diffs()).hasSize(2);

    FieldDiffs.Diff diff = diffs.diffs().get("severity");
    assertThat(diff.oldValue()).isEqualTo("BLOCKER");
    assertThat(diff.newValue()).isEqualTo("INFO");

    diff = diffs.diffs().get("resolution");
    assertThat(diff.oldValue()).isEqualTo("OPEN");
    assertThat(diff.newValue()).isEqualTo("FIXED");
  }

  @Test
  public void test_parse_empty_values() throws Exception {
    diffs = FieldDiffs.parse("severity=|INFO,resolution=OPEN|");
    assertThat(diffs.diffs()).hasSize(2);

    FieldDiffs.Diff diff = diffs.diffs().get("severity");
    assertThat(diff.oldValue()).isEqualTo("");
    assertThat(diff.newValue()).isEqualTo("INFO");

    diff = diffs.diffs().get("resolution");
    assertThat(diff.oldValue()).isEqualTo("OPEN");
    assertThat(diff.newValue()).isEqualTo("");
  }

  @Test
  public void test_parse_null() throws Exception {
    diffs = FieldDiffs.parse(null);
    assertThat(diffs.diffs()).isEmpty();
  }

  @Test
  public void test_parse_empty() throws Exception {
    diffs = FieldDiffs.parse("");
    assertThat(diffs.diffs()).isEmpty();
  }
}
