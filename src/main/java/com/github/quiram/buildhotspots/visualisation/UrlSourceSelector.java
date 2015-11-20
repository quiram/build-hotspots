package com.github.quiram.buildhotspots.visualisation;

public class UrlSourceSelector extends SourceSelector {
    @Override
    protected String getDefaultSourceText() {
        return "url";
    }

    @Override
    protected String getPromptLabel() {
        return "Start by pointing at your Jenkins instance:";
    }
}
