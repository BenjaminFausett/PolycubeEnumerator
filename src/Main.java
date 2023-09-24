import model.Coordinate;
import model.Polycube;
import model.PolycubeRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Polycube monoCube = new Polycube();

        PolycubeRepository polycubeRepository = new PolycubeRepository();

        polycubeRepository.add(monoCube);
        //im finding 3 different diCubes, which should only be 1. They are all a 2x1 cube just rotated in a different direction. seems i am filtering out the mirror or reflection rotations only.
        //yeah its one diCube doing up and down, one diCube going left and right, and one diCube going forwards and backwards

        for (int i = 1; i < 2; i++) {
            List<Polycube> polycubes = polycubeRepository.getPolycubes(i);
            for(Polycube polycube: polycubes) {
                List<Coordinate> coordinates = polycube.getValidNewCubePlacements();
                for(Coordinate coordinate: coordinates) {
                    Polycube newCube = new Polycube(polycube, coordinate);
                    if(!polycubeRepository.exists(newCube)) {
                        polycubeRepository.add(newCube);
                        System.out.println("new polycube found! " + newCube.hashCode());
                        System.out.println(newCube);
                    }
                }
            }
        }

    }
}