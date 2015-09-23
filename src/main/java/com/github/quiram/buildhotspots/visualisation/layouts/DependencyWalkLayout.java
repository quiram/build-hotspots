package com.github.quiram.buildhotspots.visualisation.layouts;

import java.util.Map;

import com.github.quiram.buildhotspots.visualisation.BuildConfiguration;

/*
 * this layout method starts with all build configurations which have 0 dependents
 * it then recursively draws each one and it's dependencies based on it's dependency depth
 */
public class DependencyWalkLayout extends LayoutBase{
	
	/*
	 * internal class to wrap build configuration
	 */
	private class BuildConfigWrapper {
		private BuildConfiguration m_bc = null;
		private boolean m_drawn = false;
		private int m_gridX = -1;
		private int m_gridY = -1;
		
		public BuildConfigWrapper(BuildConfiguration p_bc) {
			m_bc = p_bc;
			m_drawn = false;
			m_gridX = -1;
			m_gridY = -1;
		}
	}
	

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
