package com.github.quiram.buildhotspots.visualisation.layouts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.quiram.buildhotspots.drawingdata.BuildConfigurationType.Dependencies;
import com.github.quiram.buildhotspots.visualisation.BuildConfiguration;
import com.github.quiram.buildhotspots.visualisation.Dependency;

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
		private int m_graphID = -1; //There may be multiple disjointed graphs in our data. Separate these out and give them a unique ID
		
		public BuildConfigWrapper(BuildConfiguration p_bc) {
			m_bc = p_bc;
			m_drawn = false;
			m_gridX = -1;
			m_gridY = -1;
		}
		
		public void draw(int p_gridX, int p_gridY) {
			m_gridX = p_gridX;
			m_gridY = p_gridY;
			m_drawn = true;
		}
		
		public BuildConfiguration getBuildConfiguration() {
			return m_bc;
		}
		
		public boolean isDrawn() {
			return m_drawn;
		}
		
		public int getGraphID() {
			return m_graphID;
		}
		
		public void allocateGraphID(int p_graphID) throws Exception {
			//allocate a graph ID.
			//also visit all neighbours and do the same
			m_graphID = p_graphID;
			
			for (Dependency d : m_bc.getDependencies()) {
				BuildConfigWrapper othW = bcList.get(d.getOrigin().getXMLType().getName());
				if (othW==this) throw new Exception("Logical Error");
				if (othW==null) throw new Exception("Logical error");
				if(othW.getGraphID()== -1) othW.allocateGraphID(p_graphID);
			}
			for (Dependency d : m_bc.getDependents()) {
				BuildConfigWrapper othW = bcList.get(d.getTarget().getXMLType().getName());
				if (othW==this) throw new Exception("Logical Error");
				if (othW==null) throw new Exception("Logical error");
				if(othW.getGraphID()== -1) othW.allocateGraphID(p_graphID);
			}
			
		}
	}
	

	public DependencyWalkLayout() {
		super("Dependency Walk");
	}

	private void draw(BuildConfigWrapper p_bcw) throws Exception {
		//TODO
		p_bcw.draw(1, 1);
		
	}
	
	private Map<String,BuildConfigWrapper> bcList = null; //list contianing everything we want to graph 
	
	@Override
	public void executeLayout(Map<String, BuildConfiguration> p_buildConfigurations) throws Exception {
		
		//I need more data for the drawing process only so I am using a wrapper to keep it
		//e.g. I am keeping note on what has and hasn't already drawn
		//Also this step ignores the hidden BC's
		bcList = new HashMap<String,BuildConfigWrapper>();
		for (BuildConfiguration bc : p_buildConfigurations.values()) {
			if (bc.isVisible()) {
				bcList.put(bc.getXMLType().getName(),new BuildConfigWrapper(bc));
				//Set a dummy position for everything - we will update it later
				bc.setPosition(100, 100);
			}
		}
		
		//Detect separate disconnected graphs and populate graph ID
		int curGraphID = -1;
		BuildConfigWrapper bcWithNoGraph = null;
		while (true) {
			bcWithNoGraph = null;
			for (BuildConfigWrapper cur : bcList.values()) {
				if (cur.getGraphID()==-1) bcWithNoGraph = cur;
			}
			if (bcWithNoGraph==null) break;
			curGraphID++;
			bcWithNoGraph.allocateGraphID(curGraphID);
		};
			
		//Redundant check - make sure every BC has a graph ID now
		for (BuildConfigWrapper bcw : bcList.values()) {
			if (bcw.getGraphID()==-1) throw new Exception("Algroythm missed a build configuration");
		}

		//Build list of all top level dependencies
		List<String> topLevel = new ArrayList<String>();
		for (BuildConfigWrapper bcw : bcList.values()) {
			if (bcw.getBuildConfiguration().getDependencyDepth()==0) topLevel.add(bcw.getBuildConfiguration().getXMLType().getName());
		}
		
		//sort by graph so we draw each individual disjointed graph separately
		Comparator<String> GRAPH_ORDER = new Comparator<String>() {
		    public int compare(String str1, String str2) {
				BuildConfigWrapper bcw = bcList.get(str1);
				BuildConfigWrapper bcw2 = bcList.get(str2);
				return Integer.compare(bcw.getGraphID(), bcw2.getGraphID());
		    }
		};
		Collections.sort(topLevel, GRAPH_ORDER);		

		
		//show result
		for (String cur : topLevel) {
			BuildConfigWrapper bcw = bcList.get(cur);
			System.out.print(bcw.getGraphID());
			System.out.print(" - ");
			System.out.print(cur);
			System.out.println("");
		}
		
		
		
		
		//Redundant check - make sure all drawn
		//
		//for (BuildConfigWrapper bcw : bcList.values()) {
		//	if (!bcw.isDrawn()) throw new Exception("Build Configuration was not drawn");
		//}
		
		
	}
}
