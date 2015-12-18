package com.github.quiram.buildhotspots.clients.jenkins.beans;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;

public class FirstBuildResponseBeanTest {

    @Test
    public void defaultNumberIsEmptyOptional() {
        final FirstBuildResponseBean firstBuildResponseBean = new FirstBuildResponseBean();
        Optional<Integer> number = firstBuildResponseBean.getNumber();
        assertFalse(number.isPresent());
    }
}
