package com.github.quiram.buildhotspots.visualisation.layouts;

import java.util.Map;

import com.github.quiram.buildhotspots.visualisation.BuildConfiguration;

/*
 * Base class for all different layout types
 */
public class LayoutBase {
	private String m_name = "";
	
	public LayoutBase(String p_name) {
		m_name = p_name;
	}
	public String getName() {return m_name;}
	
	public void executeLayout(Map<String, BuildConfiguration> p_buildConfigurations) throws Exception{
		throw new Exception("Should be overridden");
	}
}
