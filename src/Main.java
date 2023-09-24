import model.Coordinate;
import model.Polycube;
import model.PolycubeRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Polycube monoCube = new Polycube();

        PolycubeRepository polycubeRepository = new PolycubeRepository();

        polycubeRepository.add(monoCube);
        //man i got no idea whats going on. shrink method says its shrinking, but nothings when printing stuff at this layer.

        for (int i = 1; i < 2; i++) {
            List<Polycube> polycubes = polycubeRepository.getPolycubes(i);
            for(Polycube polycube: polycubes) {
                List<Coordinate> coordinates = polycube.getValidNewCubePlacements();
                System.out.println("coordinates size" + coordinates.size());
                for(Coordinate coordinate: coordinates) {
                    Polycube newCube = new Polycube(polycube, coordinate);
                    if(!polycubeRepository.exists(newCube)) {
                        polycubeRepository.add(newCube);
                        System.out.println("new polycube found! " + newCube.getCanonicalValue());
                        System.out.println(newCube);
                    }
                }
            }
        }

    }
}