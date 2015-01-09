/*
 * Copyright (c) 2014 - M-net Telekommunikations GmbH
 * All rights reserved.
 * -------------------------------------------------------
 * File created: 06.10.2014
 */
package de.consol.sakuli.javaDSL.actions;

import de.consol.sakuli.loader.BeanLoader;

/**
 * @author Tobias Schneck
 */
public class Environment extends de.consol.sakuli.actions.environment.Environment {


    /**
     * Creates a new Environment object, to control all non-region actions like typing or pasting.
     */
    public Environment() {
        super(false, BeanLoader.loadScreenActionLoader());
    }

    /**
     * Creates a new Environment object, to control all non-region actions like typing or pasting.
     *
     * @param resumeOnException if true, the test execution won't stop on an occurring error.
     */
    public Environment(boolean resumeOnException) {
        super(resumeOnException, BeanLoader.loadScreenActionLoader());
    }
}
