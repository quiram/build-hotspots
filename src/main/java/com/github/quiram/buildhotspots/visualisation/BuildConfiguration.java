package com.github.quiram.buildhotspots.visualisation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class BuildConfiguration {
    private List<Dependency> m_Dependencies = new ArrayList<>(); //link to BC's that are dependencies of this BC
    private List<Dependency> m_Dependents = new ArrayList<>(); //link to BC's that are dependant on this one

    public Dependency addDependency(BuildConfiguration p_bc) {
        final Dependency dependency = new Dependency(p_bc, this);
        m_Dependencies.add(dependency);
        p_bc.m_Dependents.add(dependency);
        return dependency;
    }

    public int getDepth() {
        return m_Dependencies.stream().map(d -> {
            BuildConfiguration origin = d.getOrigin();
            int depth = 0;
            depth = 1 + origin.getDepth();
            return depth;
        }).max(Comparator.<Integer>naturalOrder()).orElse(0);
    }

    public List<Dependency> getDependencies() {
        return unmodifiableList(m_Dependencies);
    }

    public List<Dependency> getDependents() {
        return m_Dependents;
    }
}
