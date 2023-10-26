package controller;

import config.Config;

/**
 * This program calculates the number of permutations for a polycube of size n, resources permitting.
 *
 * @author Benjamin Fausett
 */
public class Main {

    public static void main(String[] args) throws Exception {
        PolycubeEnumerator.calculatePolycubes(12);
    }
}