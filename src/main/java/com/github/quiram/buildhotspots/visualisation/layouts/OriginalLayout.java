package com.github.quiram.buildhotspots.visualisation.layouts;

import java.util.Map;

import com.github.quiram.buildhotspots.visualisation.BuildConfiguration;

public class OriginalLayout extends LayoutBase {

	public OriginalLayout() {
		super("Original");
	}

	@Override
	public void executeLayout(Map<String, BuildConfiguration> p_buildConfigurations) throws Exception {
        double initialposX = 60;
        double initialposY = 60;
        double initialpos_setupWidth = 100;
        double initialpos_setupHeight = 100;
		
        //Third pass - reposition builds according to their dependency depth
        final int MAX_DEPTH = 100;
        int[] depthCounter = new int[MAX_DEPTH];

        p_buildConfigurations.values().forEach(b -> {
            final int depth = b.getDepth();
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
