package com.github.quiram.buildhotspots.visualisation.layouts;

import com.github.quiram.buildhotspots.visualisation.BuildConfigurationGroup;
import javafx.scene.Node;

import java.util.Map;

public class OriginalLayout extends LayoutBase {

    public static final String ORIGINAL_LAYOUT = "Original";

    public OriginalLayout() {
        super(ORIGINAL_LAYOUT);
    }

	@Override
    public void executeLayout(Map<String, BuildConfigurationGroup> p_buildConfigurationGroups) throws Exception {
        double initialposX = 160;
        double initialposY = 160;
        double initialpos_setupWidth = 180;
        double initialpos_setupHeight = 180;
		
        //Third pass - reposition builds according to their dependency depth
        final int MAX_DEPTH = 100;
        int[] depthCounter = new int[MAX_DEPTH];

        p_buildConfigurationGroups.values().stream().filter(Node::isVisible).forEach(b -> {
            final int depth = b.getDependencyDepth();
            double x = initialposX + depth * initialpos_setupWidth;
            double y = initialposY + depthCounter[depth] * initialpos_setupHeight;

            // purposely missalign columns for better visibility
            if (depth % 2 == 1) {
                y += initialpos_setupHeight / 2;
            }

            b.setPosition(x, y);
            b.Draw();
            depthCounter[depth]++;
        });
	}
}
