package controller;

import model.Polycube;

public class Main {

    public static void main(String[] args) throws Exception {
        PolycubeEnumerator.calculatePolycubes(7);
        System.out.println(Polycube.trueEqualsCalls);
    }
}