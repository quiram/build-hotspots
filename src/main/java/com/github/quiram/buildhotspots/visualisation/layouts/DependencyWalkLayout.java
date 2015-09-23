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
		
		public void finalDraw(
				int p_xOffset, 
				int p_yOffset, 
				int p_xSpacing, 
				int p_ySpacing
		) throws Exception {
			if (!m_drawn) throw new Exception("ERROR Not yet drawn");
			m_bc.setPosition(p_xOffset + (m_gridX * p_xSpacing), p_yOffset + (m_gridY * p_ySpacing));
		}
	}
	

	public DependencyWalkLayout() {
		super("Dependency Walk");
	}

	//Draw the Build Configurations
	//Return the number of rows used in the output
	private int drawWithDependencies(BuildConfigWrapper p_bcw, int p_curRow) throws Exception {
		if (p_bcw.isDrawn()) return 0;
		int rows_output = 0;
		//Draw current
		p_bcw.draw(p_bcw.getBuildConfiguration().getDependentDepth(), p_curRow);
		
		for (Dependency d : p_bcw.getBuildConfiguration().getDependencies()) {
			if (d.getOrigin()==p_bcw.getBuildConfiguration()) throw new Exception("Logical Error");
			rows_output += drawWithDependencies(bcList.get(d.getOrigin().getXMLType().getName()),(p_curRow + rows_output) );
		}
		
		if (rows_output==0) rows_output=1;
		
		return rows_output;		
	}
	
	private Map<String,BuildConfigWrapper> bcList = null; //list containing everything we want to graph 
	
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

		//Build list of all top level - as we will walk down dependencies we want where depandents = 0
		List<String> topLevel = new ArrayList<String>();
		for (BuildConfigWrapper bcw : bcList.values()) {
			if (bcw.getBuildConfiguration().getDependentDepth()==0) topLevel.add(bcw.getBuildConfiguration().getXMLType().getName());
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

		
		int cur_row = 1;
		//show result
		for (String cur : topLevel) {
			BuildConfigWrapper bcw = bcList.get(cur);
			cur_row += drawWithDependencies(bcw, cur_row);
			//System.out.print(bcw.getGraphID());
			//System.out.print(" - ");
			//System.out.print(cur);
			//System.out.println("");
		}
		
		//Map all virtual grid co-ords to actual locations
		for (BuildConfigWrapper bcw : bcList.values()) {
			bcw.finalDraw(
				65, 
				65, 
				260, 
				120
			);
		}
		
		
		
		
	}
}