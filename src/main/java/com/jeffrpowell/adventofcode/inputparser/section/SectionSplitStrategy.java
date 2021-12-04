package com.jeffrpowell.adventofcode.inputparser.section;

import java.util.List;

public interface SectionSplitStrategy {
    List<List<String>> splitSectionInputs(List<String> allInput);
}
