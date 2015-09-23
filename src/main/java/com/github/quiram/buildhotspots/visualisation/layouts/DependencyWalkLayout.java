package com.github.quiram.buildhotspots.visualisation.layouts;

import java.util.Map;

import com.github.quiram.buildhotspots.visualisation.BuildConfiguration;

public class DependencyWalkLayout extends LayoutBase{

	public DependencyWalkLayout() {
		super("Dependency Walk");
	}

	@Override
	public void executeLayout(Map<String, BuildConfiguration> p_buildConfigurations) throws Exception {
		
		
		//TODO code this. Just dummy position change for now
		for (BuildConfiguration bc : p_buildConfigurations.values()) {
			bc.setPosition(100, 100);
		}
		
	}
}
